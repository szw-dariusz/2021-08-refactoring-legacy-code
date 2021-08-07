package com.smalaca.taskamanager.model.embedded;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class PhoneNumber {

    @Column(name = "phone_prefix")
    private String prefix;

    @Column(name = "phone_number")
    private String number;

    @Deprecated
    public PhoneNumber() {
    }

    public PhoneNumber(final String prefix, final String number) {
        this.prefix = prefix;
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    @Deprecated
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNumber() {
        return number;
    }

    @Deprecated
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhoneNumber that = (PhoneNumber) o;

        return new EqualsBuilder()
                .append(prefix, that.prefix)
                .append(number, that.number)
                .isEquals();
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(prefix)
                .append(number)
                .toHashCode();
    }
}
