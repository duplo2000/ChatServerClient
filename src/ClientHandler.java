import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Klassen representerar en klienthanterare som hanterar kommunikation med en enskild klient på chattservern.
 */
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter printWriter;
    private String hostAddress;

    private String userName;

    /**
     * Konstruktor för ClientHandler som initierar kommunikation med en klient och lägger till den i listan av hanterare.
     *
     * @param clientSocket Socket som används för kommunikation med klienten.
     */
    public ClientHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            //Denna stream används för att läsa meddelanden
            InputStream input = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            //Denna stream används för att skicka meddelanden
            OutputStream output = clientSocket.getOutputStream();
            printWriter = new PrintWriter(output, true);

            //Ta reda på klientens adress
            hostAddress = clientSocket.getInetAddress().getHostName();

            this.userName = reader.readLine();

            //Lägg till klienten i listan av klienter
            clientHandlers.add(this);

            broadcast(userName + " has entered the chat.");
        } catch (IOException e) {
            closeClientHandler(clientSocket, reader, printWriter);
        }
    }

    /**
     * Run-metoden som körs i tråden för att lyssna på inkommande meddelanden från klienten.
     */
    @Override
    public void run() {
        String msg;

        while (clientSocket.isConnected()) {
            try {
                msg = reader.readLine();
                broadcast(hostAddress + " " + userName + ": " + msg);
            } catch (IOException e) {
                closeClientHandler(clientSocket, reader, printWriter);
                break;
            }
        }
    }

    /**
     * Stänger klienthanteraren genom att stänga dess resurser och ta bort den från listan av hanterare.
     *
     * @param clientSocket Socket som används för kommunikation med klienten.
     * @param reader       BufferedReader för att läsa meddelanden från klienten.
     * @param printWriter  PrintWriter för att skicka meddelanden till klienten.
     */
    private void closeClientHandler(Socket clientSocket, BufferedReader reader, PrintWriter printWriter) {
        removeClientHandler();
        try {
            if (reader != null) {
                reader.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Skickar ett meddelande till alla anslutna klienter utom den aktuella klienten.
     *
     * @param msg Meddelandet som ska skickas.
     */
    private void broadcast(String msg) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                clientHandler.printWriter.println(msg);
                clientHandler.printWriter.flush();
            }
        }
    }

    /**
     * Tar bort klienthanteraren från listan av hanterare och meddelar att klienten har lämnat chatten.
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcast(userName + " has left the chat.");
    }
}
