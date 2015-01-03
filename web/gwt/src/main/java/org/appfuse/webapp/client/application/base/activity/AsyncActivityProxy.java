package org.appfuse.webapp.client.application.base.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class AsyncActivityProxy<A extends Activity> implements Activity {

    private final AsyncProvider<A> provider;
    private Activity activity;
    private boolean isCancelled;

    /**
     * @param provider
     */
    @Inject
    public AsyncActivityProxy(final AsyncProvider<A> provider) {
        super();
        this.provider = provider;
    }

    @Override
    public String mayStop() {
        if (activity != null) {
            return activity.mayStop();
        }
        return null;
    }

    @Override
    public final void onCancel() {
        isCancelled = true;
        if (activity != null) {
            activity.onCancel();
        }
    }

    @Override
    public void onStop() {
        if (activity != null) {
            activity.onStop();
        }
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        provider.get(new AsyncCallback<A>() {

            @Override
            public void onFailure(final Throwable reason) {
                Window.alert("Fatal Error: " + reason.getMessage());
            }

            @Override
            public void onSuccess(final A result) {
                activity = result;
                if (!isCancelled) {
                    activity.start(panel, eventBus);
                }
            }
        });
    }
}
