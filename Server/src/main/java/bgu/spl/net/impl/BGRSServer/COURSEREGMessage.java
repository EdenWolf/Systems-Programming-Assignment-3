package bgu.spl.net.impl.BGRSServer;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

public class COURSEREGMessage extends Message{
    private short courseNumber;
    private final short opcode = 5;

    public COURSEREGMessage(short _courseNumber) {
        courseNumber = _courseNumber;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        User user = protocol.getUser();
        Database database = Database.getInstance();
        Course course = database.getCourse(courseNumber);

        if (findErrors(protocol, user, course)) {
            return new ERRORMessage(opcode);
        }

        // Register to the course
        user.registerToCourse(courseNumber);
        course.registerStudent(user.getUsername());

        return new ACKMessage(opcode, null);
    }

    // check errors
    private boolean findErrors(ImplementMessagingProtocol protocol, User user, Course course) {
        if (user == null || !user.isLogin() || user.isAdmin() || course == null || course.getNumOfAvailableSeats() <= 0 || !checkKdamCourses(course.getKdamCourses(), user.getCourses()) || user.isRegistered(courseNumber)) {
            return true;
        }

        return false;
    }

    // return true if ths student have all the kdam courses
    private Boolean checkKdamCourses(ArrayList<Short> kdamCourses, ArrayList<Short> studentKdamCourses) {
        for (short courseNum : kdamCourses) {
            if (!studentKdamCourses.contains(courseNum)) {
                return false;
            }
        }

        return true;
    }
}
