<%@ include file="/common/taglibs.jsp"%>

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
