package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.order.OrderDetailDTO;
import com.example.Artisan.DTOs.products.ProductBaseDTO;
import com.example.Artisan.services.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling order-related operations.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService _orderService;

    /**
     * Constructor for OrderController.
     * @param orderService The OrderService instance to be used for order operations.
     */
    public OrderController(OrderService orderService) {
        this._orderService = orderService;
    }

    /**
     * Endpoint for checking out an order.
     * @param products The list of products to be checked out.
     * @return A ResponseEntity containing the checkout URL if successful, or an error message if unsuccessful.
     */
    @PostMapping("/checkout")
    public ResponseEntity<String> Checkout(@RequestBody List<ProductBaseDTO> products) {
        try {
            // get user details from token
            var userName = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userName == null || userName.isBlank()) {
                throw new UsernameNotFoundException("user doesn't exist");
            }
            // Initialize checkout
            String checkoutUrl = _orderService.CheckoutOrder(products, userName);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(checkoutUrl);
        }catch (UsernameNotFoundException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    /**
     * Endpoint for completing an order.
     * @param sessionId The session ID of the order.
     * @param products The list of products in the order.
     * @return A ResponseEntity containing true if the order was completed successfully, or false if unsuccessful.
     */
    @PostMapping("/complete-order")
    public ResponseEntity<Boolean> CompleteOrder(@RequestParam("session_id") String sessionId, @RequestBody List<ProductBaseDTO> products) {
        try {
            // get user details from token
            var userName = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userName == null || userName.isBlank()) {
                throw new UsernameNotFoundException("user doesn't exist");
            }
            // complete order and save details
            var isComplete = _orderService.completeOrder(products, sessionId, userName);
            if(isComplete){
                // update order details
                return ResponseEntity.ok(true);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        } catch (UsernameNotFoundException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * Endpoint for retrieving the order history of a user.
     * @return A ResponseEntity containing the list of order details if successful, or an error message if unsuccessful.
     */
    @GetMapping("/history")
    public ResponseEntity<List<OrderDetailDTO>> GetOrderHistory() {
        try {
            // get user details from token
            var userName = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userName == null || userName.isBlank()) {
                throw new UsernameNotFoundException("user doesn't exist");
            }
            // get order history details
            var orderDetails = _orderService.getOrderHistoryDetails(userName);
            return ResponseEntity.ok(orderDetails);

        } catch (UsernameNotFoundException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
