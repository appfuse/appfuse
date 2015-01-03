/**
 * 
 */
package org.appfuse.webapp.client.ui.reloadOptions;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class ReloadOptionsPlace extends Place {

    @Prefix("reloadOptions")
    public static class Tokenizer implements PlaceTokenizer<ReloadOptionsPlace> {
        @Override
        public String getToken(ReloadOptionsPlace place) {
            return "";
        }

        @Override
        public ReloadOptionsPlace getPlace(String token) {
            return new ReloadOptionsPlace();
        }
    }

}
