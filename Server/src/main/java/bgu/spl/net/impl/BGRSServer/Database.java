package bgu.spl.net.impl.BGRSServer;


import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ConcurrentHashMap<String, User> users;
	private ConcurrentHashMap<Short, Course> courses;
	private ArrayList<Short> coursesList;

	//to prevent user from creating new Database
	private Database() {
		users = new ConcurrentHashMap<>();
		courses = new ConcurrentHashMap<>();
		coursesList = new ArrayList<>();
		initialize("./Courses.txt");
	}

	// add new user to the users ConcurrentHashMap
	public synchronized boolean addUser(User newUser) {
		User oldUser = users.get(newUser.getUsername());

		if (oldUser != null) {
			return false;
		}
		users.put(newUser.getUsername(), newUser);
		return true;
	}

	public User getUser(String username) {
		return users.get(username);
	}

	public Course getCourse(short courseNumber) { return courses.get(courseNumber); }

	public ArrayList<Short> getCoursesList() {
		return coursesList;
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		if (Singleton.database == null) {
			Singleton.database = new Database();
		}

		return Singleton.database;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		boolean isSuccessful = true;
		try {
			ArrayList<String> coursesString = (ArrayList<String>) Files.readAllLines(Paths.get(coursesFilePath));
			for (String courseLine : coursesString) {

				String[] line = courseLine.split("\\|");

				short courseNum = Short.parseShort(line[0]);

				String courseName = line[1];
				coursesList.add(courseNum);

				// get kdam courses
				String kdamCoursesStringList;
				ArrayList<Short> kdamCourses = new ArrayList<>();
				if (line[2].length() > 2) {
					kdamCoursesStringList = line[2].substring(1, line[2].length()-1);
					String[] kdamCoursesStringArray = kdamCoursesStringList.split(",");

					for (String kdamCourseString : kdamCoursesStringArray) {
						kdamCourses.add(Short.parseShort(kdamCourseString));
					}
				}



				int numOfMaxStudents = Integer.parseInt(line[3]);

				courses.put(courseNum, new Course(courseNum, courseName, kdamCourses, numOfMaxStudents));
			}
		} catch (IOException ex) { isSuccessful = false; }
		return isSuccessful;
	}

	private static class Singleton {
		private static Database database;
	}

}
