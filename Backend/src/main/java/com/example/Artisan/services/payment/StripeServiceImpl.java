package com.example.Artisan.services.payment;

import com.example.Artisan.DTOs.CheckoutDetailDTO;
import com.example.Artisan.DTOs.payment.PaymentResponseDTO;
import com.example.Artisan.exceptions.PaymentGatewayException;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionRetrieveParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeServiceImpl implements PaymentService {

    private static final int cents = 100;
    @Value("${stripe.secretKey}")
    private String apiKey;

    @Value("${clientBaseUrl}")
    private String clientBaseUrl;

    @Override
    public String initializeCheckout(CheckoutDetailDTO checkOutDetail) {
        if(checkOutDetail == null || checkOutDetail.getProducts() == null){
            throw new IllegalArgumentException("Checkout detail is null");
        }
        if(checkOutDetail.getProducts().isEmpty()){
            throw new IllegalArgumentException("product list cannot be empty");
        }

        Stripe.apiKey = apiKey;
        // Next, create a checkout session by adding the details of the checkout
        SessionCreateParams.Builder paramsBuilder =
            SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(clientBaseUrl + "/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(clientBaseUrl + "/status")
                    .setCustomerEmail(checkOutDetail.getCustomerEmail())
                    .setShippingAddressCollection(
                            SessionCreateParams.ShippingAddressCollection.builder()
                                    .addAllowedCountry(
                                            SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
                                    )
                                    .build()
                    );

        for (var product : checkOutDetail.getProducts()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(product.getQuantity().longValue())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .putMetadata("app_id", product.getId().toString())
                                                            .setName(product.getName())
                                                            .addImage(product.getImg_url())
                                                            .build()
                                            )
                                            .setCurrency("cad")
                                            .setUnitAmountDecimal(product.getPrice().multiply(BigDecimal.valueOf(cents)))
                                            .build())
                            .build());
        }


        Session session = null;
        try {
            session = Session.create(paramsBuilder.build());
        } catch (StripeException e) {
            throw new PaymentGatewayException(e.getMessage());
        }

        return session.getUrl();
    }

    @Override
    public PaymentResponseDTO getSessionDetails(String sessionId){
        if (sessionId == null || sessionId.isBlank()){
            throw new IllegalArgumentException("session id cannot be blank");
        }
        Stripe.apiKey = apiKey;
        SessionRetrieveParams params =
                SessionRetrieveParams.builder().addExpand("payment_intent.payment_method").build();

        try {
            Session session = Session.retrieve(sessionId, params, null);
            Gson gson = new Gson();
            return gson.fromJson(session.toJson(), PaymentResponseDTO.class);
        } catch (StripeException e) {
            throw new PaymentGatewayException(e.getMessage());
        }
    }
}
