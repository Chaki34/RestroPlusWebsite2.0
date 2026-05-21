package com.RestroPlusWebsite_20.repos;



import com.RestroPlusWebsite_20.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByRestaurantId(Long restaurantId);

}