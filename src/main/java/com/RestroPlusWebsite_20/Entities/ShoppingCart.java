package com.RestroPlusWebsite_20.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     =====================================
                USER
     =====================================
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /*
     =====================================
            FOOD ITEM DETAILS
     =====================================
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_details_id")
    private MenuCategoryDetails menuCategoryDetails;

    /*
     =====================================
                BILLING
     =====================================
    */

    private Integer quantity;

    private Double itemPrice;

    private Double totalPrice;

    /*
     =====================================
            SAVE / ORDER FLAGS
     =====================================
    */

    private Boolean savedForLater = false;

    private Boolean ordered = false;

    /*
     =====================================
            CONSTRUCTORS
     =====================================
    */

    public ShoppingCart() {
    }

    /*
     =====================================
            GETTERS & SETTERS
     =====================================
    */

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public Boolean getSavedForLater() {
        return savedForLater;
    }

    public void setSavedForLater(Boolean savedForLater) {
        this.savedForLater = savedForLater;
    }

    public Boolean getOrdered() {
        return ordered;
    }

    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }
}