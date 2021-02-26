package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class COURSESTATMessage extends Message {
    private final short courseNumber;
    private final short opcode = 7;

    public COURSESTATMessage(short _courseNumber) {
        courseNumber = _courseNumber;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        Database database = Database.getInstance();
        Course course = database.getCourse(courseNumber);

        if (findErrors(protocol, course)) {
            return new ERRORMessage(opcode);
        }

        return new ACKMessage(opcode, getStringCourseStat(course));
    }

    // check errors
    private boolean findErrors(ImplementMessagingProtocol protocol, Course course) {
        User user = protocol.getUser();

        return user == null || !user.isLogin() || !user.isAdmin() || course == null;
    }

    private String getStringCourseStat(Course course){
        return "Course: (" + courseNumber + ") " + course.getCourseName() + "\n" +
                "Seats Available: " + course.getNumOfAvailableSeats() + " / " + course.getNumOfMaxStudents() + "\n" +
                "Students Registered: [" + arrayListToSortedString(course.getStudentsList()) + "]";
    }

    private String arrayListToSortedString(ArrayList<String> studentsList) {
        StringBuilder str = new StringBuilder();
        studentsList.sort(String::compareTo);
        int listSize = studentsList.size();

        for (int ind = 0; ind < listSize; ind++) {
            str.append(studentsList.get(ind));
            if (ind < listSize - 1) {
                str.append(",");
            }
        }

        return str.toString();
    }

}
