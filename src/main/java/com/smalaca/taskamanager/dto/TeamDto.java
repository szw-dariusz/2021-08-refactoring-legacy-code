package com.smalaca.taskamanager.dto;

import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

import java.util.ArrayList;
import java.util.List;

public class TeamDto {
    private Long id;
    private String name;
    private String codenameShort;
    private String codenameFull;
    private String description;
    private List<Long> userIds = new ArrayList<>();

    @Deprecated
    public TeamDto() {}

    private TeamDto(Builder builder) {
        id = builder.id;
        name = builder.name;
        codenameShort = builder.shortName;
        codenameFull = builder.fullName;
        description = builder.description;
        userIds = builder.membersIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodenameShort() {
        return codenameShort;
    }

    public void setCodenameShort(String codenameShort) {
        this.codenameShort = codenameShort;
    }

    public String getCodenameFull() {
        return codenameFull;
    }

    public void setCodenameFull(String codenameFull) {
        this.codenameFull = codenameFull;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String fullName;
        private String description;
        private List<Long> membersIds;
        private String shortName;

        public static Builder teamDto() {
            return new Builder();
        }

        public TeamDto build() {
            return new TeamDto(this);
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withUserIds(List<Long> membersIds) {
            this.membersIds = membersIds;
            return this;
        }

        public Builder withCodename(String shortName, String fullName) {
            this.shortName = shortName;
            this.fullName = fullName;
            return this;
        }
    }
}
