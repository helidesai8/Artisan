package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.order.OrderDetailDTO;
import com.example.Artisan.DTOs.order.OrderItemDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.services.order.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class OrderControllerTest {
    @Mock
    private Authentication mockAuthentication;

    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void Setup(){
        MockitoAnnotations.openMocks(this);
        Authentication auth = new UsernamePasswordAuthenticationToken("test_user", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void checkout_userNotFound_returnUnauthorised(){
        Authentication auth = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var result = orderController.Checkout(new ArrayList<ProductBaseDTO>());

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void checkout_orderServiceThrowsException_returnInternalServerError(){
        var productList = new ArrayList<ProductBaseDTO>();
        when(orderService.CheckoutOrder(productList, "test_user")).thenThrow(new APIException("test"));

        var result = orderController.Checkout(productList);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void checkout_returnOk(){
        var productList = new ArrayList<ProductBaseDTO>();
        when(orderService.CheckoutOrder(productList, "test_user")).thenReturn("testurl");

        var result = orderController.Checkout(productList);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals("testurl", result.getBody());
    }

    @Test
    void completeOrder_userNotFound_returnUnauthorised(){
        Authentication auth = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var result = orderController.CompleteOrder("sessionId", new ArrayList<ProductBaseDTO>());

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void completeOrder_orderServiceThrowsException_returnInternalServerError(){
        var productList = new ArrayList<ProductBaseDTO>();
        when(orderService.completeOrder(productList, "sessionId","test_user")).thenThrow(new APIException());

        var result = orderController.CompleteOrder("sessionId", productList);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void completeOrder_orderNOtCompleted_returnInternalServerError(){
        var productList = new ArrayList<ProductBaseDTO>();
        when(orderService.completeOrder(productList, "sessionId","test_user")).thenReturn(false);

        var result = orderController.CompleteOrder("sessionId", productList);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void completeOrder_orderCompleted_returnsOk(){
        var productList = new ArrayList<ProductBaseDTO>();
        when(orderService.completeOrder(productList, "sessionId","test_user")).thenReturn(true);

        var result = orderController.CompleteOrder("sessionId", productList);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
     void getOrderHistory_validUser_returnsOrderDetails() {
        String userName = "test_user";
        List<OrderDetailDTO> expectedOrderDetails = createOrderDetailsList();
        when(orderService.getOrderHistoryDetails(userName)).thenReturn(expectedOrderDetails);

        ResponseEntity<List<OrderDetailDTO>> response = orderController.GetOrderHistory();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedOrderDetails, response.getBody());
        //Assertions.assertNull(SecurityContextHolder.getContext());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @NullSource
     void getOrderHistory_InvalidUserName_returnsUnauthorized(String userName) {
        SecurityContextHolder.setContext(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getName()).thenReturn(userName);

        ResponseEntity<List<OrderDetailDTO>> response = orderController.GetOrderHistory();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertNull(response.getBody());

    }

    @Test
     void getOrderHistory_otherException_returnsInternalServerError() {
        String userName = "test_user";
        SecurityContextHolder.setContext(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getName()).thenReturn(userName);
        when(orderService.getOrderHistoryDetails(userName)).thenThrow(new RuntimeException("some error"));

        ResponseEntity<List<OrderDetailDTO>> response = orderController.GetOrderHistory();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @AfterEach
    public void clear(){
        SecurityContextHolder.clearContext();
    }

    // Helper method to create mock data (optional)
    private List<OrderDetailDTO> createOrderDetailsList() {
        List<OrderDetailDTO> orderDetails= new ArrayList<OrderDetailDTO>(3);
        for (int i =1; i <= 3;i++){
            var product = new ProductDTO();
            String imgUrl = "imgurl"+i;
            product.setImageUrls( new ArrayList<String>()
            {{add(imgUrl);}});
            product.setName("product"+i);
            var item = new OrderItemDTO();
            item.setProduct(product);
            item.setQuantity(2);
            item.setAmount(new BigDecimal("222.33"));
            var orderDetail = new OrderDetailDTO();
            orderDetail.setItems(new ArrayList<>()
            {{
                add(item);
            }});
            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }
}
