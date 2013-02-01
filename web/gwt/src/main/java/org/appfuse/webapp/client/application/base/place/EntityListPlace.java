/**
 * 
 */
package org.appfuse.webapp.client.application.base.place;

import java.util.HashMap;
import java.util.Map;

import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;
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
	private static final String PREFIX = "l";

	private static final Map<Class<? extends EntityProxy>,Class<? extends BaseProxy>> 
		SEARCH_CRITERIA_ENTITIES_MAP = new HashMap<Class<? extends EntityProxy>,Class<? extends BaseProxy>>();
	
	static {
		// You need to map proxyClass - searchCriteria.getClass() here 
		// for automatic serialization of the search criteria to the history token, as
		// there is no way to re-generate a class object, that is not an EntityProxy.class, from a token string 
		SEARCH_CRITERIA_ENTITIES_MAP.put(UserProxy.class, UsersSearchCriteriaProxy.class);
	}
	
	private final Class<? extends EntityProxy> proxyClass;
	private BaseProxy searchCriteria;
	private int firstResult = 0;
	private int maxResults = 25;

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

	/**
	 * @param proxyClass
	 * @param searchCriteria
	 * @param firstResult
	 * @param maxResults
	 */
	public EntityListPlace(Class<? extends EntityProxy> proxyClass, BaseProxy searchCriteria, int firstResult, int maxResults) {
		super();
		this.proxyClass = proxyClass;
		this.searchCriteria = searchCriteria;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public Class<? extends EntityProxy> getProxyClass() {
		return proxyClass;
	}
	
	public BaseProxy getSearchCriteria() {
		return searchCriteria;
	}
	
	public int getFirstResult() {
		return firstResult;
	}

	public void setSearchCriteria(BaseProxy searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}


	/**
	 * Tokenizer.
	 */
	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<EntityListPlace> {
		private static final String SEPARATOR = "!";
		private static final RequestFactory requests = GWT.create(ApplicationRequestFactory.class);//FIXME inject this
		private static final ProxySerializer serializer = requests.getSerializer(new DefaultProxyStore());

		public EntityListPlace getPlace(String token) {
			String tokens[] = token.split(SEPARATOR);
			Class<? extends EntityProxy> proxyType = requests.getProxyClass(tokens[0]);
			BaseProxy searchCriteria = null;
			int firstResult = 0;
			int maxResults = 0;
			if(tokens.length > 2) {
				firstResult = parseInt(tokens[1]);
				maxResults = parseInt(tokens[2]);
			}
			if(tokens.length > 3) {
				Class<? extends BaseProxy> searchCriteriaClass = SEARCH_CRITERIA_ENTITIES_MAP.get(proxyType);
				searchCriteria = serializer.deserialize(searchCriteriaClass, tokens[3]);
			}
			return new EntityListPlace(proxyType, searchCriteria, firstResult, maxResults);
		}

		public String getToken(EntityListPlace place) {
			StringBuilder sb = new StringBuilder();
			sb.append(requests.getHistoryToken(place.getProxyClass()));
			sb.append(SEPARATOR);
			sb.append(place.getFirstResult());
			sb.append(SEPARATOR);
			sb.append(place.getMaxResults());
			if(place.getSearchCriteria() != null) {
				sb.append(SEPARATOR);
				sb.append(serializer.serialize(place.getSearchCriteria()));
			}
			return sb.toString();
		}
		
		public String getFullHistoryToken(EntityListPlace place) {
			return PREFIX + ":" + getToken(place);
		}
		
		private int parseInt(String text) {
			try {
				return Integer.parseInt(text);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		return false;//allow reloading by changing place
	}

}
