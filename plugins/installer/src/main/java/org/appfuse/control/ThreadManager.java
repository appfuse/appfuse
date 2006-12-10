package org.appfuse.control;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David Whitehurst
 *
 */

import java.lang.ref.ReferenceQueue;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * ThreadManager class that creates a Runnable service that smoothly manages multiple command
 * requests in the form of add(Runnable).  A similar class was written by James Northrup originally
 * for the dbpirate project on dev.java.net.  This class has been modified many times, however
 * it effectively manages multiple threads and we have yet to find any problems with the
 * interruption logic.  Dr. Dobbs 2006, unknown month, had article on OSGI threading for Eclipse
 * that was very good.  Consider refactoring this. 
 */
public class ThreadManager {

    /**
     * The singleton
     */
    private static ThreadManager instance;

    /**
     *  Thread counter
     */
    private static int counter = 0;

    /**
     * List of Threads
     */
    private static final LinkedList threads = new LinkedList();

    /**
     * List of Runnables
     */
    private LinkedList runnables = new LinkedList();

    /**
     * Thread group (no real benefit)
     */
    private final ThreadGroup workerGroup = new ThreadGroup("ManagedThreads-" + getCounter());

    /**
     * Fixed number of worker Threads
     */
    private static final int WORKER_COUNT = 5;

    /**
     *
     */
    private final Thread controllerThread;

    /**
     * static thread queue
     */
    private static final ReferenceQueue referenceQueue = new ReferenceQueue();

    /**
     * static boolean for Factory status
     */
    private static boolean shutdown = false;

    /**
     * Static anonymous class that creates and runs a (Runnable) service host
     * that manages multiple thread requests
     */
    static {
        final Runnable runnable = new Runnable() {
            public void run() {
                while (!isShutdown()) {
                    final Reference reference;
                    try {
                        reference = getReferenceQueue().remove();
                    } catch (InterruptedException e) {
                        break;
                    }
                    final Object o = reference.get();
                    if (null != o) {
                        if (o instanceof ManagedThread) {
                            final ManagedThread w = (ManagedThread) o;
                            int numThreads = threads.size();
                            if (numThreads < WORKER_COUNT) {
                                enqueue(w);
                                new WeakReference(w);
                            } else {
                                reference.clear();
                            }
                        }
                    }
                }
            }
        };
        getInstance().add(runnable);
    }

    /**
     * adds threads to a synchronized LinkedList
     * 
     * @param thread
     */
    static private void enqueue(final ManagedThread thread) {
        synchronized (threads) {
            threads.addLast(thread);
        }
    };

    /**
     * adds runnables to a sychronized LinkedList
     */
    public final void add(final Runnable runnable) {
        synchronized (runnables) {
            runnables.addLast(runnable);
        }
        getControllerThread().interrupt();
    };

    /**
     * Thread Factory Constructor
     */
    public ThreadManager() {
        workerGroup.setDaemon(true);
        controllerThread = new Thread() {
            public void run() {
                while (!isShutdown()) {
                    if (((LinkedList) threads).isEmpty() && workerGroup.activeCount() < WORKER_COUNT) {
                        enqueue(new ManagedThread(workerGroup, "taskThread-" + counter++));
                    }
                    try {
                        sleep(1500);
                    } catch (InterruptedException ignore) {
                          // do nothing
                    }

                    while (!((LinkedList) threads).isEmpty() && !runnables.isEmpty()) {
                        final Runnable runnable;
                        final ManagedThread managedThread;
                        synchronized (runnables) {
                            runnable = (Runnable) runnables.removeFirst();
                        }
                        synchronized (threads) {
                            managedThread = (ManagedThread) threads.removeFirst();
                        }
                        managedThread.setRunnable(runnable);
                        managedThread.start();
                        System.err.println("someThread-" + getCounter());
                    }


                }

                workerGroup.interrupt();
            }

        };
        getControllerThread().start();


    }

    /**
     * Get singleton
     * @return
     */
    public static ThreadManager getInstance() {
        if (null == instance) instance = new ThreadManager();
        return instance;
    }

    /**
     * Is Factory shutdown
     * @return
     */
    private static boolean isShutdown() {
        return shutdown;
    }

    /**
     * Get thread counter
     * @return
     */
    private static int getCounter() {
        return counter;
    }

    /**
     * Get factory controller thread
     *
     * @return
     */
    private Thread getControllerThread() {
        return controllerThread;
    }

    /**
     * return static public Thread queue
     * @return
     */
    public static ReferenceQueue getReferenceQueue() {
        return referenceQueue;
    }
}
