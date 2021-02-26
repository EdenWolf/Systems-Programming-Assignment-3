package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class KDAMCHECKMessage extends Message {
    private final short courseNumber;
    private final short opcode;

    public KDAMCHECKMessage(short _courseNumber) {
        courseNumber = _courseNumber;
        opcode = 6;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        Database database = Database.getInstance();
        Course course = database.getCourse(courseNumber);
        User user = protocol.getUser();

        if (findErrors(protocol, course, user)) {
            return new ERRORMessage(opcode);
        }

        return new ACKMessage(opcode, getStringKdamCoursesList(course));
    }

    // check errors
    private boolean findErrors(ImplementMessagingProtocol protocol, Course course, User user) {
        return course == null || user == null || user.isAdmin() || !user.isLogin();
    }

    // return the list of kdam courses as a String
    private String getStringKdamCoursesList(Course course) {
        ArrayList<Short> kdamCoursesArray = course.getKdamCourses();

        StringBuilder coursesString = new StringBuilder("[");
        int arraySize = kdamCoursesArray.size();

        for (int ind = 0; ind < arraySize; ind++) {
            coursesString.append(kdamCoursesArray.get(ind));
            if (ind < arraySize - 1) {
                coursesString.append(",");
            }
        }

        coursesString.append("]");
        return coursesString.toString();
    }
}
