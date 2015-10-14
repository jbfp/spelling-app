package dk.jbfp.staveapp;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final ArrayList<User> users;

    static {
        users = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.id = i;
            user.name = "User " + i;
            users.add(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public void addUser(String name, int seed, byte[] photo) {
        User user = new User();
        user.id = users.size() + 1;
        user.name = name;
        user.seed = seed;
        user.photo = photo;
        users.add(user);
    }
}
