<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<!DOCTYPE validators PUBLIC "-//OpenSymphony Group//XWork Validator 1.0.2//EN"
    "http://www.opensymphony.com/xwork/xwork-validator-1.0.2.dtd">
<validators>
    <field name="${pojoNameLower}">
        <field-validator type="visitor">
            <param name="appendPrefix">false</param>
            <message/>
        </field-validator>
    </field>
</validators> 