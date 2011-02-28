/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.appfuse.webapp.jsp;

/**
 * Handles escaping of characters that could be interpreted as XML markup.
 * <p>The specification for <code>&lt;c:out&gt;</code> defines the following
 * character conversions to be applied:
 * <table rules="all" frame="border">
 * <tr><th>Character</th><th>Character Entity Code</th></tr>
 * <tr><td>&lt;</td><td>&amp;lt;</td></tr>
 * <tr><td>&gt;</td><td>&amp;gt;</td></tr>
 * <tr><td>&amp;</td><td>&amp;amp;</td></tr>
 * <tr><td>&#039;</td><td>&amp;#039;</td></tr>
 * <tr><td>&#034;</td><td>&amp;#034;</td></tr>
 * </table>
 */
public class EscapeXml {
    
    private static final String[] ESCAPES;

    static {
        int size = '>' + 1; // '>' is the largest escaped value
        ESCAPES = new String[size];
        ESCAPES['<'] = "&lt;";
        ESCAPES['>'] = "&gt;";
        ESCAPES['&'] = "&amp;";
        ESCAPES['\''] = "&#039;";
        ESCAPES['"'] = "&#034;";
    }

    private static String getEscape(char c) {
        if (c < ESCAPES.length) {
            return ESCAPES[c];
        } else {
            return null;
        }
    }
    
    /**
     * Escape a string.
     * 
     * @param src
     *            the string to escape; must not be null
     * @return the escaped string
     */
    public static String escape(String src) {
        // first pass to determine the length of the buffer so we only allocate once
        int length = 0;
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            String escape = getEscape(c);
            if (escape != null) {
                length += escape.length();
            } else {
                length += 1;
            }
        }

        // skip copy if no escaping is needed
        if (length == src.length()) {
            return src;
        }

        // second pass to build the escaped string
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            String escape = getEscape(c);
            if (escape != null) {
                buf.append(escape);
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}
