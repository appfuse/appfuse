<%@ include file="/common/taglibs.jsp"%>

<div id="menu">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="FileUpload"/>
    <menu:displayMenu name="FlushCache"/>
    <menu:displayMenu name="Clickstream"/>
</menu:useMenuDisplayer>
</div>
<script type="text/javascript">
    initializeMenus();
</script>