package org.appfuse.webapp.server.services.impl;

import org.apache.cxf.jaxrs.ext.MessageContext;

/**
 * Interface to allow injection of MessageContext into spring jdk proxies.
 * 
 * @author ivangsa
 *
 */
public interface CxfMessageContextAware {

    /**
     * Sets an CXF Message Context
     * 
     * @param messageContext
     */
    void setMessageContext(MessageContext messageContext);
}