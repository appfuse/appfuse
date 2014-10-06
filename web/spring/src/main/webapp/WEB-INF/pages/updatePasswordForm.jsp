<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="updatePassword.title"/></title>
    <meta name="menu" content="UserMenu"/>
</head>
<body id="updatePassword">

<div class="col-sm-3">
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
<div class="col-sm-5">
	<form method="post" id="updatePassword" action="<c:url value='/updatePassword'/>" class="well" autocomplete="off">
		
        <div class="form-group">
            <label class="control-label"><fmt:message key="user.username"/></label>
            <input type="text" name="username" class="form-control" id="username" value="<c:out value="${username}" escapeXml="true"/>" required>
        </div>

	    <c:choose>
	    	<c:when test="${not empty token}">
			    <input type="hidden" name="token" value="<c:out value="${token}" escapeXml="true" />"/>
	    	</c:when>
	    	<c:otherwise>
		        <div class="form-group">
		        	<label class="control-label"><fmt:message key="updatePassword.currentPassword.label"/></label>
                    <input type="password" class="form-control" name="currentPassword" id="currentPassword" required autofocus>
		        </div>
	    	</c:otherwise>
	    </c:choose>

        <div class="form-group">
        	<label class="control-label"><fmt:message key="updatePassword.newPassword.label"/></label>
            <input type="password" class="form-control" name="password" id="password" required>
        </div>
	    
	    <div class="form-group">
		    <button type="submit" class="btn btn-large btn-primary">
		        <i class="icon-ok icon-white"></i> <fmt:message key='updatePassword.changePasswordButton'/>
		    </button>
	    </div>
	</form>
</div>	

</body>