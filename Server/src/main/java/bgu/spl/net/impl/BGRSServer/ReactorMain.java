package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {
        Database database = Database.getInstance();

        Server.reactor(Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                ImplementMessagingProtocol::new,
                MessageEncDec::new
        ).serve();
    }
}
