<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE page-specification PUBLIC
    "-//Apache Software Foundation//Tapestry Specification 4.0//EN"
    "http://jakarta.apache.org/tapestry/dtd/Tapestry_4_0.dtd">

<page-specification>
    <inject property="${pojoNameLower}Manager" type="spring" object="${pojoNameLower}Manager"/>

    <property name="message" persist="flash"/>
    <property name="row"/>

    <bean name="rowsClass" class="org.apache.tapestry.bean.EvenOdd"/>
    <asset name="upArrow" path="/images/arrow_up.png"/>
    <asset name="downArrow" path="/images/arrow_down.png"/>
</page-specification>
