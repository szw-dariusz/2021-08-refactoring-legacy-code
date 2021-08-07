package com.smalaca.taskamanager.dto;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String phoneNumber;
    private String phonePrefix;
    private String emailAddress;
    private String teamRole;

    @Deprecated
    public UserDto() {}

    private UserDto(Builder builder) {
        id = builder.id;
        firstName = builder.firstName;
        lastName = builder.lastName;
        login = builder.login;
        password = builder.password;
        teamRole = builder.teamRole;
        phonePrefix = builder.phonePrefix;
        phoneNumber = builder.phoneNumber;
        emailAddress = builder.emailAddress;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String login;
        private String password;
        private String teamRole;
        private String phonePrefix;
        private String phoneNumber;
        private String emailAddress;

        public static Builder userDto() {
            return new Builder();
        }

        public UserDto build() {
            return new UserDto(this);
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            return this;
        }

        public Builder withCredentials(String login, String password) {
            this.login = login;
            this.password = password;
            return this;
        }

        public Builder withTeamRole(String teamRole) {
            this.teamRole = teamRole;
            return this;
        }

        public Builder withPhone(String prefix, String number) {
            phonePrefix = prefix;
            phoneNumber = number;
            return this;
        }

        public Builder withEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }
    }
}
