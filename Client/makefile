CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
# LDFLAGS:=-lWs2_32
LDFLAGS:=-lboost_system -pthread

all: BGRSclient
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/Decoder.o bin/Client.o bin/Encoder.o $(LDFLAGS)

BGRSclient: bin/connectionHandler.o bin/Decoder.o bin/Client.o bin/Encoder.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/Decoder.o: src/Decoder.cpp
	g++ $(CFLAGS) -o bin/Decoder.o src/Decoder.cpp

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp

bin/Encoder.o: src/Encoder.cpp
	g++ $(CFLAGS) -o bin/Encoder.o src/Encoder.cpp

.PHONY: clean
clean:
	rm -f bin/*
