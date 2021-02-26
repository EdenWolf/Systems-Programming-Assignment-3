package bgu.spl.net.impl.BGRSServer;

public class STUDENTREGMessage extends Message {
    private final User user;
    private final short opcode;

    public STUDENTREGMessage(String _username, String _password) {
        user = new User(_username, _password, false);
        opcode = 2;
    }

    public synchronized Message process(ImplementMessagingProtocol protocol) {
        Database database = Database.getInstance();
        boolean isSuccessful = false;

        if (!findErrors(protocol.getUser())) {
            isSuccessful = database.addUser(user);
        }

        if (isSuccessful) {
            protocol.setUser(user);
            return new ACKMessage(opcode, null);
        }
        return new ERRORMessage(opcode);
    }

    // check errors
    private boolean findErrors(User user) {
        return user != null && user.isLogin();
    }
}
