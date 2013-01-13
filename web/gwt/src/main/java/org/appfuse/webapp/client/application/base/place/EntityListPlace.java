/**
 * 
 */
package org.appfuse.webapp.client.application.base.place;

import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.DefaultProxyStore;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxySerializer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author ivangsa
 *
 */
public class EntityListPlace extends Place {

	private final Class<? extends EntityProxy> proxyClass;
	private final BaseProxy searchCriteria;

	/**
	 * @param proxyType
	 */
	public EntityListPlace(Class<? extends EntityProxy> proxyClass) {
		this.proxyClass = proxyClass;
		this.searchCriteria = null;
	}
	
	/**
	 * @param proxyType
	 * @param searchCriteria
	 */
	public EntityListPlace(Class<? extends EntityProxy> proxyClass, BaseProxy searchCriteria) {
		super();
		this.proxyClass = proxyClass;
		this.searchCriteria = searchCriteria;
	}


	public Class<? extends EntityProxy> getProxyClass() {
		return proxyClass;
	}
	
	public BaseProxy getSearchCriteria() {
		return searchCriteria;
	}
	
	/**
	 * Tokenizer.
	 */
	@Prefix("l")
	public static class Tokenizer implements PlaceTokenizer<EntityListPlace> {
		private static final String SEPARATOR = "!";
		private static final RequestFactory requests = GWT.create(ApplicationRequestFactory.class);//FIXME inject this
		private static final ProxySerializer serializer = requests.getSerializer(new DefaultProxyStore());

		public EntityListPlace getPlace(String token) {
			String tokens[] = token.split(SEPARATOR);
			Class<? extends EntityProxy> proxyType = requests.getProxyClass(tokens[0]);
			BaseProxy searchCriteria = null;
			if(tokens.length > 1) {
				searchCriteria = serializer.deserialize(proxyType, tokens[1]);
			}
			return new EntityListPlace(proxyType, searchCriteria);
		}

		public String getToken(EntityListPlace place) {
			StringBuilder sb = new StringBuilder();
			sb.append(requests.getHistoryToken(place.getProxyClass()));
			if(place.getSearchCriteria() != null) {
				sb.append(SEPARATOR);
				sb.append(serializer.serialize(place.getSearchCriteria()));
			}
			return sb.toString();
		}
	}

}
