# 

# Java Chat Client/Server Program

This repository contains a simple Java-based chat client/server program that allows multiple clients to connect to a server and communicate in a chatroom-like environment.

### Contents

- [ClientHandler.java]: Represents a handler for client communication on the server side.
- [Server.java]: Implements a simple chat server that manages client connections.
- [Client.java]: Represents a client connecting to the chat server.

### Usage

### Server

1. **Compile Server:** Compile the `Server.java` file.
- `javac Server.java`
- **Run Server:** Start the server.
1. `java Server [port]`

### Client

1. **Compile Client:** Compile the `Client.java` file.
- `javac Client.java`
- **Run Client:** Start a client to connect to the server.
1. `java Client [host] [port]`

### Features

- **ClientHandler:** Manages communication with individual clients on the server.
- **Server:** Handles multiple client connections and relays messages between them.
- **Client:** Connects to the server to participate in the chatroom.

### How to Use

- Start the server on a designated port.
- Run the client and specify the server's host and port.
- Enter a username when prompted to start chatting.

### Author

Created by : Erik Örtenholm

Last update: 10/10-2023
