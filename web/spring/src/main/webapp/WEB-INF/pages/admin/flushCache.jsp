<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="flushCache.title"/></title>
    <meta name="heading" content="<fmt:message key='flushCache.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<cache:flush/>
<div class="message">
    <img src="<c:url value="/images/iconInformation.gif"/>"
        alt="<fmt:message key="icon.information"/>" class="icon" />
    <fmt:message key="flushCache.message"/>
</div>
<script type="text/javascript">
    window.setTimeout("history.back()", 2000);
</script>