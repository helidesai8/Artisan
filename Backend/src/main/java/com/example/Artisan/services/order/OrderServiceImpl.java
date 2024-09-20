/**
 * This class implements the OrderService interface and provides the implementation for various order-related operations.
 * It handles the checkout process, completing an order, and retrieving order history details.
 */
package com.example.Artisan.services.order;

import com.example.Artisan.DTOs.order.OrderDetailDTO;
import com.example.Artisan.DTOs.order.OrderItemDTO;
import com.example.Artisan.DTOs.payment.PaymentResponseDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.OrderDetail;
import com.example.Artisan.entities.OrderItem;
import com.example.Artisan.entities.PaymentDetail;
import com.example.Artisan.entities.ShippingBillingDetail;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductImage;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.exceptions.PaymentGatewayException;
import com.example.Artisan.repositories.OrderDetailRepository;
import com.example.Artisan.services.UserService;
import com.example.Artisan.services.payment.PaymentService;
import com.example.Artisan.services.products.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements  OrderService{

    private static final BigDecimal centsToDollarMultiplier = new BigDecimal("0.01");
    private final ProductService _productService;
    private final PaymentService _paymentService;
    private final UserService _userService;
    private final OrderDetailRepository _orderDetailRepository;
    private final ModelMapper _modelMapper;

    @Autowired
    public OrderServiceImpl(ProductService productService, PaymentService paymentService,
                            UserService userService, OrderDetailRepository orderDetailRepository,
                            ModelMapper modelMapper) {
        _productService = productService;
        _paymentService = paymentService;
        _userService = userService;
        _orderDetailRepository = orderDetailRepository;
        this._modelMapper = modelMapper;
    }

    @Override
    public String CheckoutOrder(List<ProductBaseDTO> products, String userEmail) {
        if (userEmail == null) {
            throw new IllegalArgumentException("customer email cannot be null");
        }
        try {
            // create checkout items list
            var checkoutProducts = _productService.CheckoutProducts(products);
            if (checkoutProducts == null || checkoutProducts.getProducts().isEmpty()) {
                throw new APIException("Cannot checkout products");
            }

            checkoutProducts.setCustomerEmail(userEmail);
            // generate strip checkout url
            var stripeUrl = _paymentService.initializeCheckout(checkoutProducts);
            return stripeUrl;
        } catch (Exception e) {
            throw new APIException("Payment gateway error");
        }
    }

    @Override
    public boolean completeOrder(List<ProductBaseDTO> orderProducts, String sessionId, String userEmail) {
        if (orderProducts == null || orderProducts.isEmpty()){
            throw new IllegalArgumentException("products not present in order");
        }
        if (sessionId == null || sessionId.isBlank()){
            throw new IllegalArgumentException("stripe session cannot be blank");
        }

        if(userEmail == null || userEmail.isBlank()){
            throw new IllegalArgumentException("user detail cannot be blank");
        }

        try{
            var user = _userService.getUserEntityByEmail(userEmail);
            if(user == null){
                throw new UsernameNotFoundException("user not found");
            }
            // get payment session details
            var paymentResponse = this._paymentService.getSessionDetails(sessionId);
            if(paymentResponse == null){
                throw new PaymentGatewayException("Cannot retrieve payment response from gateway");
            }
            var orderDetail = generateCompleteOrderDetail(paymentResponse, orderProducts);
            orderDetail.setUser(user);
            _orderDetailRepository.save(orderDetail);

            return true;

        }catch(UsernameNotFoundException | PaymentGatewayException ex){
            throw ex;
        }
        catch (Exception e){
            throw new APIException(e.getMessage());
        }
    }

    @Override
    public List<OrderDetailDTO> getOrderHistoryDetails(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new UsernameNotFoundException("user id wrong");
        }
        var orderDetails = this._orderDetailRepository.findByUserEmail(userEmail);
        List<OrderDetailDTO> orders = new ArrayList<>();
        if (orderDetails != null) {
            for (var order : orderDetails) {
                var orderDetailDto = mapToOrderDetailDto(order);
                orders.add(orderDetailDto);
            }
        }
        return orders;

    }

    private OrderDetailDTO mapToOrderDetailDto(OrderDetail order){
        var orderDetail = _modelMapper.map(order, OrderDetailDTO.class);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for(var item : order.getOrderItems()){
            var orderItem = new OrderItemDTO();
            orderItem.setProduct(_productService.mapProductWithImagesAndCategories(item.getProduct()));
            orderItem.setAmount(item.getAmount());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
        }

        orderDetail.setItems(orderItems);
        return orderDetail;
    }

    private OrderDetail generateCompleteOrderDetail(PaymentResponseDTO paymentResponse, List<ProductBaseDTO> orderProducts){
        OrderDetail orderDetail = generateOrderDetail(paymentResponse);
        var orderItems = generateOrderItems(orderProducts, orderDetail);
        orderDetail.setOrderItems(orderItems);
        PaymentDetail paymentDetail = generatePaymentDetail(paymentResponse);
        paymentDetail.setOrderDetail(orderDetail);
        orderDetail.setPaymentDetail(paymentDetail);
        var billingShippingDetail = generateBillingShippingDetail(paymentResponse);
        billingShippingDetail.setOrderDetail(orderDetail);
        orderDetail.setShippingBillingDetail(billingShippingDetail);
        return orderDetail;
    }

    private OrderDetail generateOrderDetail(PaymentResponseDTO paymentResponse){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDate(LocalDateTime.now());
        orderDetail.setTotal(new BigDecimal(paymentResponse.getPayment_intent().getAmount()).multiply(centsToDollarMultiplier));
        return  orderDetail;
    }

    private List<OrderItem> generateOrderItems(List<ProductBaseDTO> orderProducts, OrderDetail orderDetail){
        List<OrderItem> orderItems = new ArrayList<>(orderProducts.size());
        for (var orderProduct: orderProducts){
            var product = this._productService.getProductById(orderProduct.getId());
            UpdateProductInventory(product, orderProduct.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderProduct.getQuantity());
            BigDecimal amount = product.getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
            orderItem.setAmount(amount);
            orderItem.setOrderDetail(orderDetail);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private PaymentDetail generatePaymentDetail(PaymentResponseDTO paymentResponse){
        var paymentMethod = paymentResponse.getPayment_intent().getPayment_method();
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setStatus(paymentResponse.getStatus());
        paymentDetail.setPayment_type(paymentMethod.getType());
        var amountPaid =paymentResponse.getPayment_intent().getAmount();
        paymentDetail.setAmount(new BigDecimal(amountPaid).multiply(centsToDollarMultiplier));
        if(paymentMethod.getCard() != null){
            var cardDetails = paymentMethod.getCard();
            paymentDetail.setCard_number("Ending with "+ cardDetails.getLast4());
            paymentDetail.setExpiry(cardDetails.getExp_month() + "/" + cardDetails.getExp_year());
            paymentDetail.setProvider(cardDetails.getDisplay_brand());
            paymentDetail.setCard_Type(cardDetails.getFunding());
        }

        return  paymentDetail;
    }

    private ShippingBillingDetail generateBillingShippingDetail(PaymentResponseDTO paymentResponse) {
        var capturesBillingShipping = paymentResponse.getShipping_details().getAddress();
        var shippingBillingDetail = _modelMapper.map(capturesBillingShipping, ShippingBillingDetail.class);
        shippingBillingDetail.setName(paymentResponse.getShipping_details().getName());
        var billingDetails = paymentResponse.getPayment_intent().getPayment_method().getBilling_details();
        if(billingDetails != null){
        shippingBillingDetail.setBilling_name(billingDetails.getName());
        shippingBillingDetail.setBilling_country(billingDetails.getAddress().getCountry());
        shippingBillingDetail.setBilling_postal_code(billingDetails.getAddress().getPostal_code());
        }

        return  shippingBillingDetail;
    }

    private void UpdateProductInventory(Product product, int quantity){
        var currentQuantity = product.getProductInventory().getQuantity();
        product.getProductInventory().setQuantity(currentQuantity - quantity);
    }
}
