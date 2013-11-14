/**
 *
 */
package org.appfuse.webapp.client.ui.users.updatePassword;

import org.appfuse.webapp.client.ui.users.updatePassword.UpdatePasswordView.UserCredentials;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class UpdatePasswordPlace extends Place {

    private static final String PAIRS_SEPARATOR = "!";
    private static final String LABEL_VALUE_SEPARATOR = "=";

    private final UserCredentials userCredentials = new UserCredentials();

    public UpdatePasswordPlace() {
        super();
    }

    public UpdatePasswordPlace(final String username) {
        super();
        userCredentials.setUsername(username);
    }

    public UpdatePasswordPlace(final String username, final String token) {
        super();
        userCredentials.setUsername(username);
        userCredentials.setToken(token);
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    @Prefix("updatePassword")
    public static class Tokenizer implements PlaceTokenizer<UpdatePasswordPlace> {
        @Override
        public String getToken(final UpdatePasswordPlace place) {
            final StringBuffer sb = new StringBuffer();
            if (place.userCredentials.getUsername() != null) {
                sb.append("username" + LABEL_VALUE_SEPARATOR + place.userCredentials.getUsername());
            }
            sb.append(PAIRS_SEPARATOR);
            if (place.userCredentials.getToken() != null) {
                sb.append("token" + LABEL_VALUE_SEPARATOR + place.userCredentials.getToken());
            }
            return sb.toString();
        }

        @Override
        public UpdatePasswordPlace getPlace(final String token) {
            final UpdatePasswordPlace place = new UpdatePasswordPlace();
            final String[] pairs = token.split(PAIRS_SEPARATOR);
            for (final String string : pairs) {
                if (string != null) {
                    final String[] labelValue = string.split(LABEL_VALUE_SEPARATOR);
                    if (labelValue.length == 2) {
                        if ("username".equals(labelValue[0])) {
                            place.userCredentials.setUsername(labelValue[1]);
                        }
                        if ("token".equals(labelValue[0])) {
                            place.userCredentials.setToken(labelValue[1]);
                        }
                    }
                }
            }
            return place;
        }
    }
}
