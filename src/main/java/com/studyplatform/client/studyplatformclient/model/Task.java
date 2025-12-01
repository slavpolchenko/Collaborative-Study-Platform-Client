package com.studyplatform.client.studyplatformclient.model;

import lombok.Data;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private String deadline;
}