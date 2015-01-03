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

    private static final String TOKEN_PREFIX = "login";
    private static final String FULL_TOKEN_PREFIX = TOKEN_PREFIX + ":";

    private String historyToken = "";

    public LoginPlace() {
        super();
    }

    /**
     * Constructor with a history token to redirect after successful login.
     * 
     * @param loginHistoryToken
     *            history token to redirect after successful login.
     */
    public LoginPlace(String loginHistoryToken) {
        super();
        if (loginHistoryToken != null) {
            if (loginHistoryToken.startsWith(FULL_TOKEN_PREFIX)) {
                this.historyToken = loginHistoryToken.replaceFirst(FULL_TOKEN_PREFIX, "");
            } else {
                this.historyToken = loginHistoryToken;
            }
        }
    }

    public String getHistoryToken() {
        return historyToken;
    }

    @Prefix(TOKEN_PREFIX)
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
        return false;// allow go to same place
    }
}
