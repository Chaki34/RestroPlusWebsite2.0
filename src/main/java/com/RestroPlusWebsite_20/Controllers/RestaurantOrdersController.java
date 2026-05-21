package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.OrderEntity;
import com.RestroPlusWebsite_20.Entities.OrderItem;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.OrderItemRepository;
import com.RestroPlusWebsite_20.repos.OrderRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RestaurantOrdersController {

    @Autowired
    private resturnetRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/orders")
    public String orders(
            HttpSession session,
            Model model
    ) {

        String regNo =
                (String) session.getAttribute(
                        "loggedInRegNo"
                );

        if(regNo == null){
            return "redirect:/restro-login";
        }

        resturentEntity restaurant =
                restaurantRepository
                        .findByResturentRegistrationNo(regNo)
                        .orElseThrow();

        List<OrderItem> orders =
                orderItemRepository
                        .findByRestaurantOrderByIdDesc(
                                restaurant
                        );

        model.addAttribute(
                "orders",
                orders
        );

        return "restaurant-orders";
    }

    @PostMapping("/orders/accept/{id}")
    public String acceptOrder(
            @PathVariable Long id
    ) {

        OrderEntity order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setOrderStatus("ACCEPTED");

        orderRepository.save(order);

        return "redirect:/orders";
    }

    @PostMapping("/orders/reject/{id}")
    public String rejectOrder(
            @PathVariable Long id
    ) {

        OrderEntity order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setOrderStatus("REJECTED");

        orderRepository.save(order);

        return "redirect:/orders";
    }



    @PostMapping("/orders/start/{id}")
    public String startMaking(
            @PathVariable Long id
    ) {

        OrderEntity order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setOrderStatus("PREPARING");

        orderRepository.save(order);

        return "redirect:/orders";
    }

    @PostMapping("/orders/ready/{id}")
    public String markReady(
            @PathVariable Long id
    ) {

        OrderEntity order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setOrderStatus("READY");

        orderRepository.save(order);

        return "redirect:/orders";
    }


    @PostMapping("/orders/delivered/{id}")
    public String markDelivered(
            @PathVariable Long id
    ) {

        OrderEntity order =
                orderRepository.findById(id)
                        .orElseThrow();

        order.setOrderStatus("DELIVERED");

        orderRepository.save(order);

        return "redirect:/orders";
    }


}
