package com.example.Artisan.services;

import com.example.Artisan.DTOs.CheckoutDetailDTO;
import com.example.Artisan.DTOs.products.CheckoutProductDTO;
import com.example.Artisan.exceptions.PaymentGatewayException;
import com.example.Artisan.services.payment.StripeServiceImpl;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class StripeServiceTest {
    @InjectMocks
    private StripeServiceImpl stripeService;
    @Mock
    private Session session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(stripeService, "apiKey", "testKey");
        ReflectionTestUtils.setField(stripeService, "clientBaseUrl", "http://test.com");
    }

    @Test
    public void initializeCheckout_NullCheckoutDetail_throws_IllegalArgumentException() {
        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> stripeService.initializeCheckout(null));
    }

    @Test
    public void initializeCheckout_emptyProductList_throws_IllegalArgumentException() {
        //arrange
        var checkoutDetail = new CheckoutDetailDTO();
        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> stripeService.initializeCheckout(checkoutDetail));
    }

    @Test
    public void getSessionDetail_NullSessionId_throws_IllegalArgumentException() {
        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> stripeService.getSessionDetails(null));
    }

    @Test
    public void getSessionDetail_blankSessionId_throws_IllegalArgumentException() {
        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> stripeService.getSessionDetails("   "));
    }

    @Test
    public void initializeCheckout_Success() {
        try {
            CheckoutDetailDTO checkoutDetailDTO = new CheckoutDetailDTO();
            CheckoutProductDTO product = new CheckoutProductDTO();
            product.setId(1L);
            product.setQuantity(1);
            product.setPrice(BigDecimal.ZERO);
            checkoutDetailDTO.setProducts(Collections.singletonList(product));
            when(session.getUrl()).thenReturn("http://test.com");

            String url = stripeService.initializeCheckout(checkoutDetailDTO);

            assertEquals("http://test.com", url);
        } catch (PaymentGatewayException e) {
            System.err.println("Payment gateway error: " + e.getMessage());
        }
    }

    @Test
    public void initializeCheckout_NullCheckoutDetail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> stripeService.initializeCheckout(null));
    }

    @Test
    public void initializeCheckout_EmptyProductList_ThrowsIllegalArgumentException() {
        CheckoutDetailDTO checkoutDetailDTO = new CheckoutDetailDTO();
        assertThrows(IllegalArgumentException.class, () -> stripeService.initializeCheckout(checkoutDetailDTO));
    }


    @Test
    public void getSessionDetails_NullSessionId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> stripeService.getSessionDetails(null));
    }

    @Test
    public void getSessionDetails_BlankSessionId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> stripeService.getSessionDetails(" "));
    }


}
