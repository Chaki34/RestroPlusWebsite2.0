package com.RestroPlusWebsite_20.DTOS;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterRequest {

    public String getUsername() {
        return username;
    }

    public Long getPhoneno() {
        return phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneno(Long phoneno) {
        this.phoneno = phoneno;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private Long phoneno;
    private String password;
}

