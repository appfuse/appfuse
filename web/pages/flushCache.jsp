<%@ include file="/common/taglibs.jsp" %>

<title><fmt:message key="flushCache.title"/></title>
<content tag="heading"><fmt:message key="flushCache.heading"/></content>

<cache:flush />
<div class="message" style="font-size: 12px">
    <html:img pageKey="icon.information.img" 
        altKey="icon.information" styleClass="icon"/>
    <fmt:message key="flushCache.message"/>
</div>
<script type="text/javascript">
window.setTimeout("history.back()", 2000);
</script>