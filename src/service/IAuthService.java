package service;

import model.Admin;
import model.User;

import java.util.List;

public interface IAuthService {
    boolean isUsernameTaken(String username);
    Object login(String username, String password);
    User registerNewUser(String username, String password, String firstName, String lastName, String email);
    Admin registerNewAdmin(String username, String password);
    List<User> loadUsers();
    List<Admin> loadAdmins();
}
