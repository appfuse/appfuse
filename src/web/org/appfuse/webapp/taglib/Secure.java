package org.appfuse.webapp.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.webapp.util.SslUtil;


/**
 * This tag library is designed to be used on a JSP
 * to switch HTTP -> HTTPS protocols and vise versa.
 *
 * If you want to force the page to be viewed in SSL,
 * then you would do something like this:<br /><br />
 * <pre>
 * &lt;tag:secure /&gt;
 * or
 * &lt;tag:secure mode="secured" /&gt;
 * </pre>
 * If you want the force the page to be viewed in
 * over standard http, then you would do something like:<br />
 * <pre>
 * &lt;tag:secure mode="unsecured" /&gt;
 * </pre>
 * @jsp.tag name="secure"
 *          bodycontent="empty"
 *
 * @author <a href="mailto:jon.lipsky@xesoft.com">Jon Lipsky</a>
 *
 * Contributed by:
 *
 * XEsoft GmbH
 * Oskar-Messter-Strasse 18
 * 85737 Ismaning, Germany
 * http://www.xesoft.com
 */
public class Secure extends BodyTagSupport {
    //~ Static fields/initializers =============================================

    public static final String MODE_SECURED = "secured";
    public static final String MODE_UNSECURED = "unsecured";
    public static final String MODE_EITHER = "either";

    //~ Instance fields ========================================================

    private Log log = LogFactory.getLog(Secure.class);
    protected String TAG_NAME = "Secure";
    private String mode = MODE_SECURED;
    private String httpPort = null;
    private String httpsPort = null;

    //~ Methods ================================================================

    /**
     * Sets the mode attribute. This is included in the tld file.
     *
     * @jsp.attribute
     *     description="The mode attribute (secure | unsecured)"
     *     required="false"
     *     rtexprvalue="true"
     */
    public void setMode(String aMode) {
        mode = aMode;
    }

    public int doStartTag() throws JspException {
        // get the port numbers from the application context
        Map config =
            (HashMap) pageContext.getServletContext().getAttribute(Constants.CONFIG);

        httpPort = (String) config.get(Constants.HTTP_PORT);

        if (httpPort == null) {
            httpPort = SslUtil.STD_HTTP_PORT;
        }

        httpsPort = (String) config.get(Constants.HTTPS_PORT);

        if (httpsPort == null) {
            httpsPort = SslUtil.STD_HTTPS_PORT;
        }

        return SKIP_BODY;
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        if (mode.equalsIgnoreCase(MODE_SECURED)) {
            if (pageContext.getRequest().isSecure() == false) {
                String vQueryString =
                    ((HttpServletRequest) pageContext.getRequest()).getQueryString();
                String vPageUrl =
                    ((HttpServletRequest) pageContext.getRequest()).getRequestURI();
                String vServer = pageContext.getRequest().getServerName();

                StringBuffer vRedirect = new StringBuffer("");
                vRedirect.append("https://");
                vRedirect.append(vServer + ":" + httpsPort + vPageUrl);

                if (vQueryString != null) {
                    vRedirect.append("?");
                    vRedirect.append(vQueryString);
                }

                if (log.isDebugEnabled()) {
                    log.debug("attempting to redirect to: " + vRedirect);
                }

                try {
                    ((HttpServletResponse) pageContext.getResponse()).sendRedirect(vRedirect.toString());

                    return SKIP_PAGE;
                } catch (Exception exc2) {
                    throw new JspException(exc2.getMessage());
                }
            }
        } else if (mode.equalsIgnoreCase(MODE_UNSECURED)) {
            if (pageContext.getRequest().isSecure() == true) {
                String vQueryString =
                    ((HttpServletRequest) pageContext.getRequest()).getQueryString();
                String vPageUrl =
                    ((HttpServletRequest) pageContext.getRequest()).getRequestURI();
                String vServer = pageContext.getRequest().getServerName();

                StringBuffer vRedirect = new StringBuffer("");
                vRedirect.append("http://");
                vRedirect.append(vServer + vPageUrl);

                if (vQueryString != null) {
                    vRedirect.append("?");
                    vRedirect.append(vQueryString);
                }

                try {
                    ((HttpServletResponse) pageContext.getResponse()).sendRedirect(vRedirect.toString());

                    return SKIP_PAGE;
                } catch (Exception exc2) {
                    throw new JspException(exc2.getMessage());
                }
            }
        } else if (mode.equalsIgnoreCase(MODE_EITHER)) {
            return EVAL_PAGE;
        } else {
            throw new JspException("Illegal value for the attribute mode: " +
                                   mode);
        }

        return EVAL_PAGE;
    }
}
