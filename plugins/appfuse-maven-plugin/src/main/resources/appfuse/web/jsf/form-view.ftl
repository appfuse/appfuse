<html xmlns="http://www.w3.org/1999/xhtml" xmlns:c="http://java.sun.com/jstl/core"
      xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets" xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:v="http://corejsf.com/validator">

<f:view>
<f:loadBundle var="text" basename="${'#'}{${pojo.shortName.toLowerCase()}Form.bundleName}"/>
    <head>
        <title>${'#'}{text['${pojo.shortName.toLowerCase()}Detail.title']}</title>
        <meta name="heading" content="${'#'}{text['${pojo.shortName.toLowerCase()}Detail.heading']}"/>
        <meta name="menu" content="${pojo.shortName}Menu"/>
    </head>
<body id="${pojo.shortName.toLowerCase()}Form">

<h:form id="${pojo.shortName.toLowerCase()}Form" onsubmit="return validate${pojo.shortName}Form(this)">
<h:inputHidden value="${'#'}{${pojo.shortName.toLowerCase()}Form.${pojo.shortName.toLowerCase()}.id}" id="id"/>

<h:panelGrid columns="3">
    <h:outputLabel styleClass="desc" for="firstName" value="${'#'}{text['${pojo.shortName.toLowerCase()}.firstName']}"/>
    <h:inputText styleClass="text medium" id="firstName" value="${'#'}{${pojo.shortName.toLowerCase()}Form.${pojo.shortName.toLowerCase()}.firstName}" required="true">
        <v:commonsValidator type="required" arg="${'#'}{text['${pojo.shortName.toLowerCase()}.firstName']}"/>
    </h:inputText>
    <t:message for="firstName" styleClass="fieldError"/>

    <h:outputLabel styleClass="desc" for="lastName" value="${'#'}{text['${pojo.shortName.toLowerCase()}.lastName']}"/>
    <h:inputText styleClass="text medium" id="lastName" value="${'#'}{${pojo.shortName.toLowerCase()}Form.${pojo.shortName.toLowerCase()}.lastName}" required="true">
        <v:commonsValidator type="required" arg="${'#'}{text['${pojo.shortName.toLowerCase()}.lastName']}"/>
    </h:inputText>
    <t:message for="lastName" styleClass="fieldError"/>

    <h:panelGroup styleClass="buttonBar bottom">
        <h:commandButton value="${'#'}{text['button.save']}" action="${'#'}{${pojo.shortName.toLowerCase()}Form.save}" id="save" styleClass="button"/>

        <c:if test="${'$'}{not empty ${pojo.shortName.toLowerCase()}Form.${pojo.shortName.toLowerCase()}.id}">
        <h:commandButton value="${'#'}{text['button.delete']}" action="${'#'}{${pojo.shortName.toLowerCase()}Form.delete}"
            id="delete" styleClass="button" onclick="bCancel=true; return confirmDelete('${pojo.shortName}')"/>
        </c:if>

        <h:commandButton value="${'#'}{text['button.cancel']}" action="cancel" immediate="true"
            id="cancel" styleClass="button" onclick="bCancel=true"/>
    </h:panelGroup>
    <h:outputText/><h:outputText/>
</h:panelGrid>
</h:form>

<v:validatorScript functionName="validate${pojo.shortName}Form"/>

<script type="text/javascript">
    //Form.focusFirstElement($('${pojo.shortName.toLowerCase()}Form'));
    highlightFormElements();
</script>

</body>
</f:view>
</html>
