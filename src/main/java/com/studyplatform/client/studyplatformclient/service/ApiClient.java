package com.studyplatform.client.studyplatformclient.service;

import com.google.gson.Gson;
import com.studyplatform.client.studyplatformclient.model.User;
import com.studyplatform.client.studyplatformclient.utils.UserSession;
import okhttp3.*;

import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static boolean login(String email, String password) {
        String jsonInput = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);

        RequestBody body = RequestBody.create(jsonInput, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/auth/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return false;
            }

            if (response.body() != null) {
                String responseString = response.body().string();
                if (responseString.isEmpty()) return false;

                User user = gson.fromJson(responseString, User.class);

                if (user == null || user.getEmail() == null) {
                    return false;
                }

                UserSession.setSession(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean register(String name, String email, String password) {
        String jsonInput = String.format("{\"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", name, email, password);

        RequestBody body = RequestBody.create(jsonInput, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/auth/register")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }

    public static String getGroups() {
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/groups/all")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[]";
    }

    public static String getTasks(Long groupId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/tasks/group/" + groupId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[]";
    }

    public static String getActivityLog() {
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/activity/all")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[]";
    }

    public static boolean createTask(Long groupId, String title, String description, String deadline) {
        String jsonInput = String.format(
                "{\"title\": \"%s\", \"description\": \"%s\", \"deadline\": \"%s\"}",
                title, description, deadline
        );

        RequestBody body = RequestBody.create(jsonInput, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/tasks/create?groupId=" + groupId)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}