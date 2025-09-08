package LMS.LearingManagementSystem;

import java.util.ArrayList;
import java.util.List;

public class Courses {
    private String title;
    private String description;
    private String instructorName;

    private List<Users> enrolledStudents = new ArrayList<>();
    private List<Lessons> lessons = new ArrayList<>();

    public Courses(String title, String description, String instructorName) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
    }

    public void addLesson(Lessons lesson) {
        lessons.add(lesson);
    }

    public void enrollStudent(Users user) {
        if (user.getRole() == UserRole.STUDENT) {
            enrolledStudents.add(user);
        }
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorName() { return instructorName; }
    public List<Users> getEnrolledStudents() { return enrolledStudents; }
    public List<Lessons> getLessons() { return lessons; }
}
