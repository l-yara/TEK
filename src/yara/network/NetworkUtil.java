package yara.network;

/**
 * Common functionality shared between Client and Server
 */
class NetworkUtil {

    static void closeQuiet(AutoCloseable... closeables) {
        for (AutoCloseable autoCloseable : closeables) {
            if (autoCloseable != null) {
                try {
                    autoCloseable.close();
                } catch (Exception ignore) {
                    //if already closed or something else is going wrong - do nothing,
                    //just consider closing done
                }
            }
        }
    }
}
