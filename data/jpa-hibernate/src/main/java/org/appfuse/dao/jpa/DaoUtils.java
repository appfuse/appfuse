package org.appfuse.dao.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DaoUtils {

    protected static final Log log = LogFactory.getLog(DaoUtils.class);

    private static final String GET_INITIALS = "get";

    @SuppressWarnings("unchecked")
    public static Object getPersistentId(Object o) throws PersistenceException {
        Object objId = null;
        final String eMsg = "Error executing get<IdValue> method on persistent class.";
        String logMsg = "Persistent identity for object of type '?1' is accessible with method '?2'";
        boolean hasId = false;

        try {
            final AccessibleObject annotatedAccessibleObject = getAnnotatedAccessibleObject(
                    o, Id.class, EmbeddedId.class);

            if (annotatedAccessibleObject != null) {
                hasId = true;
                Method getter = null;
                if (annotatedAccessibleObject instanceof Method) {
                    getter = (Method) annotatedAccessibleObject;
                } else if (annotatedAccessibleObject instanceof Field) {
                    getter = findGetter(o.getClass(),
                            ((Field) annotatedAccessibleObject).getName());
                }

                objId = getter.invoke(o);
                if (log.isDebugEnabled()) {
                    logMsg = logMsg.replace("?1", o.getClass().getName());
                    logMsg = logMsg.replace("?2", getter.getName());
                    log.debug(logMsg);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new PersistenceException(eMsg, e);
        } catch (IllegalAccessException e) {
            throw new PersistenceException(eMsg, e);
        } catch (InvocationTargetException e) {
            throw new PersistenceException(eMsg, e);
        } catch (SecurityException e) {
            throw new PersistenceException(eMsg, e);
        } catch (NoSuchMethodException e) {
            throw new PersistenceException("There is no getter method for "
                    + o.getClass().getSimpleName() + " ID property", e);
        }
        
        if (!hasId) {
            throw new PersistenceException("Object of type '"
                    + o.getClass().getName()
                    + "' does not have an @Id or @EmbeddedId annotation.");
        }

        return objId;
    }

    private static AccessibleObject getAnnotatedAccessibleObject(Object o, Class<? extends Annotation>... annotations)
            throws PersistenceException {

        final Set<AccessibleObject> members = new HashSet<AccessibleObject>();
        members.addAll(Arrays.asList(o.getClass().getDeclaredMethods()));
        members.addAll(Arrays.asList(o.getClass().getDeclaredFields()));

        for (AccessibleObject member : members) {
            for (Class<? extends Annotation> annotation : annotations)
                if (member.isAnnotationPresent(annotation))
                    return member;
        }

        return null;
    }

    private static Method findGetter(Class type, String property) throws SecurityException, NoSuchMethodException {
        String methodName = GET_INITIALS
                + Character.toUpperCase(property.charAt(0))
                + property.substring(1);
        try {
            return type.getMethod(methodName);
        } catch (SecurityException e) {
            throw e;
        } catch (NoSuchMethodException e) {
            throw e;
        }
    }
}
