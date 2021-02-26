package bgu.spl.net.impl.BGRSServer;

public class ISREGISTEREDMessage extends Message {
    private final short courseNumber;
    private final short opcode = 9;

    public ISREGISTEREDMessage(short _courseNumber) {
        courseNumber = _courseNumber;
    }

    public Message process(ImplementMessagingProtocol protocol) {
        User user = protocol.getUser();

        if (findErrors(user)) {
            return new ERRORMessage(opcode);
        }

        return new ACKMessage(opcode, isRegistered(user));
    }

    // check errors
    private boolean findErrors(User user) {
        Database database = Database.getInstance();

        return user == null || !user.isLogin() || user.isAdmin() || database.getCourse(courseNumber) == null;
    }

    private String isRegistered(User user) {
        if (user.isRegistered(courseNumber)) {
            return "REGISTERED";
        }
        return "NOT REGISTERED";
    }
}
