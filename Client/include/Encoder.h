#include "connectionHandler.h"

#ifndef BOOST_ECHO_CLIENT_ENCODER_H
#define BOOST_ECHO_CLIENT_ENCODER_H

using namespace std;

class Encoder {
public:
    Encoder(ConnectionHandler *connectionHandler, bool *toTerminate);
    void run();

private:
    void shortToBytes(short num, char *bytesArr);
    void encodeUserAndPassword(ConnectionHandler *connectionHandler, short opcode, vector<string> inputVector);
    void encodeOpcode(ConnectionHandler *connectionHandler, short opcode);
    void encodeCourse(ConnectionHandler *connectionHandler, short opcode, string courseNumber);
    void encodeSTUDENTSTAT(ConnectionHandler *connectionHandler, string username);

    ConnectionHandler *connectionHandler;
    bool *toTerminate;
};


#endif //BOOST_ECHO_CLIENT_ENCODER_H
