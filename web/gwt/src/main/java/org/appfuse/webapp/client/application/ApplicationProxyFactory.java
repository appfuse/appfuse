/**
 * 
 */
package org.appfuse.webapp.client.application;

import static com.google.web.bindery.autobean.shared.AutoBeanUtils.getAutoBean;

import java.util.HashMap;
import java.util.Map;

import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.client.requests.ApplicationRequestFactory;

import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.impl.AbstractRequestFactory;
import com.google.web.bindery.requestfactory.shared.impl.Constants;

/**
 * @author ivangsa
 *
 */
public class ApplicationProxyFactory {
    private static final Map<Class<? extends EntityProxy>, Class<?>> SEARCH_CRITERIA_TYPES_MAP = new HashMap<Class<? extends EntityProxy>, Class<?>>();

    static {
        // You need to map proxyClass - searchCriteria.getClass() here
        // for automatic serialization of the search criteria to the history
        // token, as
        // there is no way to re-generate a class object, that is not an
        // EntityProxy.class, from a token string
        SEARCH_CRITERIA_TYPES_MAP.put(UserProxy.class, UsersSearchCriteriaProxy.class);
    }

    //
    public interface NonPublicProxyFactory extends AutoBeanFactory {
        AutoBean<UsersSearchCriteriaProxy> searchCriteria();
    }

    private NonPublicProxyFactory autoBeanFactory = GWT.create(NonPublicProxyFactory.class);
    private final ApplicationRequestFactory requests;

    /**
     * @param requests
     */
    @Inject
    public ApplicationProxyFactory(ApplicationRequestFactory requests) {
        super();
        this.requests = requests;
    }

    public Class<?> getSearchCriteriaTypeForProxy(Class<? extends EntityProxy> proxyType) {
        return SEARCH_CRITERIA_TYPES_MAP.get(proxyType);
    };

    /**
     * 
     * @param proxyType
     * @return
     */
    public <T extends BaseProxy> T create(Class<T> proxyType) {
        AutoBean<T> autoBean = autoBeanFactory.create(proxyType);
        allocateId(autoBean);
        return autoBean.as();
    }

    public <T extends BaseProxy> T clone(T proxy) {
        Class proxyType = getAutoBean(proxy).getType();
        return (T) deserialize(proxyType, serialize(proxy));
    }

    /**
     * Serialized an object into a string.
     * 
     * It only provides serialization out-of-the-box for {@link BaseProxy}
     * objects. You may need to provide your own serialization for your custom
     * types.
     * 
     * @param object
     * @return
     */
    public String serialize(Object object) {
        if (object instanceof BaseProxy) {
            return serializeProxy((BaseProxy) object);
        }
        // Provide here your own serialization
        return "";
    }

    /**
     * Deserializes a string, previously serialized by
     * {@link #serialize(Object)}, into its object form.
     * 
     * It only provides out-of-the-box deserialization for configured
     * {@link BaseProxy} objects. You may need to provide your own serialization
     * for your custom types.
     * 
     * @param type
     * @param key
     * @return
     */
    public Object deserialize(Class<?> type, String key) {
        if (String.class.equals(type)) {
            return key;
        } else if (autoBeanFactory.create(type) != null) {
            return deserializeProxy((Class<BaseProxy>) type, key);
        }
        // Provide here your own serialization
        return null;
    }

    /**
     * 
     * @param proxy
     * @return
     */
    public <T extends BaseProxy> String serializeProxy(T proxy) {
        try {
            String serialized = AutoBeanCodex.encode(getAutoBean(proxy)).getPayload();
            return base64encode(serialized);
        } catch (Exception e) {
            e.printStackTrace();// for dev mode
            return "";
        }
    }

    /**
     * 
     * @param proxyType
     * @param key
     * @return
     */
    public <T extends BaseProxy> T deserializeProxy(Class<T> proxyType, String key) {
        try {
            T proxy = AutoBeanCodex.decode(autoBeanFactory, proxyType, base64decode(key)).as();
            allocateId((AutoBean) getAutoBean(proxy));
            return proxy;
        } catch (Exception e) {
            e.printStackTrace();// for dev mode
            return null;
        }
    }

