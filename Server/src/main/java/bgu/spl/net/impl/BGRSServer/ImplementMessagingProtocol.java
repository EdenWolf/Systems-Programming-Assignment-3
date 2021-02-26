package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;

public class ImplementMessagingProtocol implements MessagingProtocol<Message> {
    private User user = null;
    private boolean terminate = false;

    @Override
    public Message process(Message msg) {

        if (msg instanceof ADMINREGMessage) { // opcode: 1
            return ((ADMINREGMessage) msg).process(this);
        }
        else if (msg instanceof STUDENTREGMessage) { // opcode: 2
            return ((STUDENTREGMessage) msg).process(this);
        }
        else if (msg instanceof LOGINMessage) { // opcode: 3
            return ((LOGINMessage) msg).process(this);
        }
        else if (msg instanceof LOGOUTMessage) { // opcode: 4
            return ((LOGOUTMessage) msg).process(this);
        }
        else if (msg instanceof COURSEREGMessage) { // opcode: 5
            return ((COURSEREGMessage) msg).process(this);
        }
        else if (msg instanceof KDAMCHECKMessage) { // opcode: 6
            return ((KDAMCHECKMessage) msg).process(this);
        }
        else if (msg instanceof COURSESTATMessage) { // opcode: 7
            return ((COURSESTATMessage) msg).process(this);
        }
        else if (msg instanceof STUDENTSTATMessage) { // opcode: 8
            return ((STUDENTSTATMessage) msg).process(this);
        }
        else if (msg instanceof ISREGISTEREDMessage) { // opcode: 9
            return ((ISREGISTEREDMessage) msg).process(this);
        }
        else if (msg instanceof UNREGISTERMessage) { // opcode: 10
            return ((UNREGISTERMessage) msg).process(this);
        }
        else if (msg instanceof MYCOURSESMessage) { // opcode: 11
            return ((MYCOURSESMessage) msg).process(this);
        }


        return null;
    }

    @Override
    public boolean shouldTerminate() { return terminate; }

    public void setUser(User user) { this.user = user; }

    public void setTerminate() { terminate = true; }

    public User getUser() { return user; }
}
