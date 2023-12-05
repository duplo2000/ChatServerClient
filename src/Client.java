import java.net.*;
import java.io.*;

/**
 * Denna klass representerar en klient som kopplar upp sig mot en chattserver.
 */
public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    /**
     * Konstruktor som kopplar upp klienten mot servern genom att skapa en socket-koppling
     * till den angivna host-adressen och portnumret.
     *
     * @param host Värden för serverns host-adress.
     * @param port Värden för serverns portnummer.
     * @throws IOException Kastas om det uppstår något fel i samband med uppkopplingen.
     */
    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"), true);
    }

    /**
     * Startar klienten genom att skapa trådar för att läsa från servern och skriva till servern.
     */
    public void start() {
        Thread reader = new Thread(new Readthread());
        Thread sender = new Thread(new Writethread());

        reader.start();
        sender.start();
    }


    public static void main(String[] args) {
        String defaultHost = "127.0.0.1";
        int defaultPort = 2000;

        if (args.length == 0) {
            // java Client
            // Default host and port
        } else if (args.length == 1) {
            // java Client <host> (port default)
            defaultHost = args[0];
        } else if (args.length == 2) {
            // java Client <host> <port>
            defaultHost = args[0];
            defaultPort = Integer.parseInt(args[1]);
        } else {
            System.out.println("Usage: java Client <host> <port>");
            return;
        }

        try {
            Client client = new Client(defaultHost, defaultPort);
            System.out.println("Connected to server " + defaultHost + " at port: " + defaultPort);
            System.out.print("Enter username: ");
            client.start();
        } catch (IOException e) {
            System.err.println("Error: not connecting to the server.");
            e.printStackTrace();
        }
    }

    /**
     * En privat inre klass som representerar en tråd som ständigt lyssnar efter meddelanden från servern.
     */
    private class Readthread implements Runnable {

        @Override
        public void run() {
            String message;
            while (socket.isConnected()) {
                try {
                    message = in.readLine();
                    System.out.println(message);

                } catch (IOException e) {
                    closeClient(socket, in, out);
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * En privat inre klass som representerar en tråd som ständigt lyssnar efter input från användaren
     * och skickar meddelanden till servern.
     */
    private class Writethread implements Runnable {
        @Override
        public void run() {
            try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
                while (socket.isConnected()) {
                    String message = consoleReader.readLine();
                    out.println(message);
                }
            } catch (IOException e) {
                closeClient(socket, in, out);
                e.printStackTrace();
            }
        }
    }
    /**
     * Stänger klienten genom att stänga socket-kopplingen och de nödvändiga in- och utströmmarna.
     *
     * @param socket Socket-kopplingen att stänga.
     * @param in     BufferedReader att stänga.
     * @param out    PrintWriter att stänga.
     */
    private void closeClient(Socket socket, BufferedReader in, PrintWriter out) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
