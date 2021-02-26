package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args){
        Database database = Database.getInstance();

        Server.threadPerClient(
                Integer.decode(args[0]),
                () -> new ImplementMessagingProtocol(),
                () -> new MessageEncDec()
        ).serve();
    }
}
