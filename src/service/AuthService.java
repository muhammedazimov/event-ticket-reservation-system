package service;
import model.Admin;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService implements IAuthService{
    private static final String USERS_FILE = "data/users.txt";
    private static final String ADMINS_FILE = "data/admins.txt";
    private int userIdCounter = 1;
    private int adminIdCounter = 1;

    static {
        new File("data").mkdirs();
    }

    public boolean isUsernameTaken(String username) {
        return existsInFile(username, USERS_FILE) || existsInFile(username, ADMINS_FILE);
    }

    public Object login(String username, String password) {
        if (existsInFile(username, password, ADMINS_FILE)) {
            return getAdminByUsername(username);
        } else if (existsInFile(username, password, USERS_FILE)) {
            return getUserByUsername(username);
        } else {
            return null;
        }
    }

    public User registerNewUser(String username, String password, String firstName, String lastName, String email) {
        loadUsers();
        int id = userIdCounter++;
        try {
            boolean isNewFile = new File(USERS_FILE).length() == 0;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                if (isNewFile) {
                    writer.write("id|username|password|firstName|lastName|email");
                    writer.newLine();
                }
                writer.write(id + "|" + username + "|" + password + "|" + firstName + "|" + lastName + "|" + email);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new User(id, username, firstName, lastName, email);
    }

    public Admin registerNewAdmin(String username, String password) {
        loadAdmins();
        int id = adminIdCounter++;
        try {
            boolean isNewFile = new File(ADMINS_FILE).length() == 0;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMINS_FILE, true))) {
                if (isNewFile) {
                    writer.write("id|username|password");
                    writer.newLine();
                }
                writer.write(id + "|" + username + "|" + password);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Admin(id, username);
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String firstName = parts[3];
                    String lastName = parts[4];
                    String email = parts[5];
                    users.add(new User(id, username, firstName, lastName, email));
                    userIdCounter = Math.max(userIdCounter, id + 1);
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return users;
    }

    public List<Admin> loadAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINS_FILE))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    admins.add(new Admin(id, username));
                    adminIdCounter = Math.max(adminIdCounter, id + 1);
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return admins;
    }

    private boolean existsInFile(String username, String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    private boolean existsInFile(String username, String password, String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 3 && parts[1].equals(username) && parts[2].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    private User getUserByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[1].equals(username)) {
                    return new User(Integer.parseInt(parts[0]), parts[1], parts[3], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    private Admin getAdminByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINS_FILE))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 3 && parts[1].equals(username)) {
                    return new Admin(Integer.parseInt(parts[0]), parts[1]);
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return null;
    }
}
