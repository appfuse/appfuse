package org.appfuse.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * Implementation of <strong>Action</strong> that switches to different
 * layouts based on certain conditions:
 *
 * <ul>
 * <li>condition 1: if "print=true" passed in, used printLayout</li>
 * </ul>
 *
 * <p>
 * <a href="SwitchLayoutAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Matt Raible
 */
public final class SwitchLayoutAction extends BaseAction {

    //~ Methods ================================================================

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        boolean print =
            Boolean.valueOf(request.getParameter("print")).booleanValue();

        // see if a print parameter is passed in the request
        if (print) {
            log.debug("switching base layout to printing...");

            return mapping.findForward("printLayout");
        } else {
            return mapping.findForward("baseLayout");
        }
    }
}
