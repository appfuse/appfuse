/**
 * 
 */
package org.appfuse.webapp.client.application.base.place;

import java.util.logging.Logger;

import org.appfuse.webapp.client.application.ApplicationProxyFactory;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author ivangsa
 *
 */
public class EntitySearchPlace extends Place {
    private static final String PREFIX = "l";

    private final Class<? extends EntityProxy> proxyClass;
    private Object searchCriteria;
    private int firstResult = 0;
    private int maxResults = 25;

    /**
     * @param proxyType
     */
    public EntitySearchPlace(Class<? extends EntityProxy> proxyClass) {
        this.proxyClass = proxyClass;
        this.searchCriteria = null;
    }

    /**
     * @param proxyType
     * @param searchCriteria
     */
    public EntitySearchPlace(Class<? extends EntityProxy> proxyClass, Object searchCriteria) {
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
    public EntitySearchPlace(Class<? extends EntityProxy> proxyClass, int firstResult, int maxResults, Object searchCriteria) {
        super();
        this.proxyClass = proxyClass;
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.searchCriteria = searchCriteria;
    }

    public Class<? extends EntityProxy> getProxyClass() {
        return proxyClass;
    }

    public Object getSearchCriteria() {
        return searchCriteria;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setSearchCriteria(Object searchCriteria) {
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
    public static class Tokenizer implements PlaceTokenizer<EntitySearchPlace> {
        protected final Logger logger = Logger.getLogger(getClass().getName());

        private static final String SEPARATOR = "!";
        private final ApplicationProxyFactory proxyFactory;
        private final RequestFactory requests;

        /**
         * @param proxyFactory
         */
        public Tokenizer(ApplicationProxyFactory proxyFactory, RequestFactory requests) {
            super();
            this.proxyFactory = proxyFactory;
            this.requests = requests;
        }

        public EntitySearchPlace getPlace(String token) {
            logger.fine("Slicing token: " + token);
            String tokens[] = token.split(SEPARATOR);
            Class<? extends EntityProxy> proxyType = requests.getProxyClass(tokens[0]);
            Object searchCriteria = null;
            int firstResult = 0;
            int maxResults = 0;
            if (tokens.length > 2) {
                firstResult = parseInt(tokens[1]);
                maxResults = parseInt(tokens[2]);
            }
            if (tokens.length > 3) {
                Class<? extends Object> searchCriteriaClass = proxyFactory.getSearchCriteriaTypeForProxy(proxyType);
                searchCriteria = proxyFactory.deserialize(searchCriteriaClass, tokens[3]);
            }
            return new EntitySearchPlace(proxyType, firstResult, maxResults, searchCriteria);
        }

        public String getToken(EntitySearchPlace place) {
            StringBuilder sb = new StringBuilder();
            sb.append(requests.getHistoryToken(place.getProxyClass()));
            sb.append(SEPARATOR);
            sb.append(place.getFirstResult());
            sb.append(SEPARATOR);
            sb.append(place.getMaxResults());
            if (place.getSearchCriteria() != null) {
                sb.append(SEPARATOR);
                sb.append(proxyFactory.serialize(place.getSearchCriteria()));
            }
            return sb.toString();
        }

        public String getFullHistoryToken(EntitySearchPlace place) {
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

}
