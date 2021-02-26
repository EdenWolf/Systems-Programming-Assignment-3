package bgu.spl.net.impl.BGRSServer;

public class LOGINMessage extends Message {
    private final String username;
    private final String password;
    private final short opcode;

    public LOGINMessage(String _username, String _password) {
        username = _username;
        password = _password;
        opcode = 3;
    }

    public synchronized Message process(ImplementMessagingProtocol protocol) {
        Database database = Database.getInstance();
        User user = database.getUser(username);

        if (findErrors(user)) {
            return new ERRORMessage(opcode);
        }

        if (protocol.getUser() == null || !protocol.getUser().compare(user)) {
            protocol.setUser(user);
        }

        user.setLogin(true);
        return new ACKMessage(opcode, null);
    }

    private boolean findErrors(User user) {
        return  user == null || user.isLogin() || !user.getPassword().equals(password);
    }
}