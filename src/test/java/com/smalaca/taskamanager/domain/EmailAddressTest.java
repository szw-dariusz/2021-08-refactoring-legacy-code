package com.smalaca.taskamanager.domain;

import java.math.BigDecimal;
import java.util.List;

import com.smalaca.taskamanager.model.embedded.EmailAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class EmailAddressTest {

    private static List<Object> notEqualEmailAddresses() {
        EmailAddress emailAddress = new EmailAddress("natasha.romanov@avengers.com");
        return asList(emailAddress, BigDecimal.valueOf(13));
    }

    @Test
    void shouldBeEqual() {
        EmailAddress actual = emailAddress();

        assertThat(actual.equals(emailAddress())).isTrue();
        assertThat(actual.hashCode()).isEqualTo(emailAddress().hashCode());
    }

    @Test
    void shouldBeEqualWithItself() {
        EmailAddress actual = emailAddress();

        assertThat(actual.equals(actual)).isTrue();
        assertThat(actual.hashCode()).isEqualTo(actual.hashCode());
    }

    @Test
    void shouldNotBeEqualToNull() {
        assertThat(emailAddress().equals(null)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("notEqualEmailAddresses")
    void shouldNotBeEqual(Object emailAddress) {
        EmailAddress actual = emailAddress();

        assertThat(actual.equals(emailAddress)).isFalse();
        assertThat(actual.hashCode()).isNotEqualTo(emailAddress.hashCode());
    }

    @Test
    void shouldCreateEmailAddress() {
        String emailAddress = "dummy@fake.domain.com";

        EmailAddress actual = new EmailAddress(emailAddress);

        assertThat(actual.getEmailAddress()).isEqualTo(emailAddress);
    }

    private EmailAddress emailAddress() {
        return new EmailAddress("tony.stark@avengers.com");
    }
}
