package org.appfuse.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailSender;
import org.appfuse.service.UserManager;


/**
 * Action class to send password hints to registered users.
 *
 * <p>
 * <a href="PasswordHintAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 * @struts.action path="/passwordHint" validate="false"
 * @struts.action-forward name="previousPage" path="/login.jsp"
 */
public final class PasswordHintAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        MessageResources resources = getResources(request);
        ActionMessages errors = new ActionMessages();
        String userId = request.getParameter("username");

        // ensure that the username has been sent
        if (userId == null) {
            log.warn("Username not specified, notifying user that it's a required field.");

            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.required",
                                         resources.getMessage("userFormEx.username")));
            saveErrors(request, errors);
            return mapping.findForward("previousPage");
        }

        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint...");
        }

        ActionMessages messages = new ActionMessages();

        // look up the user's information
        try {
            UserManager userMgr = (UserManager) getBean("userManager");

            User user = userMgr.getUser(userId);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtils.serverURL(request) +
                       request.getContextPath());

            // From,to,cc,subject,content
            MailSender.sendTextMessage(Constants.DEFAULT_FROM,
                                       user.getEmail(), null,
                                       "Password Hint", msg.toString());
            messages.add(ActionMessages.GLOBAL_MESSAGE,
                         new ActionMessage("login.passwordHint.sent", userId,
                                           user.getEmail()));
            saveMessages(request.getSession(), messages);
        } catch (Exception e) {
            // If exception is expected do not rethrow
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("login.passwordHint.error", userId));
            saveErrors(request, errors);
        }

        return mapping.findForward("previousPage");
    }
}
