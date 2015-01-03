package org.appfuse.webapp.client.application.base.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * A place in the app that deals with a specific {@link EntityProxy}
 */
public class EntityProxyPlace extends Place {
    /**
     * The things you do with a record, each of which is a different
     * bookmarkable location in the application.
     */
    public enum Operation {
        CREATE,
        EDIT,
        DETAILS
    }

    protected String entityId;
    protected Class<? extends EntityProxy> proxyClass;
    protected Operation operation;

    /**
     * @param proxyClass
     */
    public EntityProxyPlace(Class<? extends EntityProxy> proxyClass) {
        super();
        this.operation = Operation.CREATE;
        this.proxyClass = proxyClass;
    }

    /**
     * @param proxyClass
     * @param entityId
     * @param operation
     */
    public EntityProxyPlace(Class<? extends EntityProxy> proxyClass, String entityId, Operation operation) {
        super();
        this.proxyClass = proxyClass;
        this.entityId = entityId;
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getEntityId() {
        return entityId;
    }

    public Class<? extends EntityProxy> getProxyClass() {
        return proxyClass;
    }

    /**
     * 
     * Tokenizer
     *
     */
    @Prefix("r")
    public static class Tokenizer implements PlaceTokenizer<EntityProxyPlace> {

        private static final String SEPARATOR = "!";
        private final RequestFactory requests;

        /**
         * @param proxyFactory
         */
        public Tokenizer(RequestFactory requests) {
            super();
            this.requests = requests;
        }

        public EntityProxyPlace getPlace(String token) {
            String bits[] = token.split(SEPARATOR);
            Class<? extends EntityProxy> proxyClass = requests.getProxyClass(bits[0]);
            String entityId = bits[1];
            Operation operation = Operation.valueOf(bits[2]);
            return new EntityProxyPlace(proxyClass, entityId, operation);
        }

        public String getToken(EntityProxyPlace place) {
            return requests.getHistoryToken(
                    place.getProxyClass()) +
                    SEPARATOR + place.getEntityId() +
                    SEPARATOR + place.getOperation();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntityProxyPlace)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        EntityProxyPlace other = (EntityProxyPlace) obj;
        if (operation != other.operation) {
            return false;
        }
        if (proxyClass == null) {
            if (other.proxyClass != null) {
                return false;
            }
        } else if (!proxyClass.equals(other.proxyClass)) {
            return false;
        }
        if (entityId == null) {
            if (other.entityId != null) {
                return false;
            }
        } else if (!entityId.equals(other.entityId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + ((proxyClass == null) ? 0 : proxyClass.hashCode());
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ProxyPlace [operation=" + operation + ", proxy=" + entityId + ", proxyClass=" + proxyClass + "]";
    }
}
