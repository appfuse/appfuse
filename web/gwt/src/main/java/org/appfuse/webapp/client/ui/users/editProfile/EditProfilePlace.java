/**
 * 
 */
package org.appfuse.webapp.client.ui.users.editProfile;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class EditProfilePlace extends Place {

    @Prefix("editProfile")
    public static class Tokenizer implements PlaceTokenizer<EditProfilePlace> {

        @Override
        public EditProfilePlace getPlace(String token) {
            EditProfilePlace place = new EditProfilePlace();
            // place.proxyClass = UserProxy.class;
            // place.operation = Operation.EDIT;
            return place;
        }

        @Override
        public String getToken(EditProfilePlace place) {
            return "";
        }

    }

}
