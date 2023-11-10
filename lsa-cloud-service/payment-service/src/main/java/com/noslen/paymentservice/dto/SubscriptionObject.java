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
public class SubscriptionObject implements Serializable {

    @Id
    private String id;
    private String customer;

    public SubscriptionObject(String customer) {
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubscriptionObject that = (SubscriptionObject) o;

        if (!Objects.equals(id,
                            that.id)) return false;
        return Objects.equals(customer,
                              that.customer);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" + "id='" + id + '\'' + ", customer='" + customer + '\'' + '}';
    }
}

