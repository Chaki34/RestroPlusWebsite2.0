package com.RestroPlusWebsite_20.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "menu_category_details")
@Getter
@Setter
public class MenuCategoryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String howItIsMade;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    private Double price;

    private Double rating;

    private Double sizeSmallPrice;

    private Double sizeMediumPrice;

    private Double sizeLargePrice;

    private Boolean extraSpicy;

    private Boolean extraCheese;

    private Boolean veg;

    private Boolean nonVeg;

    private String preparationTime;

    private Boolean available;



}
