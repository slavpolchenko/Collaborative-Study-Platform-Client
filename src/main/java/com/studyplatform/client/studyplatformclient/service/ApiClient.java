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
            if (response.isSuccessful() && response.body() != null) {
                String responseString = response.body().string();
                User user = gson.fromJson(responseString, User.class);
                UserSession.setSession(user);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
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
                .url(BASE_URL + "/api/groups")
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
}