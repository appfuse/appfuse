<%@ include file="/common/taglibs.jsp" %>
<c:if test="${not empty errors}">
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/><br />
        </c:forEach>
    </div>
    <c:remove var="errors"/>
</c:if>

<c:if test="${not empty messages}">
    <div class="alert alert-success alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
