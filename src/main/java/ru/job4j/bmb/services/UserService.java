package ru.job4j.bmb.services;

import ru.job4j.bmb.model.User;

public class UserService {
    public static void saveUser(User user) throws Exception {
        if (user == null) {
            throw new Exception("User cannot be null");
        }
    }

    public static void main(String[] args) throws Exception {
        saveUser(null);
    }
}