    public <T extends BaseProxy> AutoBean<T> allocateId(AutoBean<T> autoBean) {
        Object id = ((AbstractRequestFactory) requests).allocateId(autoBean.getType());
        autoBean.setTag(Constants.STABLE_ID, id);
        return autoBean;
    }

    /**
     * 
     * @param proxy
     * @param frozen
     */
    public <T extends BaseProxy> void setFrozen(T proxy, boolean frozen) {
        getAutoBean(proxy).setFrozen(frozen);
    }

    // these don't work on <= IE8
    // private static native String base64encode(String a) /*-{
    // return window.btoa(a);
    // }-*/;
    //
    // private static native String base64decode(String b) /*-{
    // return window.atob(b);
    // }-*/;

    private static String base64encode(String a) {
        return Base64Coder.encodeString(a);
    };

    private static String base64decode(String b) {
        return Base64Coder.decodeString(b);
    }

}

//
// http://www.source-code.biz/base64coder/java/Base64Coder.java.txt
//
// Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich,
// Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
// EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
// LGPL, GNU Lesser General Public License, V2.1 or later,
// http://www.gnu.org/licenses/lgpl.html
// GPL, GNU General Public License, V2 or later,
// http://www.gnu.org/licenses/gpl.html
// AL, Apache License, V2.0 or later, http://www.apache.org/licenses
// BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
// MIT, MIT License, http://www.opensource.org/licenses/MIT
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

/**
 * A Base64 encoder/decoder.
 *
 * <p>
 * This class is used to encode and decode data in Base64 format as described in
 * RFC 1521.
 *
 * <p>
 * Project home page: <a
 * href="http://www.source-code.biz/base64coder/java/">www.
 * source-code.biz/base64coder/java</a><br>
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
 * Multi-licensed: EPL / LGPL / GPL / AL / BSD / MIT.
 */
class Base64Coder {

