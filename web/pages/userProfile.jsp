<%@ include file="/common/taglibs.jsp"%>

<!-- You could also add a message to the baseLayout and not use this page. -->
<c:choose>
    <c:when test="${param.from == 'list'}">
        <fmt:message key="userProfile.admin.message"/>
    </c:when>
    <c:otherwise>
        <fmt:message key="userProfile.message"/>
    </c:otherwise>
</c:choose>

<div class="separator"></div>

<jsp:include page="/WEB-INF/pages/userForm.jsp" flush="true"/>
