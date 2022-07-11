package com.noslen.lsaadminservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "user")
public class LSAUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;

    @Column(length = 50, unique = true)
    private String username;
    // The value of the password will always have a length of
    // 60 thanks to BCrypt
//    @Size(min = 60, max = 60)
//    @Column(name="password", nullable = false, length = 60)
//    @JsonDeserialize(using = BCryptPasswordDeserializer.class )
    private String password;
    private String email;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipcode;
    private String phone;
    private String role;
    private Boolean enabled;
    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static PasswordEncoder getEncoder() {
        return encoder;
    }
    public LSAUser() {

    }
    public LSAUser(Integer id, String firstName, String lastName, String username, String password, String email, String street1, String street2, String city, String state, String zipcode, String phone, String role, Boolean enabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phone = phone;
        this.role = role;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LSAUser lsaUser = (LSAUser) o;
        return enabled == lsaUser.enabled && Objects.equals(id, lsaUser.id) && Objects.equals(firstName, lsaUser.firstName) && Objects.equals(lastName, lsaUser.lastName) && Objects.equals(username, lsaUser.username) && Objects.equals(password, lsaUser.password) && Objects.equals(email, lsaUser.email) && Objects.equals(street1, lsaUser.street1) && Objects.equals(street2, lsaUser.street2) && Objects.equals(city, lsaUser.city) && Objects.equals(state, lsaUser.state) && Objects.equals(zipcode, lsaUser.zipcode) && Objects.equals(phone, lsaUser.phone) && Objects.equals(role, lsaUser.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, password, email, street1, street2, city, state, zipcode, phone, role, enabled);
    }

    @Override
    public String toString() {
        return "LSAUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", street1='" + street1 + '\'' +
                ", street2='" + street2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
