package com.smalaca.taskamanager.model.entities;

import com.smalaca.taskamanager.dto.UserDto;
import com.smalaca.taskamanager.model.embedded.EmailAddress;
import com.smalaca.taskamanager.model.embedded.PhoneNumber;
import com.smalaca.taskamanager.model.embedded.UserName;
import com.smalaca.taskamanager.model.enums.TeamRole;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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

    @Deprecated
    public UserName getUserName() {
        return userName;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    @Deprecated
    public String getLogin() {
        return login;
    }

    @Deprecated
    public void setLogin(String login) {
        this.login = login;
    }

    @Deprecated
    public String getPassword() {
        return password;
    }

    @Deprecated
    public void setPassword(String password) {
        this.password = password;
    }

    @Deprecated
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Deprecated
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    @Deprecated
    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Deprecated
    public TeamRole getTeamRole() {
        return teamRole;
    }

    @Deprecated
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
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userName)
                .append(login)
                .append(password)
                .append(phoneNumber)
                .append(emailAddress)
                .append(teamRole)
                .toHashCode();
    }

    public UserDto asUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName(getFirstName());
        userDto.setLastName(getLastName());
        userDto.setLogin(login);
        userDto.setPassword(password);

        if (hasTeamRole()) {
            userDto.setTeamRole(getTeamRoleName());
        }

        if (hasPhoneNumber()) {
            userDto.setPhonePrefix(getPrefixOfPhone());
            userDto.setPhoneNumber(getNumberOfPhone());
        }

        if (hasEmailAddress()) {
            userDto.setEmailAddress(getEmail());
        }
        
        return userDto;
    }

    private String getFirstName() {
        return userName.getFirstName();
    }

    private String getLastName() {
        return userName.getLastName();
    }

    private boolean hasTeamRole() {
        return teamRole != null;
    }

    private String getTeamRoleName() {
        return teamRole.name();
    }

    private boolean hasEmailAddress() {
        return emailAddress != null;
    }

    private String getEmail() {
        return emailAddress.getEmailAddress();
    }

    private boolean hasPhoneNumber() {
        return phoneNumber != null;
    }

    private String getPrefixOfPhone() {
        return phoneNumber.getPrefix();
    }

    private String getNumberOfPhone() {
        return phoneNumber.getNumber();
    }

    public void update(UserDto userDto) {
        if (userDto.getLogin() != null) {
            this.login = userDto.getLogin();
        }

        if (userDto.getPassword() != null) {
            this.password = userDto.getPassword();
        }

        if (userDto.getPhoneNumber() != null) {
            this.phoneNumber = new PhoneNumber(userDto.getPhonePrefix(), userDto.getPhoneNumber());
        }

        if (userDto.getEmailAddress() != null) {
            this.emailAddress = new EmailAddress(userDto.getEmailAddress());
        }

        if (userDto.getTeamRole() != null) {
            this.teamRole = TeamRole.valueOf(userDto.getTeamRole());
        }
    }
}
