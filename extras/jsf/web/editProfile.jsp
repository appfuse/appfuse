<%@ include file="/common/taglibs.jsp"%>

<%-- This page is used to call the userForm.edit action.
     This serves as a workaround to use Struts Menu with JSF - since
     the menu.jsp cannot contain f:view tags, and hence any JSF tags
--%>
<f:view>

<h:form id="userProfile">
    <h:commandLink action="#{userForm.edit}" id="edit"/>
</h:form>

<script type="text/javascript">
    var f = document.forms['userProfile'];
    f.elements['userProfile:_link_hidden_'].value='userProfile:edit';
    f.submit();
</script>

</f:view>