package com.RestroPlusWebsite_20.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_categories")
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    // Add image URL to store uploaded image path
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private resturentEntity restaurant;

    // Constructors
    public MenuCategory() {}

    public MenuCategory(String categoryName, resturentEntity restaurant) {
        this.categoryName = categoryName;
        this.restaurant = restaurant;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public resturentEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(resturentEntity restaurant) {
        this.restaurant = restaurant;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
