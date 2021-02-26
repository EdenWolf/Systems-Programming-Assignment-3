//
// Created by spl211 on 10/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_DECODER_H
#define BOOST_ECHO_CLIENT_DECODER_H

#include "connectionHandler.h"

using namespace std;

class Decoder {
public:
    Decoder(ConnectionHandler *connectionHandler, bool *toTerminate);
    void run();

private:
    short bytesToShort(char *opcodeCharArray);
    void standardACKOutput(short messageOpcode);
    void listOutput(string message, short messageOpcode);
    void ERROROutput(short messageOpcode);

    ConnectionHandler *connectionHandler;
    bool *toTerminate;


};


#endif //BOOST_ECHO_CLIENT_DECODER_H
