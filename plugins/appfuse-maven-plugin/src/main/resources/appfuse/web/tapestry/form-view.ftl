<head>
    <title><span key="${pojo.shortName.toLowerCase()}Detail.title"/></title>
    <content tag="heading"><span key="${pojo.shortName.toLowerCase()}Detail.heading"/></content>
</head>

<body jwcid="@Body">

<span jwcid="@ShowValidationError" delegate="ognl:delegate"/>
<span jwcid="@ShowMessage"/>

<form jwcid="${pojo.shortName.toLowerCase()}Form">
<input type="hidden" jwcid="@Hidden" value="ognl:${pojo.shortName.toLowerCase()}.id"/>
<ul>
    <li>
        <label class="desc" jwcid="@FieldLabel" field="component:firstNameField"/>
        <input class="text medium" jwcid="firstNameField" type="text" id="firstName"/>
    </li>

    <li>
        <label class="desc" jwcid="@FieldLabel" field="component:lastNameField"/>
        <input class="text medium" jwcid="lastNameField" type="text" id="lastName"/>
    </li>

    <li class="buttonBar button">
        <input type="submit" class="button" jwcid="@Submit" value="message:button.save" id="save" action="listener:save"/>
      <span jwcid="@If" condition="ognl:${pojo.shortName.toLowerCase()}.id != null">
        <input type="submit" class="button" jwcid="@Submit" value="message:button.delete" id="delete" action="listener:delete"
            onclick="form.onsubmit = null; return confirmDelete('${pojo.shortName}')"/>
      </span>
        <input type="submit" class="button" jwcid="@Submit" value="message:button.cancel" id="cancel" action="listener:cancel"
            onclick="form.onsubmit = null"/>
    </li>
</ul>
</form>

<script type="text/javascript">
    Form.focusFirstElement($("${pojo.shortName.toLowerCase()}Form"));
</script>

</body>

