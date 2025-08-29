package LMS.LearingManagementSystem;

public class Users {
    private String name;
    private String password;
    private UserRole role;

    public Users(String name, String password, UserRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getName() { return name; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(UserRole role) { this.role = role; }
}
enum UserRole {
    STUDENT,
    INSTRUCTOR
}