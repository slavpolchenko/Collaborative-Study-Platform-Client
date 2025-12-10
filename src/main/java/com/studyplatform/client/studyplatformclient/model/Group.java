package com.studyplatform.client.studyplatformclient.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Group {
    private Long groupId;
    private String name;
    private String description;

    public Group(Long groupId, String name, String description) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
    }
}