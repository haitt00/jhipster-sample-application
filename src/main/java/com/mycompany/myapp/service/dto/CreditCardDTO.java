package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CreditCard} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CreditCardDTO implements Serializable {

    private Long id;

    private String number;

    private String bank;

    private CustomerDTO owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public CustomerDTO getOwner() {
        return owner;
    }

    public void setOwner(CustomerDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditCardDTO)) {
            return false;
        }

        CreditCardDTO creditCardDTO = (CreditCardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, creditCardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", bank='" + getBank() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}
