/*
Copyright (c) 2010, Chin Huang
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice,
   this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.mycompany.webapp.jsp;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

/**
 * {@link ELResolver} which escapes XML in String values.
 */
public class EscapeXmlELResolver extends ELResolver {

    private ELResolver originalResolver;
    private ThreadLocal<Boolean> gettingValue = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    
    private ELResolver getOriginalResolver(ELContext context) {
        if (originalResolver == null) {
            originalResolver = context.getELResolver();
        }
        return originalResolver;
    }
    
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return getOriginalResolver(context).getCommonPropertyType(context, base);
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(
            ELContext context, Object base)
    {
        return getOriginalResolver(context).getFeatureDescriptors(context, base);
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        return getOriginalResolver(context).getType(context, base, property);
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (gettingValue.get()) {
            return null;
        }
        
        // This resolver is in the original resolver chain.  When this resolver
        // invokes the original resolver chain, set a flag so when execution
        // reaches this resolver, act like this resolver is not in the chain.
        gettingValue.set(true);
        Object value =
                getOriginalResolver(context).getValue(context, base, property);
        gettingValue.set(false);

        if (value instanceof String) {
            value = EscapeXml.escape((String) value);
        }
        return value;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        return getOriginalResolver(context).isReadOnly(context, base, property);
    }

    @Override
    public void setValue(
            ELContext context, Object base, Object property, Object value)
        throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException
    {
        getOriginalResolver(context).setValue(context, base, property, value);
    }
}
