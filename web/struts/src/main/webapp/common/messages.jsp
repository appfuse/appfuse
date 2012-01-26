<% if (request.getAttribute("struts.valueStack") != null) { %>
<%-- ActionError Messages - usually set in Actions --%>
<s:if test="hasActionErrors()">
    <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <s:iterator value="actionErrors">
            <s:property/><br/>
        </s:iterator>
    </div>
</s:if>

<%-- FieldError Messages - usually set by validation rules --%>
<s:if test="hasFieldErrors()">
    <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <s:iterator value="fieldErrors">
            <s:iterator value="value">
                <s:property/><br/>
            </s:iterator>
        </s:iterator>
    </div>
</s:if>

<%-- Success Messages --%>
<c:if test="${not empty messages}">
    <div class="alert alert-success fade in">
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
    <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/><br/>
        </c:forEach>
    </div>
    <c:remove var="errors" scope="session"/>
</c:if>
<% } %>