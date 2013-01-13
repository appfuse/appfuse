package org.appfuse.webapp.client.application.base.place;

import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.core.shared.GWT;
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
	 * The things you do with a record, each of which is a different bookmarkable
	 * location in the application.
	 */
	public enum Operation {
		CREATE, 
		EDIT, 
		DETAILS
	}
	
	protected EntityProxyId<?> proxyId;
	protected Class<? extends EntityProxy> proxyClass;
	protected Operation operation;
	
	public EntityProxyPlace() {
		super();
	}

	/**
	 * @param operation
	 * @param proxyId
	 * @param proxyClass
	 */
	public EntityProxyPlace(Class<? extends EntityProxy> proxyClass) {
		super();
		this.operation = Operation.CREATE;
		this.proxyClass = proxyClass;
	}

	/**
	 * @param operation
	 * @param proxyId
	 * @param proxyClass
	 */
	public EntityProxyPlace(EntityProxyId<?> proxyId, Operation operation) {
		super();
		this.operation = operation;
		this.proxyId = proxyId;
	}
	

	public Operation getOperation() {
		return operation;
	}

	public Class<? extends EntityProxy> getProxyClass() {
		return proxyId != null ? proxyId.getProxyClass() : proxyClass;
	}

	/**
	 * @return the proxyId, or null if the operation is {@link Operation#CREATE}
	 */
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}
	
	
	/**
	 * 
	 * Tokenizer
	 *
	 */
	@Prefix("r")
	public static class Tokenizer implements PlaceTokenizer<EntityProxyPlace> {
		
		private static final String SEPARATOR = "!";
		private static final RequestFactory requests = GWT.create(ApplicationRequestFactory.class);//FIXME inject this

		public EntityProxyPlace getPlace(String token) {
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.CREATE == operation) {
				return new EntityProxyPlace(requests.getProxyClass(bits[0]));
			}
			return new EntityProxyPlace(requests.getProxyId(bits[0]), operation);
		}

		public String getToken(EntityProxyPlace place) {
			if (Operation.CREATE == place.getOperation()) {
				return requests.getHistoryToken(place.getProxyClass()) + SEPARATOR + place.getOperation();
			}
			return requests.getHistoryToken(place.getProxyId()) + SEPARATOR + place.getOperation();
		}
	}
	

// XXX figure out how to reload the same place (i.e after saving) if Places are equal?	
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof EntityProxyPlace)) {
//			return false;
//		}
//		if (this == obj) {
//			return true;
//		}
//
//		EntityProxyPlace other = (EntityProxyPlace) obj;
//		if (operation != other.operation) {
//			return false;
//		}
//		if (proxyClass == null) {
//			if (other.proxyClass != null) {
//				return false;
//			}
//		} else if (!proxyClass.equals(other.proxyClass)) {
//			return false;
//		}
//		if (proxyId == null) {
//			if (other.proxyId != null) {
//				return false;
//			}
//		} else if (!proxyId.equals(other.proxyId)) {
//			return false;
//		}
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
//		result = prime * result + ((proxyClass == null) ? 0 : proxyClass.hashCode());
//		result = prime * result + ((proxyId == null) ? 0 : proxyId.hashCode());
//		return result;
//	}

	@Override
	public String toString() {
		return "ProxyPlace [operation=" + operation + ", proxy=" + proxyId + ", proxyClass=" + proxyClass + "]";
	}
}
