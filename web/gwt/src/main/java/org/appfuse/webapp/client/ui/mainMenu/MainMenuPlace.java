/**
 * 
 */
package org.appfuse.webapp.client.ui.mainMenu;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class MainMenuPlace extends Place {

	private boolean reload = false;
	
	public MainMenuPlace() {
		super();
	}

	/**
	 * @param reload
	 */
	public MainMenuPlace(boolean reload) {
		super();
		this.reload = reload;
	}

	public boolean isReload() {
		return reload;
	}


	@Prefix("mainMenu")
    public static class Tokenizer implements PlaceTokenizer<MainMenuPlace> {
        @Override
        public String getToken(MainMenuPlace place) {
            return "";
        }

        @Override
        public MainMenuPlace getPlace(String token) {
            return new MainMenuPlace();
        }
    }

}
