/**
 * 
 */
package org.appfuse.webapp.client.ui.users.edit.places;

import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.proxies.UserProxy;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class SignUpPlace extends EntityProxyPlace {

	@Prefix("signup")
    public static class Tokenizer implements PlaceTokenizer<SignUpPlace> {

		@Override
		public SignUpPlace getPlace(String token) {
			SignUpPlace place = new SignUpPlace();
			place.proxyClass = UserProxy.class;
			place.operation = Operation.CREATE;
			return place;
		}

		@Override
		public String getToken(SignUpPlace place) {
			return "";
		}

    }
}
