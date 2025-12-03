package com.studyplatform.client.studyplatformclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private String action;
    private String timestamp;
    // Можна додати userId, якщо сервер його повертає
}