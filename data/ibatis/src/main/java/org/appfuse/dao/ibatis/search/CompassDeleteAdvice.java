package org.appfuse.dao.ibatis.search;

import org.appfuse.dao.ibatis.GenericDaoiBatis;
import org.compass.core.mapping.Cascade;
import org.compass.core.mapping.CompassMapping;
import org.compass.core.spi.InternalCompass;

import java.lang.reflect.Method;

/**
 * An extension to Compass Delete Advice that works only wiht {@link org.appfuse.dao.ibatis.GenericDaoiBatis}
 * class (or sub classes) and uses its {@link org.appfuse.dao.ibatis.GenericDaoiBatis#getPersistentClass()}
 * in order to know which class to delete from the index based on its PK.
 *
 * @author kimchy
 */
public class CompassDeleteAdvice extends org.compass.spring.aop.CompassDeleteAdvice {

    private CompassMapping compassMapping;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        compassMapping = ((InternalCompass) compassTemplate.getCompass()).getMapping();
    }
    
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if (!(target instanceof GenericDaoiBatis)) {
            return;
        }
        Class persistentClass = ((GenericDaoiBatis) target).getPersistentClass();
        if (compassMapping.hasMappingForClass(persistentClass, Cascade.DELETE)) {
            compassTemplate.delete(persistentClass, findObject(returnValue, args));
        }
    }
}
