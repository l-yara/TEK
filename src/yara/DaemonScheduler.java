package yara;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * The ScheduledThreadPoolExecutor which uses daemon threads only
 */
public class DaemonScheduler extends ScheduledThreadPoolExecutor {
    public DaemonScheduler(int corePoolSize) {
        super(corePoolSize, r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
    }
}
