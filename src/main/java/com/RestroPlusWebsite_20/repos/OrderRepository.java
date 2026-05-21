package com.RestroPlusWebsite_20.repos;

import com.RestroPlusWebsite_20.Entities.OrderEntity;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface OrderRepository
        extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUser(UserEntity user);



}