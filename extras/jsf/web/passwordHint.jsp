<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.passwordHint"/></title>

<f:view>

Looking up password hint for <c:out value="${param.username}"/>...

<h:form id="passwordForm">
<h:inputHidden id="username" value="#{passwordHint.username}"/>

<%-- JSF Hack for the Display Tag, from James Violette --%>
<%-- 1. Create a dummy actionLink, w/ no value         --%>
<h:commandLink action="#{passwordHint.execute}" id="execute">
    <f:param name="username"/>
</h:commandLink>

</h:form>

<%-- 2. Write your own JavaScript function that's easy to call --%>
<script type="text/javascript">
    function submitForm() {
        var f = document.forms['passwordForm'];
        f.elements['passwordForm:_link_hidden_'].value='passwordForm:execute';
        f.elements['username'].value='<c:out value="${param.username}"/>';
        f.submit();
    }
    window.onload=submitForm;
</script>

</f:view>
