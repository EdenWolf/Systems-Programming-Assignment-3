package bgu.spl.net.impl.BGRSServer;

public class ACKMessage extends Message {
    private Short messageOpcode;
    private String optional;

    public ACKMessage(Short _messageOpcode, String _optional) {
        messageOpcode = _messageOpcode;
        optional = _optional;
    }

    public Short getMessageOpcode() {
        return messageOpcode;
    }

    public String getOptional() {
        return optional;
    }
}
