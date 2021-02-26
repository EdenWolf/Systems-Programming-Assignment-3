package bgu.spl.net.impl.BGRSServer;

public class UNREGISTERMessage extends Message {
    private final short courseNumber;
    private final short opcode;

    public UNREGISTERMessage(short _courseNumber) {
        courseNumber = _courseNumber;
        opcode = 10;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        User user = protocol.getUser();
        Database database = Database.getInstance();
        Course course = database.getCourse(courseNumber);

        if (findErrors(user, course)) {
            return new ERRORMessage(opcode);
        }

        unregister(course, user);

        return new ACKMessage(opcode, null);
    }

    // check errors
    private boolean findErrors(User user, Course course) {
        return user == null || user.isAdmin() || !user.isLogin() || !user.isRegistered(courseNumber) || course == null;
    }

    private void unregister(Course course, User user) {
        course.unregister(user.getUsername());
        user.unregister(courseNumber);
    }
}
