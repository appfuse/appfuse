<%@ include file="/common/taglibs.jsp"%>

<f:view>

<h:form id="reloadForm">
    <h:commandLink action="#{reload.execute}" id="execute">
        <f:param name="referrer"/>
    </h:commandLink>
</h:form>

<script type="text/javascript">
    var f = document.forms['reloadForm'];
    f.elements['reloadForm:_link_hidden_'].value='reloadForm:execute';
    f.elements['referrer'].value=document.referrer;
    f.submit();
</script>

</f:view>
