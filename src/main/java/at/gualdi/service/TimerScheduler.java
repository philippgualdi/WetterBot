package at.gualdi.service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TimerScheduler extends Timer {

	private static volatile TimerScheduler instanz;
	private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1); ///< Thread to execute operations
	
	private TimerScheduler(){	
	}
	
	/**
     * Singleton pattern
     *
     * @return Instance of the scheduler
     */
    public static TimerScheduler getInstance() {
        final TimerScheduler currentInstance;
        if (instanz == null) {
            synchronized (TimerScheduler.class) {
                if (instanz == null) {
                    instanz = new TimerScheduler();
                }
                currentInstance = instanz;
            }
        } else {
            currentInstance = instanz;
        }
        return currentInstance;
    }
    
    
    /**
     * Add a new CustomTimerTask to be executed
     *
     * @param task       Task to execute
     * @param targetHour Hour to execute it
     * @param targetMin  Minute to execute it
     * @param targetSec  Second to execute it
     */
    public void startExecutionEveryDayAt(DefaultTimerTask task, int targetHour, int targetMin, int targetSec) {
        //BotLogger.warn(LOGTAG, "Posting new task" + task.getTaskName());
       
    }
    
    /**
     * Find out next daily execution
     *
     * @param targetHour Target hour
     * @param targetMin  Target minute
     * @param targetSec  Target second
     * @return time in second to wait
     */
    private long computNextDilay(int targetHour, int targetMin, int targetSec) {
        final LocalDateTime localNow = LocalDateTime.now(Clock.systemUTC());
        LocalDateTime localNextTarget = localNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        while (localNow.compareTo(localNextTarget) > 0) {
            localNextTarget = localNextTarget.plusDays(1);
        }

        final Duration duration = Duration.between(localNow, localNextTarget);
        return duration.getSeconds();
    }

    
    /**
     * Stop the thread
     */
    public void stop() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
           // BotLogger.severe(LOGTAG, ex);
        } catch (Exception e) {
            //BotLogger.severe(LOGTAG, "Bot threw an unexpected exception at TimerExecutor", e);
        }
    }
}
