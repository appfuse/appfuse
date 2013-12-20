<% if (request.getAttribute("struts.valueStack") != null) { %>
<%-- ActionError Messages - usually set in Actions --%>
<c:if test="${not empty actionErrors}">
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <s:iterator value="actionErrors">
            <s:property/><br/>
        </s:iterator>
    </div>
</c:if>

<%-- FieldError Messages - usually set by validation rules --%>
<c:if test="${not empty fieldErrors}">
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <s:iterator value="fieldErrors">
            <s:iterator value="value">
                <s:property/><br/>
            </s:iterator>
        </s:iterator>
    </div>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty messages}">
    <div class="alert alert-success alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}"/><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<% } else { %>

<%-- Error Messages (on JSPs, not through Struts --%>
<c:if test="${not empty errors}">
    <div class="alert alert-danger alert-dismissable">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/><br/>
        </c:forEach>
    </div>
    <c:remove var="errors" scope="session"/>
</c:if>
<% } %>