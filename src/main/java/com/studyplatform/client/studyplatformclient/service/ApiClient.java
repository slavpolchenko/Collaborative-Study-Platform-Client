package com.studyplatform.client.studyplatformclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.studyplatform.client.studyplatformclient.model.Task;
import com.studyplatform.client.studyplatformclient.model.User;
import com.studyplatform.client.studyplatformclient.utils.UserSession;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api"; // Тарас использует /api как префикс
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final CookieJar cookieJar = new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) { cookieStore.put(url.host(), cookies); }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) { return cookieStore.getOrDefault(url.host(), new ArrayList<>()); }
    };

    private static final OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

    // --- ЛОГИН ---
    public static boolean login(String email, String password) {
        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL + "/auth/login").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                User user = gson.fromJson(response.body().string(), User.class);
                UserSession.setSession(user);
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // --- НОВЫЙ МЕТОД: ОБНОВЛЕНИЕ ПРОФИЛЯ ---
    public static boolean updateUser(Long id, String name, String email) {
        String jsonInput = String.format("{\"name\": \"%s\", \"email\": \"%s\"}", name, email);
        RequestBody body = RequestBody.create(jsonInput, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/users/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    // --- МЕТОД ТАРАСА: JOIN GROUP ---
    public static boolean joinGroup(Long groupId) {
        Long userId = 1L;
        if (UserSession.getInstance() != null && UserSession.getInstance().getUser() != null) {
            userId = UserSession.getInstance().getUser().getId();
        }

        // Тарас: /memberships/add?userId=...&groupId=...
        String url = BASE_URL + "/memberships/add?userId=" + userId + "&groupId=" + groupId;
        System.out.println("ОТПРАВЛЯЮ ЗАПРОС ТАРАСУ: " + url);

        Request request = new Request.Builder().url(url).post(RequestBody.create(new byte[0], null)).build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("ОТВЕТ СЕРВЕРА: " + response.code());
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- СОЗДАНИЕ ГРУППЫ ---
    public static boolean createGroup(String name, String description) {
        String json = String.format("{\"name\": \"%s\", \"description\": \"%s\"}", name, description);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL + "/groups").post(body).build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("CREATE GROUP STATUS: " + response.code());
            return response.isSuccessful();
        } catch (Exception e) { return false; }
    }

    public static String getGroups() {
        Request request = new Request.Builder().url(BASE_URL + "/groups").get().build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) return response.body().string();
        } catch (IOException e) {}
        return "[]";
    }

    // --- ЗАДАЧИ ---
    public static String getTasks(Long groupId) {
        Request request = new Request.Builder().url(BASE_URL + "/tasks/group/" + groupId).get().build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) return response.body().string();
        } catch (IOException e) {}
        return "[]";
    }

    public static boolean createTask(Long groupId, String title, String description, String deadline) {
        if (!deadline.contains("T")) deadline += "T00:00:00";
        String json = String.format("{\"title\": \"%s\", \"description\": \"%s\", \"deadline\": \"%s\"}", title, description, deadline);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL + "/tasks/create?groupId=" + groupId).post(body).build();
        try (Response response = client.newCall(request).execute()) { return response.isSuccessful(); }
        catch (Exception e) { return false; }
    }

    public static boolean updateTask(Task task) {
        String json = gson.toJson(task);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL + "/tasks/" + task.getTaskId()).put(body).build();
        try (Response response = client.newCall(request).execute()) { return response.isSuccessful(); }
        catch (Exception e) { return false; }
    }

    public static boolean register(String name, String email, String password) {
        String json = String.format("{\"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", name, email, password);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL + "/auth/register").post(body).build();
        try (Response response = client.newCall(request).execute()) { return response.isSuccessful(); }
        catch (Exception e) { return false; }
    }

    public static String getActivityLog() {
        Request request = new Request.Builder().url(BASE_URL + "/activity/all").get().build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) return response.body().string();
        } catch (IOException e) {}
        return "[]";
    }
}