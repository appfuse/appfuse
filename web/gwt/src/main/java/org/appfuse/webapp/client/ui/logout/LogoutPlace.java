/**
 * 
 */
package org.appfuse.webapp.client.ui.logout;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class LogoutPlace extends Place {

    @Prefix("logout")
    public static class Tokenizer implements PlaceTokenizer<LogoutPlace> {
        @Override
        public String getToken(LogoutPlace place) {
            return "";
        }

        @Override
        public LogoutPlace getPlace(String token) {
            return new LogoutPlace();
        }
    }

}
