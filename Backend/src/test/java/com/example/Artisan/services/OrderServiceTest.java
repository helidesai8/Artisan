package com.example.Artisan.services;

import com.example.Artisan.DTOs.CheckoutDetailDTO;
import com.example.Artisan.DTOs.order.OrderDetailDTO;
import com.example.Artisan.DTOs.order.OrderItemDTO;
import com.example.Artisan.DTOs.payment.AddressDTO;
import com.example.Artisan.DTOs.payment.PaymentResponseDTO;
import com.example.Artisan.DTOs.products.CheckoutProductDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.OrderDetail;
import com.example.Artisan.entities.OrderItem;
import com.example.Artisan.entities.ShippingBillingDetail;
import com.example.Artisan.entities.User;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.products.ProductImage;
import com.example.Artisan.entities.products.ProductInventory;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.exceptions.PaymentGatewayException;
import com.example.Artisan.repositories.OrderDetailRepository;
import com.example.Artisan.services.order.OrderServiceImpl;
import com.example.Artisan.services.payment.PaymentService;
import com.example.Artisan.services.products.ProductService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private ProductService _productService;

    @Mock
    private PaymentService _paymentService;

    @Mock
    private UserService _userService;

    @Mock
    private OrderDetailRepository _orderDetailRepository;

    @Mock
    private ModelMapper _modelMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckoutOrder_NullCustomerEmail_throws_IllegalArgumentException() {
        // Arrange
        List<ProductBaseDTO> products = new ArrayList<>();
        String customerEmail = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class ,() -> orderService.CheckoutOrder(products, customerEmail));
    }

    @Test
     void testCheckoutOrder_EmptyCheckoutProductList_throwsApiException() {
        // Arrange
        List<ProductBaseDTO> products = List.of();
        CheckoutDetailDTO checkoutDetail= new CheckoutDetailDTO();
        checkoutDetail.setProducts(List.of());

        when(_productService.CheckoutProducts(products)).thenReturn(checkoutDetail);

        // Act and Assert
        assertThrows(APIException.class ,() -> orderService.CheckoutOrder(products, "test"), "Empty checkout products");
    }

    @Test
     void testCheckoutOrder_nullCheckoutDetail_throwsApiException() {
        // Arrange
        List<ProductBaseDTO> products = List.of();
        when(_productService.CheckoutProducts(products)).thenReturn(null);

        // Act and Assert
        assertThrows(APIException.class ,() -> orderService.CheckoutOrder(products, "test"), "Null checkout detail");
    }

    @Test
     void testCheckoutOrder_stripeThrowsException_throwsApiException() {
        // Arrange
        List<CheckoutProductDTO> products = new ArrayList<CheckoutProductDTO>() {{
            add(new CheckoutProductDTO("test", new BigDecimal("78.9"), null));
        }};
        CheckoutDetailDTO checkoutDetail= new CheckoutDetailDTO();
        checkoutDetail.setProducts(products);
        List<ProductBaseDTO> input = List.of();
        when(_productService.CheckoutProducts(any())).thenReturn(checkoutDetail);
        when(_paymentService.initializeCheckout(any())).thenThrow(new PaymentGatewayException("stripe exception"));
        // Act and Assert
        assertThrows(APIException.class ,() -> orderService.CheckoutOrder(input, "test"), "stripe exception");
    }

    @Test
     void testCheckoutOrder_returnsStripeUrl() {
        // Arrange
        List<CheckoutProductDTO> products = new ArrayList<CheckoutProductDTO>() {{
            add(new CheckoutProductDTO("test", new BigDecimal("78.9"), null));
        }};
        CheckoutDetailDTO checkoutDetail= new CheckoutDetailDTO();
        checkoutDetail.setProducts(products);
        List<ProductBaseDTO> input = List.of();
        when(_productService.CheckoutProducts(any())).thenReturn(checkoutDetail);
        when(_paymentService.initializeCheckout(any())).thenReturn("testUrl");
        // Act
        var result = orderService.CheckoutOrder(input, "test");
        // Assert
        Assertions.assertEquals("testUrl" , result , "stripe url");
    }

    @Test
     void completeOrder_emptyProductList_throwsIllegalArgumentException() {
        // Arrange
        List<ProductBaseDTO> orderProducts = List.of();
        String sessionId = "valid_session_id";
        String userEmail = "test@example.com";

        // Act and assert
        assertThrows(IllegalArgumentException.class, () ->  orderService.completeOrder(orderProducts, sessionId, userEmail));
    }

    @Test
     void completeOrder_nullProductList_throwsIllegalArgumentException() {
        // Arrange
        String sessionId = "valid_session_id";
        String userEmail = "test@example.com";

        // Act and assert
        assertThrows(IllegalArgumentException.class, () ->  orderService.completeOrder(null, sessionId, userEmail));
    }


    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @NullSource
     void completeOrder_NullEmptySessionId_throwsIllegalArgumentException(String sessionId) {
        // Arrange
        List<ProductBaseDTO> orderProducts = new ArrayList<>(){{
            add(new ProductBaseDTO(2L, 2));
        }};
        String userEmail = "test@example.com";

        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> orderService.completeOrder(orderProducts, sessionId, userEmail));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @NullSource
     void completeOrder_EmptyUserEmail(String userEmail) {
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String sessionId = "valid_session_id";

        // Act and assert
        assertThrows(IllegalArgumentException.class, () -> orderService.completeOrder(orderProducts, sessionId, userEmail));
    }

    @Test
     void completeOrder_InvalidUser_throwsUsernameNotFoundException(){
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        when(_userService.getUserEntityByEmail(userEmail)).thenReturn(null);

        // Act and assert
        assertThrows(UsernameNotFoundException.class, () -> orderService.completeOrder(orderProducts, sessionId, userEmail));
    }

    @Test
     void completeOrder_nullPaymentResponse_throwsPaymentGatewayException(){
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        when(_userService.getUserEntityByEmail(userEmail)).thenReturn(new User());
        when(_paymentService.getSessionDetails(sessionId)).thenReturn(null);

        // Act and assert
        assertThrows(PaymentGatewayException.class, () -> orderService.completeOrder(orderProducts, sessionId, userEmail));
    }

    @Test
     void completeOrder_paymentTotalAmount_convertsToDecimal_orderDetail() {
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        mockCompleteOrderDependencies(userEmail, sessionId);
        //Act and assert
        orderService.completeOrder(orderProducts, sessionId, userEmail);
        ArgumentCaptor<OrderDetail> argumentCaptor = ArgumentCaptor.forClass(OrderDetail.class);
        verify(_orderDetailRepository).save(argumentCaptor.capture());

        OrderDetail capturedArgument = argumentCaptor.getValue();
        Assertions.assertEquals(new BigDecimal("90.67"), capturedArgument.getTotal(), "Order detail total amount converted to decimal");
    }

    @Test
     void completeOrder_noCardDetails_completesSuccessfully() {
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        mockCompleteOrderDependencies(userEmail, sessionId);
        var paymenteResponse = CreatePaymentResponse();
        paymenteResponse.getPayment_intent().getPayment_method().setCard(null);
        when(_paymentService.getSessionDetails(sessionId)).thenReturn(paymenteResponse);

        //Act and assert
        var result = orderService.completeOrder(orderProducts, sessionId, userEmail);
        ArgumentCaptor<OrderDetail> argumentCaptor = ArgumentCaptor.forClass(OrderDetail.class);
        verify(_orderDetailRepository).save(argumentCaptor.capture());

        OrderDetail capturedArgument = argumentCaptor.getValue();
        Assertions.assertNull(capturedArgument.getPaymentDetail().getCard_number(), "payment method other than card");
        Assertions.assertTrue(result, "Successful order with payment method other than card");
    }

    @Test
     void completeOrder_CardNumber_returnsEndingWith() {
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        mockCompleteOrderDependencies(userEmail, sessionId);

        //Act and assert
        var result = orderService.completeOrder(orderProducts, sessionId, userEmail);
        ArgumentCaptor<OrderDetail> argumentCaptor = ArgumentCaptor.forClass(OrderDetail.class);
        verify(_orderDetailRepository).save(argumentCaptor.capture());

        OrderDetail capturedArgument = argumentCaptor.getValue();
        Assertions.assertEquals("Ending with 4242", capturedArgument.getPaymentDetail().getCard_number(), "card number set");
    }

    @Test
     void completeOrder_InventoryUpdated_forOrderItems() {
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        mockCompleteOrderDependencies(userEmail, sessionId);

        //Act and assert
        orderService.completeOrder(orderProducts, sessionId, userEmail);
        ArgumentCaptor<OrderDetail> argumentCaptor = ArgumentCaptor.forClass(OrderDetail.class);
        verify(_orderDetailRepository).save(argumentCaptor.capture());

        OrderDetail capturedArgument = argumentCaptor.getValue();
        var product1Inventory = capturedArgument.getOrderItems().stream()
                .filter(t -> t.getProduct().getId() == 1).findFirst().map(x -> x.getProduct().getProductInventory());
        Assertions.assertEquals(96, product1Inventory.get().getQuantity() , "Inventory updated for product order item");
    }

    @Test
     void completeOrder_validResponse(){
        // Arrange
        List<ProductBaseDTO> orderProducts = createProductOrder();
        String userEmail = "test@example.com";
        String sessionId = "valid_session_id";
        mockCompleteOrderDependencies(userEmail, sessionId);
        // Act
        var result = orderService.completeOrder(orderProducts, sessionId, userEmail);
        assertTrue(result, "successful order completion");
    }

    @Test
    void getOrderHistoryDetails_blankEmail_throwsUsernameNotFoundException() {
        String userEmail = "";

        assertThrows(UsernameNotFoundException.class, () -> orderService.getOrderHistoryDetails(userEmail));

        verify(_orderDetailRepository, times(0)).findByUserEmail(userEmail);
    }

    @Test
     void getOrderHistoryDetails_nullEmail_throwsUsernameNotFoundException() {
        String userEmail = null;

        assertThrows(UsernameNotFoundException.class, () -> orderService.getOrderHistoryDetails(userEmail));
        verify(_orderDetailRepository, times(0)).findByUserEmail(userEmail);
    }

    @Test
     void getOrderHistoryDetails_noOrdersFound_returnsEmptyList() {
        String userEmail = "test@email.com";
        when(_orderDetailRepository.findByUserEmail(userEmail)).thenReturn(List.of());

        List<OrderDetailDTO> orderDetails = orderService.getOrderHistoryDetails(userEmail);

        assertNotNull(orderDetails);
        assertTrue(orderDetails.isEmpty());
        verify(_orderDetailRepository, times(1)).findByUserEmail(userEmail);
    }

    @Test
     void getOrderHistoryDetails_withOrders_mapsToDTOs() throws Exception {
        String userEmail = "test@email.com";
        OrderDetail orderDetail = createOrderDetail();
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetail);
        when(_orderDetailRepository.findByUserEmail(userEmail)).thenReturn(orderDetailsList);
        when(_productService.mapProductWithImagesAndCategories(orderDetail.getOrderItems().get(0).getProduct())).thenReturn(createProductDTO()); // Implement logic to create ProductDTO
        when(_modelMapper.map(any(OrderDetail.class), eq(OrderDetailDTO.class))).thenReturn(mapToOrderDetailDto());
        List<OrderDetailDTO> actualOrderDetails = orderService.getOrderHistoryDetails(userEmail);

        assertNotNull(actualOrderDetails);
        assertEquals(1, actualOrderDetails.size());

        // Verify mapping and interactions
        OrderDetailDTO expectedDTO = mapToOrderDetailDto();
        verify(_productService, times(1)).mapProductWithImagesAndCategories(orderDetail.getOrderItems().get(0).getProduct());
        assertEquals(expectedDTO, actualOrderDetails.get(0));

        verify(_orderDetailRepository, times(1)).findByUserEmail(userEmail);
    }

    // Helper methods to create mock data
    private OrderDetail createOrderDetail() {
        var product = new Product();
        String imgUrl = "imgUrl";
        var imgEntity = new ProductImage();
        imgEntity.setUrl(imgUrl);
        product.setImageUrls( new HashSet<>()
        {{add(imgEntity);}});
        product.setName("product1");
        var item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setAmount(new BigDecimal("222.33"));
        var orderDetail = new OrderDetail();
        orderDetail.setOrderItems(new ArrayList<>()
        {{
            add(item);
        }});

        return orderDetail;
    }

    private ProductDTO createProductDTO() {
        var product = new ProductDTO();
        product.setImageUrls( new ArrayList<String>()
        {{add("imgUrl");}});
        product.setName("product1");

        return product;
    }

    private OrderDetailDTO mapToOrderDetailDto() {
        var product = createProductDTO();
        var item = new OrderItemDTO();
        item.setProduct(product);
        item.setQuantity(2);
        item.setAmount(new BigDecimal("222.33"));
        var orderDetail = new OrderDetailDTO();
        orderDetail.setItems(new ArrayList<>()
        {{
            add(item);
        }});

       return orderDetail;
    }

    private List<ProductBaseDTO> createProductOrder(){
        return new ArrayList<>(){{
            add(new ProductBaseDTO(1L, 4));
            add(new ProductBaseDTO(2L, 2));
        }};
    }

    private PaymentResponseDTO CreatePaymentResponse(){
        Gson gson = new Gson();
        String jsonResponse= "{ \"customer_email\": \"test@example.com\", \"payment_intent\": { \"amount\": 9067, \"amount_received\": 9067, \"currency\": \"cad\", \"payment_method\": { \"billing_details\": { \"address\": { \"country\": \"CA\", \"postal_code\": \"B3H 4R2\" }, \"email\": \"test@example.com\", \"name\": \"abc\" }, \"card\": { \"brand\": \"visa\", \"country\": \"US\", \"display_brand\": \"visa\", \"exp_month\": 9, \"exp_year\": 2028, \"funding\": \"credit\", \"last4\": \"4242\" }, \"type\": \"card\" }, \"status\": \"succeeded\" }, \"payment_status\": \"paid\", \"shipping_details\": { \"address\": { \"city\": \"Halifax\", \"country\": \"CA\", \"line1\": \"5426, Portland Pl\", \"postal_code\": \"B3K 1A1\", \"state\": \"NS\" }, \"name\": \"test name\" }, \"status\": \"complete\" }";
        return gson.fromJson(jsonResponse, PaymentResponseDTO.class);
    }

    private Product CreateProductEntity(long productId){
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("23.11").multiply(new BigDecimal(productId)));
        ProductInventory productInventory = new ProductInventory();
        productInventory.setQuantity(100);
        product.setProductInventory(productInventory);
        return product;
    }

    private void mockCompleteOrderDependencies(String userEmail, String sessionId){
        when(_userService.getUserEntityByEmail(userEmail)).thenReturn(new User());
        when(_paymentService.getSessionDetails(sessionId)).thenReturn(CreatePaymentResponse());
        when(_productService.getProductById(1L)).thenReturn(CreateProductEntity(1));
        when(_productService.getProductById(2L)).thenReturn(CreateProductEntity(2));
        when(_orderDetailRepository.save(any())).thenReturn(new OrderDetail());
        when(_modelMapper.map(any(AddressDTO.class), eq(ShippingBillingDetail.class))).thenReturn(new ShippingBillingDetail());
    }
}
