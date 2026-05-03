package com.RestroPlusWebsite_20.DTOS;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResturentRegisterRequest {

    private String resturentname;
    private String ownername;
    private String address;
    private String resturentRegistrationNo;
    private String password;

    public String getResturentname() {
        return resturentname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResturentRegistrationNo() {
        return resturentRegistrationNo;
    }

    public void setResturentRegistrationNo(String resturentRegistrationNo) {
        this.resturentRegistrationNo = resturentRegistrationNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public void setResturentname(String resturentname) {
        this.resturentname = resturentname;
    }

    // NEW FIELDS FOR FRONT-END DISPLAY
    private String type;       // e.g., Italian, Fast Food
    private String imageUrl;   // Path to restaurant image
}
