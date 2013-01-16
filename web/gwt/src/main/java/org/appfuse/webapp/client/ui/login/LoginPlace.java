/**
 * 
 */
package org.appfuse.webapp.client.ui.login;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class LoginPlace extends Place {

	private String historyToken = "";

	public LoginPlace() {
		super();
	}

	public LoginPlace(String loginHistoryToken) {
		super();
		if(loginHistoryToken != null) {
			this.historyToken = loginHistoryToken;
		}
	}

	public String getHistoryToken() {
		return historyToken;
	}

	@Prefix("login")
    public static class Tokenizer implements PlaceTokenizer<LoginPlace> {
        @Override
        public String getToken(LoginPlace place) {
            return place.historyToken;
        }

        @Override
        public LoginPlace getPlace(String token) {
            return new LoginPlace(token);
        }
    }

	@Override
	public boolean equals(Object obj) {
		return false;//allow go to same place
	}
}
