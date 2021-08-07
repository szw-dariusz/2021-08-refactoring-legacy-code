package com.smalaca.taskamanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
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
    public UserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    @Deprecated
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Deprecated
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    @Deprecated
    public void setLogin(String login) {
        this.login = login;
    }

    @Deprecated
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    @Deprecated
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Deprecated
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    @Deprecated
    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Deprecated
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTeamRole() {
        return teamRole;
    }

    @Deprecated
    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }
}
