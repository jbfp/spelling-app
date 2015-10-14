package dk.jbfp.staveapp;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    void addUser(String name, int seed, byte[] photo);
}