    // Mapping table from 6-bit nibbles to Base64 characters.
    private static final char[] map1 = new char[64];
    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++)
            map1[i++] = c;
        for (char c = 'a'; c <= 'z'; c++)
            map1[i++] = c;
        for (char c = '0'; c <= '9'; c++)
            map1[i++] = c;
        map1[i++] = '+';
        map1[i++] = '/';
    }

    // Mapping table from Base64 characters to 6-bit nibbles.
    private static final byte[] map2 = new byte[128];
    static {
        for (int i = 0; i < map2.length; i++)
            map2[i] = -1;
        for (int i = 0; i < 64; i++)
            map2[map1[i]] = (byte) i;
    }

    /**
     * Encodes a string into Base64 format. No blanks or line breaks are
     * inserted.
     * 
     * @param s
     *            A String to be encoded.
     * @return A String containing the Base64 encoded data.
     */
    public static String encodeString(String s) {
        return new String(encode(s.getBytes()));
    }

    /**
     * Encodes a byte array into Base64 format. No blanks or line breaks are
     * inserted in the output.
     * 
     * @param in
     *            An array containing the data bytes to be encoded.
     * @return A character array containing the Base64 encoded data.
     */
    public static char[] encode(byte[] in) {
        return encode(in, 0, in.length);
    }

    /**
     * Encodes a byte array into Base64 format. No blanks or line breaks are
     * inserted in the output.
     * 
     * @param in
     *            An array containing the data bytes to be encoded.
     * @param iLen
     *            Number of bytes to process in <code>in</code>.
     * @return A character array containing the Base64 encoded data.
     */
    public static char[] encode(byte[] in, int iLen) {
        return encode(in, 0, iLen);
    }

    /**
     * Encodes a byte array into Base64 format. No blanks or line breaks are
     * inserted in the output.
     * 
     * @param in
     *            An array containing the data bytes to be encoded.
     * @param iOff
     *            Offset of the first byte in <code>in</code> to be processed.
     * @param iLen
     *            Number of bytes to process in <code>in</code>, starting at
     *            <code>iOff</code>.
     * @return A character array containing the Base64 encoded data.
     */
    public static char[] encode(byte[] in, int iOff, int iLen) {
        int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
        int oLen = ((iLen + 2) / 3) * 4; // output length including padding
        char[] out = new char[oLen];
        int ip = iOff;
        int iEnd = iOff + iLen;
        int op = 0;
        while (ip < iEnd) {
            int i0 = in[ip++] & 0xff;
            int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
            int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
            int o0 = i0 >>> 2;
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            int o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        }
        return out;
    }

    /**
     * Decodes a string from Base64 format. No blanks or line breaks are allowed
     * within the Base64 encoded input data.
     * 
     * @param s
     *            A Base64 String to be decoded.
     * @return A String containing the decoded data.
     * @throws IllegalArgumentException
     *             If the input is not valid Base64 encoded data.
     */
    public static String decodeString(String s) {
        return new String(decode(s));
    }

    /**
     * Decodes a byte array from Base64 format and ignores line separators, tabs
     * and blanks. CR, LF, Tab and Space characters are ignored in the input
     * data. This method is compatible with
     * <code>sun.misc.BASE64Decoder.decodeBuffer(String)</code>.
     * 
     * @param s
     *            A Base64 String to be decoded.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException
     *             If the input is not valid Base64 encoded data.
     */
    public static byte[] decodeLines(String s) {
        char[] buf = new char[s.length()];
        int p = 0;
        for (int ip = 0; ip < s.length(); ip++) {
            char c = s.charAt(ip);
            if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
                buf[p++] = c;
        }
        return decode(buf, 0, p);
    }

    /**
     * Decodes a byte array from Base64 format. No blanks or line breaks are
     * allowed within the Base64 encoded input data.
     * 
     * @param s
     *            A Base64 String to be decoded.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException
     *             If the input is not valid Base64 encoded data.
     */
    public static byte[] decode(String s) {
        return decode(s.toCharArray());
    }

    /**
     * Decodes a byte array from Base64 format. No blanks or line breaks are
     * allowed within the Base64 encoded input data.
     * 
     * @param in
     *            A character array containing the Base64 encoded data.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException
     *             If the input is not valid Base64 encoded data.
     */
    public static byte[] decode(char[] in) {
        return decode(in, 0, in.length);
    }

    /**
     * Decodes a byte array from Base64 format. No blanks or line breaks are
     * allowed within the Base64 encoded input data.
     * 
     * @param in
     *            A character array containing the Base64 encoded data.
     * @param iOff
     *            Offset of the first character in <code>in</code> to be
     *            processed.
     * @param iLen
     *            Number of characters to process in <code>in</code>, starting
     *            at <code>iOff</code>.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException
     *             If the input is not valid Base64 encoded data.
     */
    public static byte[] decode(char[] in, int iOff, int iLen) {
        if (iLen % 4 != 0)
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        while (iLen > 0 && in[iOff + iLen - 1] == '=')
            iLen--;
        int oLen = (iLen * 3) / 4;
        byte[] out = new byte[oLen];
        int ip = iOff;
        int iEnd = iOff + iLen;
        int op = 0;
        while (ip < iEnd) {
            int i0 = in[ip++];
            int i1 = in[ip++];
            int i2 = ip < iEnd ? in[ip++] : 'A';
            int i3 = ip < iEnd ? in[ip++] : 'A';
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            int o0 = (b0 << 2) | (b1 >>> 4);
            int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
            int o2 = ((b2 & 3) << 6) | b3;
            out[op++] = (byte) o0;
            if (op < oLen)
                out[op++] = (byte) o1;
            if (op < oLen)
                out[op++] = (byte) o2;
        }
        return out;
    }

    // Dummy constructor.
    private Base64Coder() {
    }

} // end class Base64Coder