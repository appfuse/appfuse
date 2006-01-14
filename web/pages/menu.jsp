<%@ include file="/common/taglibs.jsp"%>

<div id="topMenu">
<menu:useMenuDisplayer name="CoolMenu" permissions="rolesAdapter">
    <menu:displayMenu name="MainMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="FileUpload"/>
    <menu:displayMenu name="AdminMenu"/>
</menu:useMenuDisplayer>
</div>