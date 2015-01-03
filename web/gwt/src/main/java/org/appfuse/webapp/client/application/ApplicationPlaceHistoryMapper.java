package org.appfuse.webapp.client.application;

import org.appfuse.webapp.client.ui.home.HomePlace;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.logout.LogoutPlace;
import org.appfuse.webapp.client.ui.reloadOptions.ReloadOptionsPlace;
import org.appfuse.webapp.client.ui.upload.FileUploadPlace;
import org.appfuse.webapp.client.ui.users.active.ActiveUsersPlace;
import org.appfuse.webapp.client.ui.users.editProfile.EditProfilePlace;
import org.appfuse.webapp.client.ui.users.signUp.SignUpPlace;
import org.appfuse.webapp.client.ui.users.updatePassword.UpdatePasswordPlace;

import com.google.gwt.place.shared.PlaceHistoryMapperWithFactory;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
        HomePlace.Tokenizer.class,
        LoginPlace.Tokenizer.class,
        UpdatePasswordPlace.Tokenizer.class,
        LogoutPlace.Tokenizer.class,
        EditProfilePlace.Tokenizer.class,
        SignUpPlace.Tokenizer.class,
        ActiveUsersPlace.Tokenizer.class,
        FileUploadPlace.Tokenizer.class,
        ReloadOptionsPlace.Tokenizer.class
})
public interface ApplicationPlaceHistoryMapper extends PlaceHistoryMapperWithFactory<ApplicationPlaceHistoryFactory> {
}