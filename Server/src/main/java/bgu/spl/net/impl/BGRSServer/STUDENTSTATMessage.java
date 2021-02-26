package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class STUDENTSTATMessage extends Message {
    private final String username;
    private final short opcode;

    public STUDENTSTATMessage(String _username) {
        username = _username;
        opcode = 8;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        Database database = Database.getInstance();
        User user = protocol.getUser();
        User student = database.getUser(username);

        if (findErrors(user, student)) {
            return new ERRORMessage(opcode);
        }

        return new ACKMessage(opcode, getStringStudentStat(student, database));
    }

    // check errors
    private boolean findErrors(User user, User student) {
        return student == null || user == null || !user.isLogin() || !user.isAdmin() || student.isAdmin();
    }

    private String getStringStudentStat(User user, Database database) {
        return "Student: " + username + "\n" +
                "Courses: [" + getCoursesString(user,database) + "]";
    }

    private String getCoursesString(User user, Database database) {
        ArrayList<Short> studentCoursesArray = new ArrayList<>();
        ArrayList<Short> coursesList = database.getCoursesList();
        ArrayList<Short> userCourses = user.getCourses();

        for (Short courseNum : coursesList) {
            if (userCourses.contains(courseNum)) {
                studentCoursesArray.add(courseNum);
            }
        }

        StringBuilder output = new StringBuilder();
        int size = studentCoursesArray.size();

        for (int ind = 0; ind < size; ind++){
            output.append(studentCoursesArray.get(ind));
            if (ind < size - 1) {
                output.append(",");
            }
        }

        return output.toString();
    }
}
