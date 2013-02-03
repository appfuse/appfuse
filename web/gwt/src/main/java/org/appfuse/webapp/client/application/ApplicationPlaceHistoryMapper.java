package org.appfuse.webapp.client.application;

import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.logout.LogoutPlace;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.client.ui.upload.FileUploadPlace;
import org.appfuse.webapp.client.ui.users.edit.places.EditProfilePlace;
import org.appfuse.webapp.client.ui.users.edit.places.SignUpPlace;

import com.google.gwt.place.shared.PlaceHistoryMapperWithFactory;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
	MainMenuPlace.Tokenizer.class,
	LoginPlace.Tokenizer.class,
	LogoutPlace.Tokenizer.class,
	EditProfilePlace.Tokenizer.class,
	SignUpPlace.Tokenizer.class,
	FileUploadPlace.Tokenizer.class
	})
public interface ApplicationPlaceHistoryMapper extends PlaceHistoryMapperWithFactory<ApplicationPlaceHistoryFactory> {}