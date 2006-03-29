<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
</head>

<c:choose>
    <c:when test="${param.from == 'list'}">
        <p><fmt:message key="userProfile.admin.message"/></p>
    </c:when>
    <c:otherwise>
        <p><fmt:message key="userProfile.message"/></p>
    </c:otherwise>
</c:choose>

<div class="separator"></div>

<jsp:include page="/userForm.jsp"/>

