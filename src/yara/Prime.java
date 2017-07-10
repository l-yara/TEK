package yara;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

import yara.network.Client;
import yara.network.Server;

public class Prime {
    private static Logger logger = Logger.getLogger(Prime.class.getName());

    private final int primePort;
    private final int randomizerPort;

    //threads to use for parallel calculations
    private final ScheduledThreadPoolExecutor executor = new DaemonScheduler(20);

    public Prime(int primePort, int randomizerPort) {
        this.primePort = primePort;
        this.randomizerPort = randomizerPort;
    }

    public static void main(String[] args) throws Exception {
        int[] ports = Util.parsePortNumbers(args);
        Prime prime = new Prime(ports[0], ports[1]);
        prime.start();
    }

    private void start() {
        logger.info("starting Prime server, initiating client");
        try (Server<Integer> server = new Server<>(primePort, "Prime", Integer.class);
             Client<AnswerVO> client = new Client<>(randomizerPort, "Prime")) {
            //on each incoming value, schedule isPrime(...) check in a separate thread
            //(sending data back must be synchronized between threads as client is not thread-safe)
            server.processInput(i -> executor.execute(() -> {
                logger.info("got " + i);
                AnswerVO answerVO = new AnswerVO(i, isPrime(i));
                client.send(answerVO);
            }));
        } catch (Exception e) {
            logger.warning("Problem in Prime: " + e);
        }
    }

    boolean isPrime(int n) {
        // fast even test.
        if (n > 2 && (n & 1) == 0)
            return false;
        // only odd factors need to be tested up to n^0.5
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

}
