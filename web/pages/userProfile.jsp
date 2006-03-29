<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<!-- You could also add a message to the default decorator and not use this page. -->
<c:choose>
    <c:when test="${param.from == 'list'}">
        <p><fmt:message key="userProfile.admin.message"/></p>
    </c:when>
    <c:otherwise>
        <p><fmt:message key="userProfile.message"/></p>
    </c:otherwise>
</c:choose>

<div class="separator"></div>

<jsp:include page="/WEB-INF/pages/userForm.jsp"/>
