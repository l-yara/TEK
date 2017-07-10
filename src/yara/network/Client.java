package yara.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * A "client" part for sending - receiving
 */
public class Client<T extends Serializable> implements AutoCloseable {
    private static Logger logger = Logger.getLogger(Client.class.getName());

    private final String label;
    private final Socket clientSocket;
    private final ObjectOutputStream outputStream;

    public Client(int port, String label) throws IOException {
        logger.info("Creating " + label + " client on " + port);
        this.label = label;
        try {
            clientSocket = new Socket(InetAddress.getLocalHost(), port);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.warning("problem creating client " + label + ":" + e);
            close();
            throw e;
        }
        logger.info("Client on " + label + " created");

    }

    //do synchronize as needed as this method is NOT thread-safe!
    public void send(T value) {
        try {
            outputStream.writeObject(value);
        } catch (Exception e) {
            logger.warning("Client on " + label + " : problem sending " + value + ":" + e);
        }
    }

    @Override
    public void close() {
        logger.info("closing " + label + " client");
        NetworkUtil.closeQuiet(outputStream, clientSocket);
    }
}
