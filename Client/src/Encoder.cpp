#include <boost/algorithm/string.hpp>
#include "boost/lexical_cast.hpp"
#include "../include/Encoder.h"

using namespace std;

Encoder::Encoder(ConnectionHandler *connectionHandler, bool *toTerminate) : connectionHandler(connectionHandler), toTerminate(toTerminate) {}

void Encoder::run() {
    while (!(*toTerminate)) {
        char buffer[1024];
        cin.getline(buffer, 1024);
        string stringInput(buffer);
        vector<string> inputVector;
        boost::split(inputVector, stringInput, boost::is_any_of(" "));

        if (inputVector[0] == "ADMINREG") {
            encodeUserAndPassword(connectionHandler, 1, inputVector);
        }
        else if (inputVector[0] == "STUDENTREG") {
            encodeUserAndPassword(connectionHandler, 2, inputVector);
        }
        else if (inputVector[0] == "LOGIN") {
            encodeUserAndPassword(connectionHandler, 3, inputVector);
        }
        else if (inputVector[0] == "LOGOUT") {
            encodeOpcode(connectionHandler, 4);
            break;
        }
        else if (inputVector[0] == "COURSEREG") {
            encodeCourse(connectionHandler, 5, inputVector[1]);
        }
        else if (inputVector[0] == "KDAMCHECK") {
            encodeCourse(connectionHandler, 6, inputVector[1]);
        }
        else if (inputVector[0] == "COURSESTAT") {
            encodeCourse(connectionHandler, 7, inputVector[1]);
        }
        else if (inputVector[0] == "STUDENTSTAT") {
            encodeSTUDENTSTAT(connectionHandler, inputVector[1]);
        }
        else if (inputVector[0] == "ISREGISTERED") {
            encodeCourse(connectionHandler, 9, inputVector[1]);
        }
        else if (inputVector[0] == "UNREGISTER") {
            encodeCourse(connectionHandler, 10, inputVector[1]);
        }
        else { // inputVector[0] == "MYCOURSES"
            encodeOpcode(connectionHandler, 11);
        }
    }
}

void Encoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

// send input for messages: 1, 2, 3
void Encoder::encodeUserAndPassword(ConnectionHandler *connectionHandler, short opcode, vector<string> inputVector) {
    char opcodeChar[2];
    shortToBytes(opcode, opcodeChar);
    connectionHandler -> sendBytes(opcodeChar, 2);
    connectionHandler ->sendLine(inputVector[1]);
    connectionHandler ->sendLine(inputVector[2]);
}

// send input for messages: 4, 11
void Encoder::encodeOpcode(ConnectionHandler *connectionHandler, short opcode) {
    char opcodeChar[2];
    shortToBytes(opcode, opcodeChar);
    connectionHandler -> sendBytes(opcodeChar, 2);
}

// send input for messages: 5, 6, 7, 9, 10
void Encoder::encodeCourse(ConnectionHandler *connectionHandler, short opcode, string courseNumber) {
    char chars[2];
    shortToBytes(opcode, chars);
    connectionHandler -> sendBytes(chars, 2);
    short shortCourseNumber = boost::lexical_cast<short>(courseNumber);
    shortToBytes(shortCourseNumber, chars);
    connectionHandler -> sendBytes(chars, 2);
}

// send input for message 8
void Encoder::encodeSTUDENTSTAT(ConnectionHandler *connectionHandler, string username) {
    char opcodeChar[2];
    shortToBytes(8, opcodeChar);
    connectionHandler -> sendBytes(opcodeChar, 2);
    connectionHandler -> sendLine(username);
}