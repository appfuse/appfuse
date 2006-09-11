<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="mainMenu.title"/></title>
<meta name="menu" content="MainMenu"/>
<content tag="heading"><fmt:message key="mainMenu.heading"/></content>

<f:view>
<f:loadBundle var="text" basename="#{basePage.bundleName}"/>

<p><fmt:message key="mainMenu.message"/></p>

<div class="separator"></div>

<h:form id="editUser">
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
