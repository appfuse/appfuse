<%@ include file="/common/taglibs.jsp"%>

<div id="menu">
<menu:useMenuDisplayer name="ListMenu" 
    bundle="org.apache.struts.action.MESSAGE"
    permissions="rolesAdapter">
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="FileUpload"/>
    <menu:displayMenu name="FlushCache"/>
    <menu:displayMenu name="Clickstream"/>
</menu:useMenuDisplayer>
</div>