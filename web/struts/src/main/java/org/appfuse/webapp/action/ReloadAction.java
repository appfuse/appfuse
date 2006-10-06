package org.appfuse.webapp.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.appfuse.webapp.listener.StartupListener;

import org.apache.struts2.ServletActionContext;

/**
 * This class is used to reload the drop-downs initialized in the
 * StartupListener.
 *
 * <p>
 * <a href="ReloadAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class ReloadAction extends BaseAction {
    private static final long serialVersionUID = 295460450224891051L;

    public String execute() throws IOException {
        StartupListener.setupContext(getSession().getServletContext());

        String referer = getRequest().getHeader("Referer");
        HttpServletResponse response = ServletActionContext.getResponse();
        
        if (referer != null) {
            log.info("reload complete, reloading user back to: " + referer);     
            saveMessage(getText("reload.succeeded"));
            response.sendRedirect(response.encodeRedirectURL(referer));
            return SUCCESS;
        } else {
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Context Reloaded</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Reloading options succeeded! Click OK to continue.');");
            out.println("history.back();");
            out.println("</script>");
            out.println("</body>");
            out.println("</html>");
        }

        return SUCCESS;
    }
}
