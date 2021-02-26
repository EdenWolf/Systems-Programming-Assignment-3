package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncDec implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(2);
    short opcode = -1;
    String userName = "";
    String password = "";
    short courseNumber = -1;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison

        if (opcode == -1) {
            byteBuffer.put(nextByte);
            if (!byteBuffer.hasRemaining()) {
                byteBuffer.flip();
                opcode = byteBuffer.getShort();
                byteBuffer.clear();
                if (opcode == 4 || opcode == 11) {
                    return createMessage();
                }
            }
        }
        else {
            if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 9 || opcode == 10) {
                if (courseNumber == -1) {
                    byteBuffer.put(nextByte);
                    if (!byteBuffer.hasRemaining()) {
                        byteBuffer.flip();
                        courseNumber = byteBuffer.getShort();
                        byteBuffer.clear();
                        return createMessage();
                    }
                }
                else {
                    courseNumber = getShortMessage();
                    return createMessage();
                }
            }
            else if (nextByte == '\n') {
                if (opcode == 1 || opcode == 2 || opcode == 3) {
                    if (userName.length() == 0) {
                        userName = getStringMessage();
                    }
                    else {
                        password = getStringMessage();
                        return createMessage();
                    }
                }
                else { // if (opcode == 8)
                    userName = getStringMessage();
                    return createMessage();
                }
            }

            if (nextByte != '\n') {
                pushByte(nextByte);
            }
        }

        return null; //not a line yet
    }

    @Override
    public byte[] encode(Message message) {
        if (message instanceof ERRORMessage) {
            return encodeERRORMessage((ERRORMessage) message);
        }
        return encodeACKMessage((ACKMessage) message);
    }

    private byte[] encodeERRORMessage(ERRORMessage message) {
        String outputString = "13|" + message.getMessageOpcode() + "|\0";
        return outputString.getBytes();
    }

    private byte[] encodeACKMessage(ACKMessage message) {
        String outputString = "12|" + message.getMessageOpcode() + "|";
        if (message.getOptional() != null) {
            outputString += message.getOptional() + "|";
        }
        outputString += "\0";
        return outputString.getBytes();
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private short getOpcode(String messageString) {
        char[] charArr = messageString.toCharArray();
        short opcode = (short)charArr[0];

        if (charArr[1] != '|') {
            opcode *= 10;
            opcode += (short)charArr[1];
        }

        return opcode;
    }

    // create and return new ADMINREGMessage object
    private ADMINREGMessage createADMINREG(String messageString) {
        int i = 0;
        char[] messageCharArray = messageString.toCharArray();

        // get username
        String username = getNextString(messageCharArray, i);
        i += username.length() + 1;

        // get password
        String password = getNextString(messageCharArray, i);

        return new ADMINREGMessage(username, password);
    }

    private Message createMessage() {
        Message newMessage;
        switch (opcode) {
            case 1:
                newMessage = new ADMINREGMessage(userName, password);
                end();
                return newMessage;
            case 2:
                newMessage = new STUDENTREGMessage(userName, password);
                end();
                return newMessage;
            case 3:
                newMessage = new LOGINMessage(userName, password);
                end();
                return newMessage;
            case 4:
                end();
                return new LOGOUTMessage();
            case 5:
                newMessage = new COURSEREGMessage(courseNumber);
                end();
                return newMessage;
            case 6:
                newMessage = new KDAMCHECKMessage(courseNumber);
                end();
                return newMessage;
            case 7:
                newMessage = new COURSESTATMessage(courseNumber);
                end();
                return newMessage;
            case 8:
                newMessage = new STUDENTSTATMessage(userName);
                end();
                return newMessage;
            case 9:
                newMessage = new ISREGISTEREDMessage(courseNumber);
                end();
                return newMessage;
            case 10:
                newMessage =  new UNREGISTERMessage(courseNumber);
                end();
                return newMessage;
            case 11:
                end();
                return new MYCOURSESMessage();
        }
        end();
        return null;
    }

    // return short number from startInd to endInd
    private short getShortNumber(String messageString, int startInd, int endInd) {
        return Short.parseShort(messageString.substring(startInd, endInd));
    }

    // return string of all the chars from ind to the next '|' char
    private String getNextString(char[] messageCharArray, int ind) {
        StringBuilder output = new StringBuilder();
        while (ind < messageCharArray.length && messageCharArray[ind] != '|') {
            output.append(messageCharArray[ind]);
            ind++;
        }
        return output.toString();
    }

    // get the string part of the message
    private String getStringMessage() {
        return  popString();
    }

    // get the short part of the message
    private short getShortMessage() {
        return Short.parseShort(popString());
    }

    private void end() {
        len = 0;
        opcode = -1;
        userName = "";
        password = "";
        courseNumber = -1;
        byteBuffer.clear();
    }
}
