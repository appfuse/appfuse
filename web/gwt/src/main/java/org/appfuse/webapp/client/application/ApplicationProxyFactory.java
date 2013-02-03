/**
 * 
 */
package org.appfuse.webapp.client.application;

import static com.google.web.bindery.requestfactory.shared.impl.Constants.STABLE_ID;

import java.util.Map;

import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.AutoBeanVisitor;
import com.google.web.bindery.autobean.shared.AutoBeanVisitor.PropertyContext;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.DefaultProxyStore;
import com.google.web.bindery.requestfactory.shared.ProxySerializer;
import com.google.web.bindery.requestfactory.shared.impl.AbstractRequestFactory;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.Constants;
import com.google.web.bindery.requestfactory.shared.impl.SimpleProxyId;

/**
 * @author ivangsa
 *
 */
public class ApplicationProxyFactory {

	public interface ProxyFactory extends AutoBeanFactory {
		  AutoBean<UsersSearchCriteriaProxy> searchCriteria();
		  AutoBean<UsersSearchCriteriaProxy> searchCriteria(UsersSearchCriteriaProxy toWrap);
	}
	
	private ProxyFactory autoBeanFactory = GWT.create(ProxyFactory.class);
	private final ApplicationRequestFactory requests;
	private final ProxySerializer serializer;
	
	/**
	 * @param requests
	 */
	@Inject
	public ApplicationProxyFactory(ApplicationRequestFactory requests) {
		super();
		this.requests = requests;
		this.serializer = requests.getSerializer(new DefaultProxyStore());
	}

	/**
	 * 
	 * @param proxyType
	 * @return
	 */
	public  <T extends BaseProxy> T create(Class<T> proxyType) {
		AutoBean<T> autoBean = (AutoBean<T>) autoBeanFactory.searchCriteria();
		allocateId(autoBean);
		return autoBean.as();
	}
	
	/**
	 * 
	 * @param proxy
	 * @return
	 */
	public <T extends BaseProxy> AutoBean<T> wrap(T proxy) {
		return autoBeanFactory.create(getProxyType(proxy), proxy);
	}	
	
	/**
	 * 
	 * @param proxy
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseProxy> T clone(T proxy) {
		
		final AutoBean<T> toClone = AutoBeanUtils.getAutoBean(proxy);
		final AutoBean<T> clone = autoBeanFactory.create(getProxyType(proxy)); 
	    clone.setTag(STABLE_ID, toClone.getTag(STABLE_ID));
	    clone.setTag(Constants.VERSION_PROPERTY_B64, toClone.getTag(Constants.VERSION_PROPERTY_B64));		
		clone.accept(new AutoBeanVisitor() {
			final Map<String, Object> values = AutoBeanUtils.getAllProperties(toClone);
			@Override
			public boolean visitValueProperty(String propertyName, Object value, PropertyContext ctx) {
				ctx.set(values.get(propertyName));
				return false;
			}			
		});
		return clone.as();
//		AutoBean<T> toClone = AutoBeanUtils.getAutoBean(proxy);
//		AutoBean<T> clone = toClone.getFactory().create(toClone.getType());
//		toClone.getFactory().create(toClone.getType());
//	    clone.setTag(STABLE_ID, toClone.getTag(STABLE_ID));
//	    clone.setTag(Constants.VERSION_PROPERTY_B64, toClone.getTag(Constants.VERSION_PROPERTY_B64));
//	    if(clone.isWrapper()) {
//	    	return clone.unwrap();
//	    }else {
//	    	return clone.as();
//	    }
		
//		AutoBean<T> autoBean = (AutoBean<T>) 
//					AutoBeanCodex.decode(autoBeanFactory, getProxyType(proxy), 
//						AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(proxy)));
//		Object	id =((AbstractRequestFactory)requests).allocateId(UsersSearchCriteriaProxy.class);
//		autoBean.setTag(Constants.STABLE_ID, id);
//		T clonedProxy = autoBean.as();
//		final SimpleProxyId<?> id2 = BaseProxyCategory.stableId(autoBean);
//		final SimpleProxyId<?> id3 = BaseProxyCategory.stableId(AutoBeanUtils.getAutoBean(clonedProxy));
//		if(id != id2) {
//			System.out.println("id != id2");
//		}
//		if(id2 != id3) {
//			System.out.println("id2 != id3");
//		}
//		
//		return clonedProxy;
	}
	
	/**
	 * 
	 * @param proxy
	 * @param frozen
	 */
	public <T extends BaseProxy> void setFrozen(T proxy, boolean frozen) {
		wrap(proxy).setFrozen(frozen);
	}
	

	/**
	 * 
	 * @param proxy
	 * @return
	 */
	public <T extends BaseProxy> String serialize(T proxy) {
		//return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(proxy)).getPayload();
		//return this.serializer.serialize(proxy);
		return null;
	}
	
	/**
	 * 
	 * @param proxyType
	 * @param key
	 * @return
	 */
	public <T extends BaseProxy> T deserialize(Class<T> proxyType, String key) {
		//return AutoBeanCodex.decode(autoBeanFactory, proxyType, key).as();
		//return this.serializer.deserialize(proxyType, key);
		return null;
	}

	
	public AutoBean<?> allocateId(AutoBean<?> autoBean) {
		Object	id = ((AbstractRequestFactory)requests).allocateId(UsersSearchCriteriaProxy.class);
		autoBean.setTag(Constants.STABLE_ID, id);
		return autoBean;
	}
	
	private Class getProxyType(BaseProxy baseProxy) {
		if(true) return UsersSearchCriteriaProxy.class;
//		for (Class proxyType : baseProxy.getClass().getInterfaces()) {
//			if(isBaseProxy(proxyType)) {
//				return proxyType;
//			}
//		}
		return null;
	}
	
	private boolean isBaseProxy(Class<?> type) {
		if(type.getSuperclass() == null) {
			return type.isInterface() && type.getSuperclass().equals(BaseProxy.class); 
		} else {
			return isBaseProxy(type.getSuperclass());
		}
		
	}
}
