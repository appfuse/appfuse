<%@ include file="/common/taglibs.jsp"%>

<page:applyDecorator name="default">

<title><fmt:message key="403.title"/></title>
<content tag="heading"><fmt:message key="403.title"/></content>

<p>
    <fmt:message key="403.message">
        <fmt:param><c:url value="/"/></fmt:param>
    </fmt:message>
</p>
<p style="text-align: center; margin-top: 20px">
    <a href="http://community.webshots.com/photo/56793801/56801692jkyHaR"
        title="Hawaii, click to Zoom In">
    <img style="border: 0" 
        src="<c:url value="/images/403.jpg"/>" 
        alt="Hawaii" /></a>
</p>
</page:applyDecorator>