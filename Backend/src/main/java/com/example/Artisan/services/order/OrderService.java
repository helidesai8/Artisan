package com.example.Artisan.services.order;

import com.example.Artisan.DTOs.order.OrderDetailDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.stripe.exception.StripeException;

import java.util.List;

public interface OrderService {
    /**
     * Checkout the order for the given products and user email.
     *
     * @param products  the list of products to be checked out
     * @param userEmail the email of the user placing the order
     * @return the checkout session ID
     */
    String CheckoutOrder(List<ProductBaseDTO> products, String userEmail);

    /**
     * Complete the order for the given products, session ID, and user email.
     *
     * @param orderProducts the list of products in the order
     * @param sessionId     the ID of the checkout session
     * @param userEmail     the email of the user placing the order
     * @return true if the order is successfully completed, false otherwise
     */
    boolean completeOrder(List<ProductBaseDTO> orderProducts, String sessionId, String userEmail);

    /**
     * Get the order history details for the given user email.
     *
     * @param userEmail the email of the user
     * @return the list of order details
     */
    List<OrderDetailDTO> getOrderHistoryDetails(String userEmail);
}
