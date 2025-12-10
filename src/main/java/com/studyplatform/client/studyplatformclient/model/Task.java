package com.studyplatform.client.studyplatformclient.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Task {
    private Long taskId; // Важно: совпадает с сервером
    private String title;
    private String description;
    private String deadline;

    public Task(Long taskId, String title, String description, String deadline) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public Long getId() { return taskId; }
}