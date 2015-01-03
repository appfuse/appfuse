/**
 * 
 */
package org.appfuse.webapp.client.ui.users.active;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class ActiveUsersPlace extends Place {

    @Prefix("activeUsers")
    public static class Tokenizer implements PlaceTokenizer<ActiveUsersPlace> {
        @Override
        public String getToken(ActiveUsersPlace place) {
            return "";
        }

        @Override
        public ActiveUsersPlace getPlace(String token) {
            return new ActiveUsersPlace();
        }
    }

}
