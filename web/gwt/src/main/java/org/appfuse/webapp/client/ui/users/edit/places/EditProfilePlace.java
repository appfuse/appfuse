/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit.places;

import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.proxies.UserProxy;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class EditProfilePlace extends EntityProxyPlace {
	
	@Prefix("editProfile")
    public static class Tokenizer implements PlaceTokenizer<EditProfilePlace> {

		@Override
		public EditProfilePlace getPlace(String token) {
			EditProfilePlace place = new EditProfilePlace();
			place.proxyClass = UserProxy.class;
			place.operation = Operation.CREATE;
			return place;
		}

		@Override
		public String getToken(EditProfilePlace place) {
			return "";
		}

    }

}
