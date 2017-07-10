package yara;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import yara.network.Client;
import yara.network.Server;

public class Randomizer {
    private static Logger logger = Logger.getLogger(Randomizer.class.getName());

    //2 thread will be enough: for periodical "send random int to Prime" and for
    // "receive data from Prime" tsk in the separate thread
    private final ScheduledThreadPoolExecutor executor = new DaemonScheduler(2);

    private final Random random = new Random(new Date().getTime());

    private final int primePort;
    private final int randomizerPort;
    //delay, in ms, between sending new integers to the Prime service
    private final long delay;

    private Randomizer(int primePort, int randomizerPort, long delay) {
        this.primePort = primePort;
        this.randomizerPort = randomizerPort;
        this.delay = delay;
        logger.info("Randomizer will send data to port " + primePort + ", listen to port " + randomizerPort);
    }

    private void start() {
        //init server and client
        try (Client<Integer> client = new Client<>(primePort, "Randomizer");
             Server<AnswerVO> server = new Server<>(randomizerPort, "Randomizer", AnswerVO.class)) {
            //"server" to process resulting AnswerVO's in the separate thread
            executor.submit(() -> //
                    server.processInput(answerVo -> {
                            //OK to use concatenation - it will be compiled by Javac to use StringBuilder anyway
                            System.out.println("Integer " + answerVo.getValue() + " is " + (answerVo.isPrime() ? "" : "not ") + "prime");

                    }));

            executor.scheduleAtFixedRate(() -> {
                int value = random.nextInt(Integer.MAX_VALUE);
                logger.info("Sending: " + value);
                client.send(value);
            }, 0, delay, TimeUnit.MILLISECONDS).get();
            //^^^ block execution of the main thread forever via .get()

        } catch (Exception e) {
            logger.warning("Problem in Randomizer: " + e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int[] ports = Util.parsePortNumbers(args);
        Randomizer instance = new Randomizer(ports[0], ports[1], 120);
        instance.start();
    }

}
