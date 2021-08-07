package com.smalaca.taskamanager.model.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.smalaca.taskamanager.model.embedded.*;
import com.smalaca.taskamanager.model.enums.TeamRole;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@SuppressWarnings("MethodCount")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String password;

    @Embedded
    private UserName userName;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private EmailAddress emailAddress;

    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;

    @OneToMany
    private List<Team> teams = new ArrayList<>();

    public UserName getUserName() {
        return userName;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Deprecated
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Deprecated
    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Deprecated
    public TeamRole getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(TeamRole teamRole) {
        this.teamRole = teamRole;
    }

    public Long getId() {
        return id;
    }

    public void setTeams(List<Team> teams) {
        this.teams = new ArrayList<>(teams);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addToTeam(Team team) {
        teams.add(team);
    }

    public void removeFrom(Team team) {
        if (!teams.contains(team)) {
            throw new RuntimeException();
        }
        teams.remove(team);
    }

    public boolean hasPhoneNumber() {
        return phoneNumber != null;
    }

    public String getNumberOfPhone() {
        return phoneNumber.getNumber();
    }

    public String getPrefixOfPhone() {
        return phoneNumber.getPrefix();
    }

    public boolean hasEmailAddress() {
        return emailAddress != null;
    }

    public String getEmail() {
        return emailAddress.getEmailAddress();
    }

    public boolean hasTeamRole() {
        return teamRole != null;
    }

    public String getNameOfTeamRole() {
        return teamRole.name();
    }

    public String getUserLastName() {
        return userName.getLastName();
    }

    public String getUserFirstName() {
        return userName.getFirstName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(userName, user.userName)
                .append(login, user.login)
                .append(password, user.password)
                .append(phoneNumber, user.phoneNumber)
                .append(emailAddress, user.emailAddress)
                .append(teamRole, user.teamRole)
                .isEquals();
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(userName)
                                          .append(login)
                                          .append(password)
                                          .append(phoneNumber)
                                          .append(emailAddress)
                                          .append(teamRole)
                                          .toHashCode();
    }
}
