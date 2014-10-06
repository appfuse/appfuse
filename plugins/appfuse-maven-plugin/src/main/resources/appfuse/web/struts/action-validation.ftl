<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!DOCTYPE validators PUBLIC "-//Apache Struts//XWork Validator 1.0.3//EN"
        "http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">
<validators>
    <field name="${pojoNameLower}">
        <field-validator type="visitor">
            <param name="appendPrefix">false</param>
            <message/>
        </field-validator>
    </field>
</validators> 