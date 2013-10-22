<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="updatePassword.title"/></title>
    <meta name="menu" content="UserMenu"/>
</head>
<body id="updatePassword">

<div class="span3">
    <h2><fmt:message key="updatePassword.heading"/></h2>
    <c:choose>
        <c:when test="${not empty token}">
            <p><fmt:message key="updatePassword.passwordReset.message"/></p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="updatePassword.changePassword.message"/></p>
        </c:otherwise>
    </c:choose>
</div>
<div class="span6">

	<form method="post" id="updatePassword" action="<c:url value='/updatePassword'/>" class="well form-horizontal" autocomplete="off">
		
        <fieldset class="control-group">
            <label class="control-label"><fmt:message key="user.username"/></label>
            <div class="controls">
				<input type="text" name="username" id="username" value="<c:out value="${username}"  escapeXml="true" />" required>
            </div>
        </fieldset>

	    <c:choose>
	    	<c:when test="${not empty token}">
			    <input type="hidden" name="token" value="<c:out value="${token}" escapeXml="true" />" />
	    	</c:when>
	    	<c:otherwise>
		        <fieldset class="control-group">
		        	<label class="control-label"><fmt:message key="updatePassword.currentPassword.label"/></label>
		            <div class="controls">
					    <input type="password" name="currentPassword" id="currentPassword" required>
		            </div>
		        </fieldset>
	    	</c:otherwise>
	    </c:choose>

        <fieldset class="control-group">
        	<label class="control-label"><fmt:message key="updatePassword.newPassword.label"/></label>
            <div class="controls">
			    <input type="password" name="password" id="password" required>
            </div>
        </fieldset>
	    
	    <fieldset class="form-actions">
		    <button type="submit" class="btn btn-large btn-primary">
		        <i class="icon-ok icon-white"></i> <fmt:message key='updatePassword.changePasswordButton'/>
		    </button>
	    </fieldset>
	</form>
</div>	

</body>