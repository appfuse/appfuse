<%@ include file="/common/taglibs.jsp"%>

<meta name="menu" content="AdminMenu"/>

<f:view>

<h:form id="reloadForm">
    <h:commandLink action="#{reload.execute}" id="execute">
        <f:param name="referrer" value=""/>
    </h:commandLink>
</h:form>

<script type="text/javascript">
    var f = document.forms['reloadForm'];
    f.elements['reloadForm:_link_hidden_'].value='reloadForm:execute';
    f.elements['referrer'].value=document.referrer;
    f.submit();
</script>

</f:view>
