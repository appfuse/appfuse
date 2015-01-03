package org.appfuse.webapp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Used to indicate RPC requests that login is required sending a '401' error,
 * code.
 * 
 * <p>
 * It can be used in combination with
 * {@link org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint}
 * </p>
 * <p>
 * A configuration might look like this:
 * </p>
 *
 * <pre>
 * &lt;bean id=&quot;daep&quot; class=&quot;org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint&quot;&gt;
 *     &lt;constructor-arg&gt;
 *         &lt;map&gt;
 *             &lt;entry key=&quot;hasHeader('Content-Type','application/json; charset=utf-8')&quot; value-ref=&quot;rpcAuthenticationEntryPoint&quot; /&gt;
 *         &lt;/map&gt;
 *     &lt;/constructor-arg&gt;
 *     &lt;property name=&quot;defaultEntryPoint&quot; ref=&quot;defaultEntryPoint&quot;/&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author ivangsa
 *
 */
public class RpcAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

}
