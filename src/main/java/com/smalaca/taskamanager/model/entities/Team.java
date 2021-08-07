package com.smalaca.taskamanager.model.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.model.embedded.Codename;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

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

    public void setCodename(Codename codename) {
        this.codename = codename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TeamDto toDto() {
        TeamDto dto = new TeamDto();
        dto.setId(id);
        dto.setName(name);

        if (hasCodename()) {
            dto.setCodenameShort(getShortNameOfCodeName());
            dto.setCodenameFull(getFullNameOfCodeName());
        }

        dto.setDescription(description);
        dto.setUserIds(getMembersIds());
        return dto;
    }

    private boolean hasCodename() {
        return codename != null;
    }

    private String getShortNameOfCodeName() {
        return codename.getShortName();
    }

    private String getFullNameOfCodeName() {
        return codename.getFullName();
    }

    private List<Long> getMembersIds() {
        return members.stream().map(User::getId).collect(toList());
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
}
