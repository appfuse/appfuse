package org.appfuse.webapp.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.webapp.listener.StartupListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to reload the drop-downs initialized in the
 * StartupListener.
 *
 * <p>
 * <a href="ReloadController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/admin/reload*")
public class ReloadController {
    private transient final Log log = LogFactory.getLog(ReloadController.class);

    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'execute' method");
        }

        StartupListener.setupContext(request.getSession().getServletContext());

        String referer = request.getHeader("Referer");

        if (referer != null) {
            log.info("reload complete, reloading user back to: " + referer);
            List<String> messages = (List) request.getSession().getAttribute(BaseFormController.MESSAGES_KEY);

            if (messages == null) {
                messages = new ArrayList();
            }

            messages.add("Reloading options completed successfully.");
            request.getSession().setAttribute(BaseFormController.MESSAGES_KEY, messages);

            response.sendRedirect(response.encodeRedirectURL(referer));
            return null;
        } else {
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Context Reloaded</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Context Reload Succeeded! Click OK to continue.');");
            out.println("history.back();");
            out.println("</script>");
            out.println("</body>");
            out.println("</html>");
        }

        return null;
    }
    
}
