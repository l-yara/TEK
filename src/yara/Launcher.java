package yara;

import java.io.File;
import java.util.logging.Logger;
import yara.Util;
import yara.network.Client;

public class Launcher {
    private static Logger logger = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) {
        try {
            Process randomizerProcess = getProcessBuilder(Randomizer.class, args).start();
            Process primeProcess = getProcessBuilder(Prime.class, args).start();

            System.out.println("=== Press Enter to stop applications ====");
            System.in.read();
            System.out.println("Terminating processes");

            randomizerProcess.destroy();
            primeProcess.destroy();
            System.out.println("Done");
        } catch (Exception e) {
            logger.warning("Exception operating processes: " + e);
        }
    }

    private static ProcessBuilder getProcessBuilder(Class clazz, String[] args) {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = clazz.getCanonicalName();

        return new ProcessBuilder(
                javaBin, "-cp", classpath, className, args[0], args[1]).inheritIO();
    }
}