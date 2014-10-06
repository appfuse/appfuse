<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/><br />
        </c:forEach>
    </div>
    <c:remove var="errors"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty successMessages}">
    <div class="alert alert-success fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="msg" items="${successMessages}">
            <c:out value="${msg}"/><br />
        </c:forEach>
    </div>
    <c:remove var="successMessages" scope="session"/>
</c:if>
