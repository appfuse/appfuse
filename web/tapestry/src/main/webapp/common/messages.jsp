<%-- Error Messages --%>
<c:if test="${not empty error}">
    <div class="error" id="errorMessages">
        <img src="<c:url value="/images/iconWarning.gif"/>"
            alt="<fmt:message key="icon.warning"/>" class="icon" />
        <c:out value="${error}"/>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty message}">
    <div class="message" id="successMessages">
        <img src="<c:url value="/images/iconInformation.gif"/>"
            alt="<fmt:message key="icon.information"/>" class="icon" />
        <c:out value="${message}"/>
    </div>
    <c:remove var="message" scope="session"/>
</c:if>
