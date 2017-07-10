package yara.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * A server which processes int stream
 */
public class Server<T extends Serializable> implements AutoCloseable {
    private static Logger logger = Logger.getLogger(Server.class.getName());

    private final String label;
    private final Class<T> clazz;


    private final ServerSocket serverSocket;
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;

    public Server(int port, String label, Class<T> clazz) throws Exception {
        logger.info("Creating the " + label + " server on port " + port);
        this.label = label;
        this.clazz = clazz;
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.warning("problem creating server " + label + ":" + e);
            close();
            throw e;
        }
        logger.info("Server " + label + " created");
    }

    public void processInput(Consumer<T> consumer) {
        logger.info("server " + label + " connected");
        try {
            while (true) {
                T value = clazz.cast(inputStream.readObject());
                consumer.accept(value);
            }
        } catch (Exception e) {
            logger.warning("Problem processing data in " + label + ":" + e);
            close();
        }
    }

    @Override
    public void close() {
        logger.info("closing " + label + " server");
        NetworkUtil.closeQuiet(inputStream, clientSocket, serverSocket);
    }
}
