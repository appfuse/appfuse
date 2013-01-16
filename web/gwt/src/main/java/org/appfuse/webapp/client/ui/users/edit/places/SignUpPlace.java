/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class SignUpPlace extends Place {

	@Prefix("signup")
    public static class Tokenizer implements PlaceTokenizer<SignUpPlace> {

		@Override
		public SignUpPlace getPlace(String token) {
			return new SignUpPlace();
		}

		@Override
		public String getToken(SignUpPlace place) {
			return "";
		}

    }
}
