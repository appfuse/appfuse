<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="net.sf.navigator.menu.*,
                 javax.servlet.jsp.jstl.sql.Result,
                 java.util.Map,
                 net.sf.navigator.displayer.MenuDisplayerMapping"%>

<html>
<head>
	<title>Dynamic, Database-driven Menu</title>
    <link rel="stylesheet" type="text/css" media="all"
        href="<c:url value="/styles/default.css"/>" />
    <link rel="stylesheet" type="text/css" media="all"
        href="<c:url value="/styles/menuExpandable.css"/>" />
    <script type="text/javascript"
        src="<c:url value="/scripts/global.js"/>"></script>
    <script type="text/javascript"
        src="<c:url value="/scripts/menuExpandable.js"/>"></script>
</head>

<body>
<div id="header"></div>

<div id="content">
<h1>Database Driven Menu</h1>

<p>This page is designed to show how easy it is create menus from a couple
of database tables.  I'm using JSTL's SQL Tags because they're easy and you
can see all the code that's needed in this one page. This example creates
tables, populates them and then extracts the values to build a menu.  This
logic should probably go into a Servlet, Struts Action or a Servlet Filter to
get the logic of who sees what out of the view.  But this works, so feel free
to copy and/or improve.
</p>


<sql:transaction dataSource="jdbc/appfuse">

    <sql:update>
        DROP TABLE IF EXISTS menu_item
    </sql:update>
    <sql:update>
        CREATE TABLE menu_item (
           id BIGINT not null,
           parent_name VARCHAR(30),
           name VARCHAR(30),
           title VARCHAR(30),
           description VARCHAR(50),
           location VARCHAR(255),
           target VARCHAR(10),
           onclick VARCHAR(100),
           onmouseover VARCHAR(100),
           onmouseout VARCHAR(100),
           image VARCHAR(50),
           altImage VARCHAR(30),
           tooltip VARCHAR(100),
           roles VARCHAR(100),
           page VARCHAR(255),
           width VARCHAR(5),
           height VARCHAR(5),
           forward VARCHAR(50),
           action VARCHAR(50),
           primary key (id)
        )
    </sql:update>

    <sql:update var="updateCount">
        INSERT INTO menu_item 
            (id, name, title)
        VALUES 
            (1,'DatabaseMenu','Database Menu')
    </sql:update>
    <sql:update var="updateCount">
        INSERT INTO menu_item 
            (id, parent_name, name, title, location)
        VALUES 
            (2,'DatabaseMenu','Yahoo','Yahoo Mail','http://mail.yahoo.com')
    </sql:update>   
    <sql:update var="updateCount">
        INSERT INTO menu_item
            (id, parent_name, name, title, location)
        VALUES
            (3,'DatabaseMenu','JavaBlogs','JavaBlogs','http://javablogs.com')
    </sql:update>    
    <sql:update var="updateCount">
        INSERT INTO menu_item
            (id, name, title, location)
        VALUES
            (4,'StandaloneMenu','Standalone Menu','http://raibledesigns.com')
    </sql:update>
    <sql:query var="menus">
        SELECT * FROM menu_item order by id;
    </sql:query>

</sql:transaction>

    <p>
        <strong>DONE:</strong> successfully created menu_items table and
        added the following entries.
    </p>

    <display:table name="${menus.rows}" class="list" style="width: 500px">
        <display:column property="id"/>
        <display:column property="name"/>
        <display:column property="parent_name" title="Parent Name"/>
        <display:column property="title"/>
        <display:column property="location"/>
    </display:table>
    <p>
        If you <a href="?" onclick="toggleDisplay('buildTable'); return false">
        view the source</a> of the code above - you can see
        that it just creates a table and inserts some data.  This table will
        be dropped, re-created and populated every time this page is loaded.
    </p>
    <div id="buildTable" style="display: none; margin-left: 10px; margin-top: 0">
