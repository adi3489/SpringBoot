package LMS.LearingManagementSystem;


import java.util.ArrayList;
import java.util.List;

public class Courses {
    private String title;
    private String description;
    private String instructorName;

    private List<Users> enrolledStudents;
    private List<Lessons> lessons;

    public Courses(String title, String description, String instructorName) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.enrolledStudents = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lessons lesson) {
        lessons.add(lesson);
    }

    public void enrollStudent(Users user) {
        if (user.getRole() == UserRole.STUDENT) {
            enrolledStudents.add(user);
        }
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorName() { return instructorName; }
    public List<Users> getEnrolledStudents() { return enrolledStudents; }
    public List<Lessons> getLessons() { return lessons; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
}