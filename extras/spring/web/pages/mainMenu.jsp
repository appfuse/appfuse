<%@ include file="/common/taglibs.jsp"%>

<fmt:message key="mainMenu.message"/>

<div class="separator"></div>

<ul class="glassList">
    <li>
        <a href="<c:url value="/editProfile.html"/>"><fmt:message key="menu.user"/></a>
    </li>
    <li>
        <a href="<c:url value="/selectFile.html"/>"><fmt:message key="menu.selectFile"/></a>
    </li>
</ul>
