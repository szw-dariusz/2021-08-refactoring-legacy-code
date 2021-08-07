package com.smalaca.taskamanager.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class TeamDto {

    private Long id;
    private String name;
    private String codenameShort;
    private String codenameFull;
    private String description;
    private List<Long> userIds = new ArrayList<>();

    @Deprecated
    public TeamDto() {
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public String getCodenameShort() {
        return codenameShort;
    }

    @Deprecated
    public void setCodenameShort(String codenameShort) {
        this.codenameShort = codenameShort;
    }

    public String getCodenameFull() {
        return codenameFull;
    }

    @Deprecated
    public void setCodenameFull(String codenameFull) {
        this.codenameFull = codenameFull;
    }

    public String getDescription() {
        return description;
    }

    @Deprecated
    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    @Deprecated
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
