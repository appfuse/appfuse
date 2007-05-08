<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="${pojo.shortName.toLowerCase()}Detail.title"/></title>
<content tag="heading"><fmt:message key="${pojo.shortName.toLowerCase()}Detail.heading"/></content>

<form:form commandName="${pojo.shortName.toLowerCase()}" method="post" action="${pojo.shortName.toLowerCase()}form.html" id="${pojo.shortName.toLowerCase()}Form" onsubmit="return validate${pojo.shortName}(this)">
<form:errors path="*" cssClass="error"/>
<form:hidden path="id"/>
<ul>
    <li>
        <appfuse:label styleClass="desc" key="${pojo.shortName.toLowerCase()}.firstName"/>
        <form:errors path="firstName" cssClass="fieldError"/>
        <form:input path="firstName" id="firstName" cssClass="text medium"/>
    </li>

    <li>
        <appfuse:label styleClass="desc" key="${pojo.shortName.toLowerCase()}.lastName"/>
        <form:errors path="lastName" cssClass="fieldError"/>
        <form:input path="lastName" id="lastName" cssClass="text medium"/>
    </li>

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save" value="<fmt:message key="button.save"/>"/>
        <c:if test="${'$'}{not empty ${pojo.shortName.toLowerCase()}.id}">
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('${pojo.shortName.toLowerCase()}')"
            value="<fmt:message key="button.delete"/>" />
        </c:if>
        <input type="submit" class="button" name="cancel" value="<fmt:message key="button.cancel"/>" onclick="bCancel=true"/>
    </li>
</ul>
</form:form>

<v:javascript formName="${pojo.shortName.toLowerCase()}" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

<script type="text/javascript">
    Form.focusFirstElement($('${pojo.shortName.toLowerCase()}Form'));
</script>
