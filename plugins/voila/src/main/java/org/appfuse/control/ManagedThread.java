package org.appfuse.control;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

import static org.appfuse.control.ThreadManager.*;

import java.lang.ref.PhantomReference;

public final class ManagedThread extends Thread {
    private Runnable runnable;

    {
        new PhantomReference<ManagedThread>(this, getReferenceQueue());
    }

    /**
     * Constructor, associates this Thread with a ThreadGroup
     * 
     * @param group
     * @param name
     */
    public ManagedThread(final ThreadGroup group, final String name) {
        super(group, name);

    }

    /**
     * The run method
     */
    public final void run() {
        getRunnable().run();
        setRunnable(null);
    }

    /**
     * Set the runnable on this Thread
     * @param r
     */
    public final void setRunnable(final Runnable r) {
        runnable = r;
    }

    /**
     * Get the runnable for this Thread
     * @return
     */
    private Runnable getRunnable() {
        return runnable;
    }
}
