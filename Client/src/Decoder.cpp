#include <boost/algorithm/string.hpp>
#include "boost/lexical_cast.hpp"
#include "../include/Decoder.h"
using namespace std;

Decoder::Decoder(ConnectionHandler *connectionHandler, bool *toTerminate) : connectionHandler(connectionHandler), toTerminate(toTerminate) {}

void Decoder::run() {
    short opcode = -1;
    short messageOpcode = -1;
    string coursesListString;
    string messageString;
    char *buffer = new char[1]; // buffer for opcode TODO: delete new
    while (!(*toTerminate)) {
        connectionHandler -> getBytes(buffer, 1); // get the opcode

        if (buffer[0] == '|' || buffer[0] == '\0') {
            if (opcode == -1) {
                opcode = boost::lexical_cast<short>(messageString);
                messageString = "";
            }
            else if (messageOpcode == -1) {
                messageOpcode = boost::lexical_cast<short>(messageString);
                messageString = "";
            }
            else {
                if (opcode == 12) { // ACK message
                    if (messageOpcode == 1 || messageOpcode == 2 || messageOpcode == 3 || messageOpcode == 4 || messageOpcode == 5 || messageOpcode == 10) {
                        standardACKOutput(messageOpcode);
                        if (messageOpcode == 4) { // LOGOUT
                            *toTerminate = true;
                        }
                        opcode = -1;
                        messageOpcode = -1;
                        messageString = "";
                    }
                    else { //if (messageOpcode == 6 || messageOpcode == 7 || messageOpcode == 8 || messageOpcode == 9 || messageOpcode == 11)
                        if (!coursesListString.empty()) {
                            listOutput(coursesListString, messageOpcode);
                            opcode = -1;
                            messageOpcode = -1;
                            messageString = "";
                            coursesListString = "";
                        }
                        coursesListString = messageString;

                    }
                }
                else { // ERROR message
                    ERROROutput(messageOpcode);
                    opcode = -1;
                    messageOpcode = -1;
                    messageString = "";
                }
            }
        }
        else if (buffer[0] != '\0') {
            messageString.push_back(buffer[0]);
        }
    }
    delete[] buffer;
}

short Decoder::bytesToShort(char *bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result = result + (short)(bytesArr[1] & 0xff);
    return result;
}

// print output for messages: 1, 2, 3, 4, 5, 10
void Decoder::standardACKOutput(short messageOpcode) {
    cout << "ACK " << messageOpcode << endl;
}

// print output for messages: 6, 11, 7, 8, 9
void Decoder::listOutput(string message, short messageOpcode) {
    cout << "ACK " << messageOpcode << "\n" << message << endl;
}

// print ERROR message
void Decoder::ERROROutput(short messageOpcode) {
    cout << "ERROR " << messageOpcode << endl;
}