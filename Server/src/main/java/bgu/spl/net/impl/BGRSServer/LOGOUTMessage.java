package bgu.spl.net.impl.BGRSServer;

public class LOGOUTMessage extends Message {
    private final short opcode;

    public LOGOUTMessage() {
        opcode = 4;
    }

    public synchronized Message process(ImplementMessagingProtocol protocol) {
        if (findErrors(protocol)) {
            return new ERRORMessage(opcode);
        }

        protocol.getUser().setLogin(false);
        protocol.setTerminate();
        return new ACKMessage(opcode, null);
    }

    // check errors
    private boolean findErrors(ImplementMessagingProtocol protocol) {
        User user = protocol.getUser();

        return user == null || !user.isLogin() ;
    }
}
