<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
</head>

<c:choose>
    <c:when test="${param.from == 'list'}">
        <fmt:message key="userProfile.admin.message"/>
    </c:when>
    <c:otherwise>
        <fmt:message key="userProfile.message"/>
    </c:otherwise>
</c:choose>

<div class="separator"></div>

<jsp:include page="/userForm.jsp"/>

