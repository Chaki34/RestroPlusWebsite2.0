package com.RestroPlusWebsite_20.Entities;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "RESTROPLUS_users")
@Data
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = false)
    private String username;

    @NotNull(message = "Phone number is required")
    @Column(nullable = false, unique = true)
    private Long phoneno;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    private String role;

	@Column(name = "city")
	private String city;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;


	// Default constructor
    public UserEntity() {}

    // Parameterized constructor
    public UserEntity(Long id, String username, Long phoneno, String password, String role) {
        this.id = id;
        this.username = username;
        this.phoneno = phoneno;
        this.password = password;
        this.role = role;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(Long phoneno) {
		this.phoneno = phoneno;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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


}


