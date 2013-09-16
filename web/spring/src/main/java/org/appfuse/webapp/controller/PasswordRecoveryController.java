package org.appfuse.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.PasswordRecoveryManager;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple class for password reset functionality.
 * 
 * @author ivangsa
 */
@Controller
@RequestMapping("/updatePassword*")
public class PasswordRecoveryController {
	private final Log log = LogFactory.getLog(PasswordRecoveryController.class);

	private PasswordRecoveryManager passwordRecoveryManager;
	private MessageSource messageSource = null;
	String recoveryUrlTemplate = "/updatePassword?username=%s&token=%s";

	@Autowired
	public void setPasswordRecoveryManager(PasswordRecoveryManager passwordRecoveryManager) {
		this.passwordRecoveryManager = passwordRecoveryManager;
	}

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * 
	 * @param username
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "username", "!token" })
	public String sendRecoveryToken(
			@RequestParam(value = "username", required = true) String username, 
			HttpServletRequest request) 
	{
		boolean sent = passwordRecoveryManager.sendPasswordRecoveryEmail(username, 
				RequestUtil.getAppURL(request) + recoveryUrlTemplate);
		if (sent) {
			saveMessage(request, messageSource.getMessage("updatePassword.recoveryToken.sent", null, request.getLocale()));
		}
		return "redirect:/";
	}

	/**
	 * 
	 * @param username
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "username", "token" })
	public ModelAndView showForm(
			@RequestParam("username") String username,
			@RequestParam("token") String token,
			HttpServletRequest request) 
	{
		if (!passwordRecoveryManager.isRecoveryTokenValid(username, token)) {
			saveMessage(request, messageSource
					.getMessage("updatePassword.invalidToken", null, request.getLocale()));
			
			return new ModelAndView("redirect:/");
		}

		return new ModelAndView("updatePasswordForm").addObject("username", username).addObject("token", token);
	}

	/**
	 * 
	 * @param username
	 * @param token
	 * @param password
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onSubmit(
			@RequestParam("username") String username,
			@RequestParam("token") String token,
			@RequestParam("password") String password,
			HttpServletRequest request) 
	throws Exception
	{

		if (passwordRecoveryManager.isRecoveryTokenValid(username, token)) {

			//update user password
			passwordRecoveryManager.updatePassword(username, token, password, RequestUtil.getAppURL(request));
			saveMessage(request, messageSource
					.getMessage("updatePassword.sucess", new Object[] { username }, request.getLocale()));
			
		} else {
			saveMessage(request, messageSource
					.getMessage("updatePassword.invalidToken", null, request.getLocale()));			
		}
		return new ModelAndView("redirect:/");
	}

	// this method is also in BaseForm Controller
	@SuppressWarnings("unchecked")
	public void saveMessage(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(BaseFormController.MESSAGES_KEY);
		if (messages == null) {
			messages = new ArrayList();
		}
		messages.add(msg);
		request.getSession().setAttribute(BaseFormController.MESSAGES_KEY, messages);
	}
}
