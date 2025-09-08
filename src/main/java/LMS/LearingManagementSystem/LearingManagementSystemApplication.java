package LMS.LearingManagementSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;

@SpringBootApplication
public class LearingManagementSystemApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private CourseService courseService;

	public static void main(String[] args) {
		SpringApplication.run(LearingManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("=== Welcome to the LMS System ===");

		boolean running = true;

		while (running) {
			System.out.println("\n--- Main Menu ---");
			System.out.println("1. Register as Student/Instructor");
			System.out.println("2. Exit");
			System.out.print("Choose an option: ");
			String mainChoice = scanner.nextLine();

			switch (mainChoice) {
				case "1":
					System.out.print("Enter your name: ");
					String name = scanner.nextLine();

					System.out.print("Enter your password: ");
					String password = scanner.nextLine();

					System.out.print("Enter your role (STUDENT or INSTRUCTOR): ");
					String roleInput = scanner.nextLine().toUpperCase();

					UserRole role;
					if (roleInput.equals("INSTRUCTOR")) {
						role = UserRole.INSTRUCTOR;
					} else if (roleInput.equals("STUDENT")) {
						role = UserRole.STUDENT;
					} else {
						System.out.println("Invalid role. Try again.");
						continue;
					}

					Users currentUser = userService.createUser(name, password, role);

					if (role == UserRole.INSTRUCTOR) {
						instructorMenu(scanner, currentUser);
					} else {
						studentMenu(scanner, currentUser);
					}
					break;

				case "2":
					running = false;
					System.out.println("Exiting the LMS. Goodbye!");
					break;

				default:
					System.out.println("Invalid option. Try again.");
			}
		}
		scanner.close();
	}

	private void instructorMenu(Scanner scanner, Users instructor) {
		boolean back = false;
		while (!back) {
			System.out.println("\n--- Instructor Menu ---");
			System.out.println("1. Create a course");
			System.out.println("2. Add lesson to a course");
			System.out.println("3. View my courses");
			System.out.println("4. Back to main menu");
			System.out.print("Choose an option: ");
			String choice = scanner.nextLine();

			switch (choice) {
				case "1":
					System.out.print("Enter course title: ");
					String title = scanner.nextLine();
					System.out.print("Enter course description: ");
					String description = scanner.nextLine();
					courseService.createCourse(title, description, instructor);
					break;

				case "2":
					List<Courses> instructorCourses = courseService.getUserCourses(instructor);
					if (instructorCourses.isEmpty()) {
						System.out.println("No courses found. Create one first.");
						break;
					}

					System.out.println("Your Courses:");
					for (int i = 0; i < instructorCourses.size(); i++) {
						System.out.println((i + 1) + ". " + instructorCourses.get(i).getTitle());
					}

					System.out.print("Select course number to add lesson: ");
					int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;

					if (courseIndex < 0 || courseIndex >= instructorCourses.size()) {
						System.out.println("Invalid selection.");
						break;
					}

					Courses selectedCourse = instructorCourses.get(courseIndex);
					System.out.print("Enter lesson title: ");
					String lessonTitle = scanner.nextLine();
					System.out.print("Enter lesson description: ");
					String lessonDesc = scanner.nextLine();
					System.out.print("Enter lesson video URL: ");
					String videoUrl = scanner.nextLine();

					courseService.addLessonToCourse(selectedCourse, lessonTitle, lessonDesc, videoUrl);
					System.out.println("Lesson added.");
					break;

				case "3":
					System.out.println("\nYour Courses:");
					for (Courses c : courseService.getUserCourses(instructor)) {
						System.out.println("- " + c.getTitle());
						for (Lessons l : c.getLessons()) {
							System.out.println("  * " + l.getTitle() + " (" + l.getVideoUrl() + ")");
						}
					}
					break;

				case "4":
					back = true;
					break;

				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void studentMenu(Scanner scanner, Users student) {
		boolean back = false;
		while (!back) {
			System.out.println("\n--- Student Menu ---");
			System.out.println("1. Enroll in a course");
			System.out.println("2. View my courses");
			System.out.println("3. Back to main menu");
			System.out.print("Choose an option: ");
			String choice = scanner.nextLine();

			switch (choice) {
				case "1":
					List<Courses> allCourses = courseService.getAllCourses();
					if (allCourses.isEmpty()) {
						System.out.println("No available courses.");
						break;
					}

					System.out.println("Available Courses:");
					for (int i = 0; i < allCourses.size(); i++) {
						System.out.println((i + 1) + ". " + allCourses.get(i).getTitle());
					}

					System.out.print("Select course number to enroll: ");
					int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;

					if (courseIndex < 0 || courseIndex >= allCourses.size()) {
						System.out.println("Invalid selection.");
						break;
					}

					Courses selected = allCourses.get(courseIndex);
					courseService.enrollStudent(selected, student);
					break;

				case "2":
					System.out.println("\nYour Courses:");
					for (Courses c : courseService.getUserCourses(student)) {
						System.out.println("- " + c.getTitle());
						for (Lessons l : c.getLessons()) {
							System.out.println("  * " + l.getTitle() + " (" + l.getVideoUrl() + ")");
						}
					}
					break;

				case "3":
					back = true;
					break;

				default:
					System.out.println("Invalid option.");
			}
		}
	}
}

@Service
class UserService {
	private final List<Users> users = new ArrayList<>();

	public Users createUser(String name, String password, UserRole role) {
		Users user = new Users(name, password, role);
		users.add(user);
		System.out.println("Registered: " + name + " (" + role + ")");
		return user;
	}
}

@Service
class CourseService {
	private final List<Courses> courses = new ArrayList<>();

	public void createCourse(String title, String description, Users instructor) {
		if (instructor.getRole() != UserRole.INSTRUCTOR) {
			System.out.println("Only instructors can create courses.");
			return;
		}
		Courses course = new Courses(title, description, instructor.getName());
		courses.add(course);
		System.out.println("Course created: " + course.getTitle());
	}

	public void addLessonToCourse(Courses course, String title, String description, String videoUrl) {
		Lessons lesson = new Lessons(title, description, videoUrl);
		course.addLesson(lesson);
	}

	public void enrollStudent(Courses course, Users student) {
		if (!course.getEnrolledStudents().contains(student)) {
			course.enrollStudent(student);
			System.out.println("Enrolled in: " + course.getTitle());
		} else {
			System.out.println("Already enrolled.");
		}
	}

	public List<Courses> getUserCourses(Users user) {
		List<Courses> userCourses = new ArrayList<>();
		for (Courses c : courses) {
			if (user.getRole() == UserRole.INSTRUCTOR && c.getInstructorName().equals(user.getName())) {
				userCourses.add(c);
			} else if (user.getRole() == UserRole.STUDENT && c.getEnrolledStudents().contains(user)) {
				userCourses.add(c);
			}
		}
		return userCourses;
	}

	public List<Courses> getAllCourses() {
		return courses;
	}
}
