package com.noslen.paymentservice.dto;

import java.util.Objects;

public class Address {

    String city;
    String country;
    String line1;
    String line2;
    String postal_code;
    String state;

    public Address() {
    }

    public Address(String city, String country, String line1, String line2, String postal_code, String state) {
        this.city = city;
        this.country = country;
        this.line1 = line1;
        this.line2 = line2;
        this.postal_code = postal_code;
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!Objects.equals(city,
                            address.city)) return false;
        if (!Objects.equals(country,
                            address.country)) return false;
        if (!Objects.equals(line1,
                            address.line1)) return false;
        if (!Objects.equals(line2,
                            address.line2)) return false;
        if (!Objects.equals(postal_code,
                            address.postal_code)) return false;
        return Objects.equals(state,
                              address.state);
    }

    @Override
    public int hashCode() {
        int result = city != null ? city.hashCode() : 0;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (line1 != null ? line1.hashCode() : 0);
        result = 31 * result + (line2 != null ? line2.hashCode() : 0);
        result = 31 * result + (postal_code != null ? postal_code.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Address{" + "city='" + city + '\'' + ", country='" + country + '\'' + ", line1='" + line1 + '\'' + ", line2='" + line2 + '\'' + ", postal_code='" + postal_code + '\'' + ", state='" + state + '\'' + '}';
    }
}
