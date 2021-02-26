package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class Course {
    private final short courseNum;
    private final String courseName;
    private final ArrayList<Short> kdamCourses;
    private final int numOfMaxStudents;
    private ArrayList<String> studentsList;
    private int numOfRegStudents;

    public Course(short _courseNum, String _courseName, ArrayList<Short> _kdamCourses, int _numOfMaxStudents) {
        courseNum = _courseNum;
        courseName = _courseName;
        kdamCourses = _kdamCourses;
        numOfMaxStudents = _numOfMaxStudents;
        studentsList = new ArrayList<>();
        numOfRegStudents = 0;
    }

    public int getNumOfAvailableSeats() {
        return numOfMaxStudents - numOfRegStudents;
    }

    public ArrayList<Short> getKdamCourses() {
        return kdamCourses;
    }

    public synchronized void registerStudent(String studentName) {
        studentsList.add(studentName);
        numOfRegStudents++;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public ArrayList<String> getStudentsList() {
        return studentsList;
    }

    public synchronized void unregister(String username) {
        studentsList.remove(username);
        numOfRegStudents--;
    }
}
