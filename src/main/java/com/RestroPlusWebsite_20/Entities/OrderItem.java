package com.RestroPlusWebsite_20.Entities;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     =====================================
                ORDER
     =====================================
    */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    /*
     =====================================
              RESTAURANT
     =====================================
    */
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private resturentEntity restaurant;

    /*
     =====================================
               FOOD ITEM
     =====================================
    */
    @ManyToOne
    @JoinColumn(name = "menu_details_id")
    private MenuCategoryDetails menuCategoryDetails;

    private Integer quantity;

    private Double itemPrice;

    private Double totalPrice;

    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public resturentEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(resturentEntity restaurant) {
        this.restaurant = restaurant;
    }

    public MenuCategoryDetails getMenuCategoryDetails() {
        return menuCategoryDetails;
    }

    public void setMenuCategoryDetails(MenuCategoryDetails menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
