package com.RestroPlusWebsite_20.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "restaurants")
public class resturentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String resturentname;

    @NotBlank
    private String ownername;

    @NotBlank
    private String address;

    @NotBlank
    @Column(unique = true)
    private String resturentRegistrationNo;

    private String role;

    @NotBlank
    @Column(unique = true)
    private String password;

    public @NotBlank String getResturentname() {
        return resturentname;
    }

    public @NotBlank String getAddress() {
        return address;
    }

    public @NotBlank String getResturentRegistrationNo() {
        return resturentRegistrationNo;
    }

    public void setResturentRegistrationNo(@NotBlank String resturentRegistrationNo) {
        this.resturentRegistrationNo = resturentRegistrationNo;
    }

    public void setAddress(@NotBlank String address) {
        this.address = address;
    }

    public @NotBlank String getOwnername() {
        return ownername;
    }

    public void setOwnername(@NotBlank String ownername) {
        this.ownername = ownername;
    }

    public void setResturentname(@NotBlank String resturentname) {
        this.resturentname = resturentname;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    private String imageUrl;       // Path to restaurant image
    private String type;           // e.g., Italian, Fast Food
    private double rating;

    private String menuCard;

    // LOCATION FIELDS

    private Double latitude;

    private Double longitude;

    private String locationName;

    private String municipality;

    private String district;

    private String state;

    private boolean active = false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // NEW FIELDS FOR DISPLAY

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getMenuCard() {
        return menuCard;
    }

    public void setMenuCard(String menuCard) {
        this.menuCard = menuCard;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}

