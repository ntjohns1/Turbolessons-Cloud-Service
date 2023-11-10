package com.noslen.paymentservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document
@Data
@NoArgsConstructor
public class CustomerObject implements Serializable {

    @Id
    private String id;
    private Address address;
    private String email;
    private String name;
    private String phone;
    private String shipping;

    public CustomerObject(Address address, String email, String name, String phone, String shipping) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.shipping = shipping;
    }

    public String getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerObject that = (CustomerObject) o;

        if (!Objects.equals(id,
                            that.id)) return false;
        if (!Objects.equals(address,
                            that.address)) return false;
        if (!Objects.equals(email,
                            that.email)) return false;
        if (!Objects.equals(name,
                            that.name)) return false;
        if (!Objects.equals(phone,
                            that.phone)) return false;
        return Objects.equals(shipping,
                              that.shipping);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (shipping != null ? shipping.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomerRequest{" + "id='" + id + '\'' + ", address=" + address + ", email='" + email + '\'' + ", name='" + name + '\'' + ", phone='" + phone + '\'' + ", shipping='" + shipping + '\'' + '}';
    }
}
