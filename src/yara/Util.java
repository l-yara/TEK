package yara;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class with common functionality
 */
public class Util {
    private static Logger logger = Logger.getLogger(Util.class.getName());

    //shared control on the logging
    //is being executed by both (Randomizer and Prime) applications so it is a convenient place
    static {
        Logger.getLogger(Util.class.getPackage().getName()).setLevel(Level.WARNING);
    }

    // parse the given arguments, return array of [primePort, randomizerPort]
    public static int[] parsePortNumbers(String... args) {
        //expect exactly two parameters: port for Randomizer -> Prime and port for Prime -> Randomizer
        if (args.length != 2) {
            logger.warning("expecting exactly 2 parameters, got " + Arrays.toString(args));
            System.exit(1);
        }
        return new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1])};
    }


}
