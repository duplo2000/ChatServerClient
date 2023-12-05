import java.io.IOException;
import java.net.*;
/**
 * Klassen representerar en enkel chattserver som hanterar anslutningar från flera klienter.
 */
public class Server {
    private ServerSocket serverSocket;
    /**
     * Konstruktor för servern som tar en ServerSocket som parameter.
     *
     * @param serverSocket ServerSocket som ska användas för att acceptera klientanslutningar.
     */
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    /**
     * Startar servern och väntar på klientanslutningar. För varje anslutning skapas en ny tråd
     * som hanterar kommunikationen med klienten.
     *
     * @throws UnknownHostException Kastas om värden för hosten inte kunde fastställas.
     */
    public void start() throws UnknownHostException {
        System.out.println("Server started on host: " + serverSocket.getInetAddress().getLocalHost().getHostName() + " and port: " + serverSocket.getLocalPort());
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
                System.out.println("Connected users: " + getConnectedUserCount());
            }


        } catch (IOException e) {
            closeSocket();
        }
    }
    /**
     * Stänger servern genom att stänga serverSocket-kopplingen.
     */
    public void closeSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int defaultHost = 0;

        // java Server
        if (args.length == 0) defaultHost = 2000;
        else if (args.length == 1) {
            // java Client <port>
            defaultHost = Integer.parseInt(args[0]);
        }
        ServerSocket serverSocket = new ServerSocket(defaultHost);
        Server server = new Server(serverSocket);
        server.start();
    }
    /**
     * Returnerar antalet anslutna användare till servern.
     *
     * @return Antalet anslutna användare.
     */
    public int getConnectedUserCount() {
        return ClientHandler.clientHandlers.size();
    }


}
