package com.smalaca.taskamanager.model.entities;

import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.model.embedded.Codename;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
public class Team {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Codename codename;

    private String description;

    @OneToMany
    private List<User> members = new ArrayList<>();

    @ManyToOne
    private Project project;

    @Deprecated
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<User> members) {
        this.members = new ArrayList<>(members);
    }

    @Deprecated
    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        if (!members.contains(user)) {
            throw new RuntimeException();
        }
        members.remove(user);
    }

    @Deprecated
    public Codename getCodename() {
        return codename;
    }

    @Deprecated
    public void setCodename(Codename codename) {
        this.codename = codename;
    }

    @Deprecated
    public String getDescription() {
        return description;
    }

    @Deprecated
    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Team team = (Team) o;

        return new EqualsBuilder()
                .append(id, team.id)
                .append(name, team.name)
                .append(codename, team.codename)
                .append(description, team.description)
                .isEquals();
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(codename)
                .append(description)
                .toHashCode();
    }

    public TeamDto asTeamDto() {
        TeamDto dto = new TeamDto();
        dto.setId(id);
        dto.setName(name);

        if (hasCodename()) {
            dto.setCodenameShort(getShortNameOfCodename());
            dto.setCodenameFull(getFullNameOfCodename());
        }

        dto.setDescription(description);
        dto.setUserIds(getMembersIds());

        return dto;
    }

    private boolean hasCodename() {
        return codename != null;
    }

    private String getShortNameOfCodename() {
        return codename.getShortName();
    }

    private String getFullNameOfCodename() {
        return codename.getFullName();
    }

    private List<Long> getMembersIds() {
        return members.stream().map(User::getId).collect(toList());
    }

    public void update(TeamDto teamDto) {
        if (teamDto.getName() != null) {
            this.name = teamDto.getName();
        }

        if (teamDto.getCodenameShort() != null && teamDto.getCodenameFull() != null) {
            this.codename = new Codename(teamDto.getCodenameShort(), teamDto.getCodenameFull());
        }

        if (teamDto.getDescription() != null) {
            this.description = teamDto.getDescription();
        }
    }
}
