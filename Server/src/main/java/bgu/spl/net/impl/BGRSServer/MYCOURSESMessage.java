package bgu.spl.net.impl.BGRSServer;

import java.util.ArrayList;

public class MYCOURSESMessage extends Message {
    private final short opcode;

    public MYCOURSESMessage() {
        opcode = 11;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        Database database = Database.getInstance();
        User user = protocol.getUser();

        if (findErrors(user)) {
            return new ERRORMessage(opcode);
        }

        return new ACKMessage(opcode, getCoursesString(user, database));
    }

    // check errors
    private boolean findErrors(User user) {
        return user == null || user.isAdmin() || !user.isLogin();
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

        return "[" + output + "]";
    }
}
