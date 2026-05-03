package com.RestroPlusWebsite_20.DTOS;



import java.util.List;

public class MenuRequest {
    private String restaurantRegNo;
    private List<String> categories;

    public MenuRequest() {}

    public MenuRequest(String restaurantRegNo, List<String> categories) {
        this.restaurantRegNo = restaurantRegNo;
        this.categories = categories;
    }

    public String getRestaurantRegNo() { return restaurantRegNo; }
    public void setRestaurantRegNo(String restaurantRegNo) { this.restaurantRegNo = restaurantRegNo; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    @Override
    public String toString() {
        return "MenuRequest{" +
                "restaurantRegNo='" + restaurantRegNo + '\'' +
                ", categories=" + categories +
                '}';
    }
}
