/**
 * 
 */
package org.appfuse.webapp.client.ui.users.active;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;

/**
 * @author ivangsa
 *
 */
public class ActiveUsersActivity extends AbstractBaseActivity implements ActiveUsersView.Delegate {

	private Handler clientSideSortHandler;
	
	public ActiveUsersActivity(Application application) {
		super(application);
		setTitle(i18n.activeUsers_title());
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		final ActiveUsersView view = viewFactory.getView(ActiveUsersView.class);
		view.setDelegate(this);
		panel.setWidget(view);
		
		final CellTable<UserProxy> cellTable = view.getCellTable();
		requests.userRequest().getActiveUsers().fire(new Receiver<List<UserProxy>>() {

			@Override
			public void onSuccess(List<UserProxy> activeUsers) {
				clientSideSortHandler = new ColumnSortEvent.ListHandler<UserProxy>(activeUsers);
				cellTable.addColumnSortHandler(clientSideSortHandler);
				cellTable.setRowData(activeUsers);
				cellTable.setPageSize(25);
			}
		});
	}

	@Override
	public void cancelClicked() {
		placeController.goTo(new MainMenuPlace());
	}

}

