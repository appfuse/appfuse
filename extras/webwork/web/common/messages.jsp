<%-- ActionError Messages - usually set in Actions --%>
<ww:if test="hasActionErrors()">
    <div class="error fade-ffff00" id="errorMessages">	
      <ww:iterator value="actionErrors">
			    <img src="<c:url value="/images/iconWarning.gif"/>"
              alt="<fmt:message key="icon.warning"/>" class="icon" />
          <ww:property/><br />
      </ww:iterator>
   </div>
</ww:if>

<%-- FieldError Messages - usually set by validation rules --%>
<ww:if test="hasFieldErrors()">
    <div class="error fade-ffff00" id="errorMessages">	
      <ww:iterator value="fieldErrors">
          <ww:iterator value="value">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
             <ww:property/><br />
          </ww:iterator>
      </ww:iterator>
   </div>
</ww:if>

<%-- Success Messages --%>
<c:if test="${not empty messages}">
    <div class="message fade-ffff00" id="successMessages">	
        <c:forEach var="msg" items="${messages}">
            <img src="<c:url value="/images/iconInformation.gif"/>"
                alt="<fmt:message key="icon.information"/>" class="icon" />
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
