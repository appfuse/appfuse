<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="mainMenu.title"/></title>
<content tag="heading"><fmt:message key="mainMenu.heading"/></content>

<f:view>
<f:loadBundle var="text" basename="#{basePage.bundleName}"/>
<h:form id="editUser">

<fmt:message key="mainMenu.message"/>

<div class="separator"></div>

<ul class="glassList">
    <li>
        <h:commandLink value="#{text['menu.user']}" action="#{userForm.edit}"/>
    </li>
    <li>
        <a href="<c:url value="/selectFile.html"/>"><fmt:message key="menu.selectFile"/></a>
    </li>
</ul>

</h:form>
</f:view>
