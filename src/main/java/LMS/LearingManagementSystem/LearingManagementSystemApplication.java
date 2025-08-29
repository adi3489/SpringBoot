package LMS.LearingManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@SpringBootApplication
public class LearingManagementSystemApplication {
	private static List<Users> users = new ArrayList<>();
	private static List<Courses> courses = new ArrayList<>();

	public static void register(Users user) {
		users.add(user);
		System.out.println("Registered: " + user.getName() + " (" + user.getRole() + ")");
	}

	public static void createCourse(Courses course, Users instructor) {
		if (instructor.getRole() != UserRole.INSTRUCTOR) {
			System.out.println("Only instructors can create courses.");
			return;
		}
		courses.add(course);
		System.out.println("Course created: " + course.getTitle());
	}

	public static List<Courses> getUserCourses(Users user) {
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



	public static void main(String[] args) {
		try {
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

						Users currentUser = new Users(name, password, role);
						register(currentUser);

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
		}catch (Exception e){
			System.out.println(e);
		}
	}

	private static void instructorMenu(Scanner scanner, Users instructor) {
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
					Courses newCourse = new Courses(title, description, instructor.getName());
					createCourse(newCourse, instructor);
					break;

				case "2":
					List<Courses> instructorCourses = getUserCourses(instructor);
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

					Lessons lesson = new Lessons(lessonTitle, lessonDesc, videoUrl);
					selectedCourse.addLesson(lesson);
					System.out.println("Lesson added.");
					break;

				case "3":
					System.out.println("\nYour Courses:");
					for (Courses c : getUserCourses(instructor)) {
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

	private static void studentMenu(Scanner scanner, Users student) {
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
					if (courses.isEmpty()) {
						System.out.println("No available courses.");
						break;
					}

					System.out.println("Available Courses:");
					for (int i = 0; i < courses.size(); i++) {
						System.out.println((i + 1) + ". " + courses.get(i).getTitle());
					}

					System.out.print("Select course number to enroll: ");
					int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;

					if (courseIndex < 0 || courseIndex >= courses.size()) {
						System.out.println("Invalid selection.");
						break;
					}

					Courses selected = courses.get(courseIndex);
					if (!selected.getEnrolledStudents().contains(student)) {
						selected.enrollStudent(student);
						System.out.println("Enrolled in: " + selected.getTitle());
					} else {
						System.out.println("Already enrolled.");
					}
					break;

				case "2":
					System.out.println("\nYour Courses:");
					for (Courses c : getUserCourses(student)) {
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



