<%@ include file="/common/taglibs.jsp" %>

<cache:flush />
<div class="message" style="font-size: 12px">
    <html:img pageKey="icon.information.img" 
        altKey="icon.information" styleClass="icon"/>
    All caches successfully flushed, returning you to your previous page in 2 seconds.
</div>
<script type="text/javascript">
window.setTimeout("history.back()", 2000);
</script>