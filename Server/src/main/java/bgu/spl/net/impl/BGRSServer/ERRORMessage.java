package bgu.spl.net.impl.BGRSServer;

public class ERRORMessage extends Message {
    private final Short messageOpcode;

    public ERRORMessage(Short _messageOpcode) {
        messageOpcode = _messageOpcode;
    }

    public Short getMessageOpcode() {
        return messageOpcode;
    }
}
