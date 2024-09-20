package com.example.Artisan.services.payment;

import com.example.Artisan.DTOs.CheckoutDetailDTO;
import com.example.Artisan.DTOs.payment.PaymentResponseDTO;

public interface PaymentService {
    
    /**
     * Initializes the checkout process for a given checkout detail.
     * 
     * @param checkOutDetail the checkout detail containing the necessary information for the payment
     * @return a string representing the checkout initialization status
     */
    String initializeCheckout(CheckoutDetailDTO checkOutDetail);
    
    /**
     * Retrieves the session details for a given session ID.
     * 
     * @param sessionId the ID of the session for which to retrieve the details
     * @return a PaymentResponseDTO object containing the session details
     */
    PaymentResponseDTO getSessionDetails(String sessionId);
}
