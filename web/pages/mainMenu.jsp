<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="mainMenu.title"/></title>
<content tag="heading"><fmt:message key="mainMenu.heading"/></content>

<fmt:message key="mainMenu.message"/>

<div class="separator"></div>

<ul class="glassList">
    <li>
        <html:link forward="editProfile">
            <fmt:message key="menu.user"/>
        </html:link>
    </li>
    <li>
        <html:link forward="selectFile">
            <fmt:message key="menu.selectFile"/>
        </html:link>
    </li>
</ul>
