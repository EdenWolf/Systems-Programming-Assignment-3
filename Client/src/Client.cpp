#include <Encoder.h>
#include <Decoder.h>
#include "../include/Client.h"
#include "../include/connectionHandler.h"
#include <thread>

using namespace std;

int main(int argc, char * argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    bool *toTerminate = new bool (false);
    Encoder encoder = Encoder(&connectionHandler, toTerminate);
    thread Encoder(&Encoder::run, &encoder);
    Decoder decoder(&connectionHandler, toTerminate);
    decoder.run();
    Encoder.join();
    delete toTerminate;
    return 0;
}
