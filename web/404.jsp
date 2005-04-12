<%@ include file="/common/taglibs.jsp"%>

<page:applyDecorator name="default">

<title><fmt:message key="404.title"/></title>
<content tag="heading"><fmt:message key="404.title"/></content>

<p>
    <fmt:message key="404.message">
        <fmt:param><c:url value="/mainMenu.html"/></fmt:param>
    </fmt:message>
</p>
<p style="text-align: center; margin-top: 20px">
    <a href="http://community.webshots.com/photo/87848122/87848260vtOXvy"
        title="Emerald Lake - Western Canada, click to Zoom In">
    <img style="border: 0"
        src="<c:url value="/images/404.jpg"/>"
        alt="Emerald Lake - Western Canada" /></a>
</p>
</page:applyDecorator>