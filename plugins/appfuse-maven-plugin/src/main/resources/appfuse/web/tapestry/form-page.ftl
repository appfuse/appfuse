<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE page-specification PUBLIC
    "-//Apache Software Foundation//Tapestry Specification 4.0//EN"
    "http://jakarta.apache.org/tapestry/dtd/Tapestry_4_0.dtd">

<page-specification>
    <inject property="engineService" object="engine-service:page"/>
    <inject property="request" object="service:tapestry.globals.HttpServletRequest"/>
    <inject property="response" object="service:tapestry.globals.HttpServletResponse"/>
    <inject property="${pojo.shortName.toLowerCase()}Manager" type="spring" object="${pojo.shortName.toLowerCase()}Manager"/>

    <property name="message" persist="flash"/>

    <component id="${pojo.shortName.toLowerCase()}Form" type="Form">
        <binding name="delegate" value="ognl:delegate"/>
        <binding name="clientValidationEnabled" value="true"/>
    </component>

    <component id="firstNameField" type="TextField">
        <binding name="value" value="${pojo.shortName.toLowerCase()}.firstName"/>
        <binding name="validators" value="validators:required"/>
        <binding name="displayName" value="message:${pojo.shortName.toLowerCase()}.firstName"/>
    </component>

    <component id="lastNameField" type="TextField">
        <binding name="value" value="${pojo.shortName.toLowerCase()}.lastName"/>
        <binding name="validators" value="validators:required"/>
        <binding name="displayName" value="message:${pojo.shortName.toLowerCase()}.lastName"/>
    </component>
</page-specification>
