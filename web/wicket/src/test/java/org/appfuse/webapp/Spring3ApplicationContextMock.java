package org.appfuse.webapp;

import org.apache.wicket.spring.test.ApplicationContextMock;
import org.springframework.beans.BeansException;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;

/**
 * org.appfuse.webapp.Spring3ApplicationContextMock
 *
 * @author Marcin Zajączkowski, 2011-06-19
 *
 * AnnotApplicationContextMock could be potentially easier to use as a base class
 */
@Deprecated //StaticWebApplicationContext seems to be enough
public class Spring3ApplicationContextMock extends ApplicationContextMock {

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) throws BeansException {
        return Collections.emptyMap();
    }

    public <A extends Annotation> A findAnnotationOnBean(String s, Class<A> aClass) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> tClass) throws BeansException {
        Map<String, T> foundBeans = getBeansOfType(tClass);
        final int numberOfBeans = foundBeans.size();
        if (numberOfBeans == 0 || numberOfBeans > 1) {
            //TODO: Find some better concrete exception
            throw new BeansException(
                    format("Just one %s bean expected. Found %d.", tClass.getName(), numberOfBeans)) {};
        }
        return foundBeans.entrySet().iterator().next().getValue();
    }
}