<pre>&lt;sql:transaction dataSource="jdbc/appfuse"&gt;

    &lt;sql:update&gt;
        DROP TABLE IF EXISTS menu_item
    &lt;/sql:update&gt;
    &lt;sql:update&gt;
        CREATE TABLE menu_item (
           id BIGINT not null,
           parent_name varchar(30),
           name VARCHAR(30),
           title VARCHAR(30),
           description VARCHAR(50),
           location VARCHAR(255),
           target VARCHAR(10),
           onclick VARCHAR(100),
           onmouseover VARCHAR(100),
           onmouseout VARCHAR(100),
           image VARCHAR(50),
           altImage VARCHAR(30),
           tooltip VARCHAR(100),
           roles VARCHAR(100),
           page VARCHAR(255),
           width VARCHAR(5),
           height VARCHAR(5),
           forward VARCHAR(50),
           action VARCHAR(50),
           primary key (id)
        )
    &lt;/sql:update&gt;

    &lt;sql:update var="updateCount"&gt;
        INSERT INTO menu_item
            (id, name, title)
        VALUES
            (1,'DatabaseMenu','Database Menu')
    &lt;/sql:update&gt;
    &lt;sql:update var="updateCount"&gt;
        INSERT INTO menu_item
            (id, parent_name, name, title, location)
        VALUES
            (2,'DatabaseMenu','Yahoo','Yahoo Mail','http://mail.yahoo.com')
    &lt;/sql:update&gt;
    &lt;sql:update var="updateCount"&gt;
        INSERT INTO menu_item
            (id, parent_name, name, title, location)
        VALUES
            (3,'DatabaseMenu','JavaBlogs','JavaBlogs','http://javablogs.com')
    &lt;/sql:update&gt;

    &lt;sql:update var="updateCount"&gt;
        INSERT INTO menu_item
            (id, name, title, location)
        VALUES
            (4,'StandaloneMenu','Standalone Menu','http://raibledesigns.com')
    &lt;/sql:update&gt;
    
    &lt;sql:query var="menus"&gt;
        SELECT * FROM menu_item
    &lt;/sql:query&gt;

&lt;/sql:transaction&gt;

    &lt;h2&gt;Current menus in the database&lt;/h2&gt;

    &lt;display:table name="${menus.rows}" class="list" style="width: 500px"&gt;
        &lt;display:column property="id"/&gt;
        &lt;display:column property="name"/&gt;
        &lt;display:column property="parent_name" title="Parent Name"/&gt;
        &lt;display:column property="title"/&gt;
        &lt;display:column property="location"/&gt;
    &lt;/display:table&gt;</pre>
    </div>

    <p>
        Now let's build a Menu definition with this data.
    </p>

    <%
        // I had issues using the existing repository - creating a new one
        // seems to solve the problem.  If you figure out how to use the default
        // Repository and keep your menus from duplicating themselves - please
        // let me know!
        
        MenuRepository repository = new MenuRepository();
        // Get the repository from the application scope - and copy the
        // DisplayerMappings from it.
        MenuRepository defaultRepository = (MenuRepository)
                application.getAttribute(MenuRepository.MENU_REPOSITORY_KEY);
        repository.setDisplayers(defaultRepository.getDisplayers());
        
        Result result = (Result) pageContext.getAttribute("menus");
        Map[] rows = result.getRows();
        for (int i=0; i < rows.length; i++) {
            MenuComponent mc = new MenuComponent();
            Map row = rows[i];
            String name = (String) row.get("name");
            mc.setName(name);
            String parent = (String) row.get("parent_name");
            System.out.println(name + ", parent is: " + parent);
            if (parent != null) {
                MenuComponent parentMenu = repository.getMenu(parent);
                if (parentMenu == null) {
                    System.out.println("parentMenu '" + parent + "' doesn't exist!");
                    // create a temporary parentMenu
                    parentMenu = new MenuComponent();
                    parentMenu.setName(parent);
                    repository.addMenu(parentMenu);
                }

                mc.setParent(parentMenu);
            }
            String title = (String) row.get("title");
            mc.setTitle(title);
            String location = (String) row.get("location");
            mc.setLocation(location);
            repository.addMenu(mc);
        }
        pageContext.setAttribute("repository", repository);
    %>

    <div id="menu" style="position: relative; top: 0; left: 0">
        <menu:useMenuDisplayer name="ListMenu" key="repository"
            bundle="org.apache.struts.action.MESSAGE">
            <menu:displayMenu name="DatabaseMenu"/>
            <menu:displayMenu name="StandaloneMenu"/>
        </menu:useMenuDisplayer>
    </div>

</div>

<div id="footer">
Suggestions or Questions should be addressed to
<a href="mailto:struts-menu-user@lists.sf.net">struts-menu-user@lists.sf.net</a>.
</div>
</body>
</html>
