/**
 * 
 */
package org.appfuse.webapp.client.ui.home;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class HomePlace extends Place {

    @Prefix("home")
    public static class Tokenizer implements PlaceTokenizer<HomePlace> {
        @Override
        public String getToken(final HomePlace place) {
            return "";
        }

        @Override
        public HomePlace getPlace(final String token) {
            return new HomePlace();
        }
    }

}
