/**
 * 
 */
package org.appfuse.webapp.client.ui.users.signUp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class SignUpPlace extends Place {

    public static final String PREFIX = "signup";

    @Prefix(PREFIX)
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
