[#ftl]
[#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)]
    <!--${pojo.shortName}-START-->
    <!-- runs ${pojoNameLower}-related tests -->
    <target name="${pojo.shortName}Tests"
        depends="Search${util.getPluralForWord(pojo.shortName)},Edit${pojo.shortName},Save${pojo.shortName},Add${pojo.shortName},Delete${pojo.shortName}"
        description="Call and executes all ${pojoNameLower} test cases (targets)">
        <echo>Successfully ran all ${pojo.shortName} UI tests!</echo>
    </target>

    <!-- Verify the ${util.getPluralForWord(pojoNameLower)} list screen displays without errors -->
    <target name="Search${util.getPluralForWord(pojo.shortName)}" description="Tests searching for ${util.getPluralForWord(pojoNameLower)}">
        <webtest name="search${util.getPluralForWord(pojo.shortName)}">
            &config;
            <steps>
                [#if hasSecurity]&login;[/#if]
                <invoke description="click View ${pojo.shortName} link" url="/${util.getPluralForWord(pojoNameLower)}"/>
                <verifytitle description="we should see the ${pojoNameLower}List title"
                    text=".*${'$'}{${pojoNameLower}List.title}.*" regex="true"/>
                <setinputfield description="set search term" name="searchForm:q" value="*"/>
                <clickbutton label="${'$'}{button.search}" description="Click Search"/>
                <verifytitle description="we should see the ${pojoNameLower}List title"
                    text=".*${'$'}{${pojoNameLower}List.title}.*" regex="true"/>
            </steps>
        </webtest>
    </target>

    <!-- Verify the edit ${pojoNameLower} screen displays without errors -->
    <target name="Edit${pojo.shortName}" description="Tests editing an existing ${pojo.shortName}'s information">
        <webtest name="edit${pojo.shortName}">
            &config;
            <steps>
                [#if hasSecurity]&login;[/#if]
                <invoke description="View ${pojo.shortName} List" url="/${util.getPluralForWord(pojoNameLower)}"/>
                <clicklink label="-1"/>
                <verifytitle description="we should see the ${pojoNameLower}Detail title"
                    text=".*${'$'}{${pojoNameLower}Detail.title}.*" regex="true"/>
            </steps>
        </webtest>
    </target>

    <!-- Edit a ${pojoNameLower} and then save -->
    <target name="Save${pojo.shortName}" description="Tests editing and saving a ${pojoNameLower}">
        <webtest name="save${pojo.shortName}">
            &config;
            <steps>
                [#if hasSecurity]&login;[/#if]
                <invoke description="View ${pojo.shortName} List" url="/${util.getPluralForWord(pojoNameLower)}"/>
                <clicklink label="-1"/>
                <verifytitle description="we should see the ${pojoNameLower}Detail title"
                    text=".*${'$'}{${pojoNameLower}Detail.title}.*" regex="true"/>

                <!-- update some of the required fields -->
    [#foreach field in pojo.getAllPropertiesIterator()]
        [#if !field.equals(pojo.identifierProperty)]
        [#foreach column in field.getColumnIterator()]
            [#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)]
                [#if field.value.typeName == "boolean" || field.value.typeName = "java.lang.Boolean"]
                <!--setCheckbox description="set ${field.name}" name="${pojoNameLower}Form:${field.name}" value="${data.getValueForWebTest(column)}"/-->
                [#else]
                <setInputField description="set ${field.name}" name="${pojoNameLower}Form:${field.name}" value="${data.getValueForWebTest(column)}"/>
                [/#if]
            [/#if]
        [/#foreach]
        [/#if]
    [/#foreach]

                <clickbutton label="${'$'}{button.save}" description="Click Save"/>
                <verifytitle description="Page re-appears if save successful"
                    text=".*${'$'}{${pojoNameLower}Detail.title}.*" regex="true"/>
                <verifytext description="verify success message" text="${'$'}{${pojoNameLower}.updated}"/>
            </steps>
        </webtest>
    </target>

    <!-- Add a new ${pojo.shortName} -->
    <target name="Add${pojo.shortName}" description="Adds a new ${pojo.shortName}">
        <webtest name="add${pojo.shortName}">
            &config;
            <steps>
                [#if hasSecurity]&login;[/#if]
                <invoke description="View ${pojo.shortName} List" url="/${util.getPluralForWord(pojoNameLower)}"/>
                <clickbutton description="Click the 'Add' button" label="${'$'}{button.add}"/>
                <verifytitle description="we should see the ${pojoNameLower}Detail title"
                    text=".*${'$'}{${pojoNameLower}Detail.title}.*" regex="true"/>

                <!-- enter required fields -->
    [#foreach field in pojo.getAllPropertiesIterator()]
        [#if !field.equals(pojo.identifierProperty)]
        [#foreach column in field.getColumnIterator()]
            [#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)]
                [#if field.value.typeName == "boolean" || field.value.typeName = "java.lang.Boolean"]
                <!--setCheckbox description="set ${field.name}" name="${pojoNameLower}Form:${field.name}" value="${data.getValueForWebTest(column)}"/-->
                [#else]
                <setInputField description="set ${field.name}" name="${pojoNameLower}Form:${field.name}" value="${data.getValueForWebTest(column)}"/>
                [/#if]
            [/#if]
        [/#foreach]
        [/#if]
    [/#foreach]

                <clickbutton label="${'$'}{button.save}" description="Click button 'Save'"/>
                <verifytitle description="${pojo.shortName} List appears if save successful"
                    text=".*${'$'}{${pojoNameLower}List.title}.*" regex="true"/>
                <verifytext description="verify success message" text="${'$'}{${pojoNameLower}.added}"/>
            </steps>
        </webtest>
    </target>

    <!-- Delete existing ${pojoNameLower} -->
    <target name="Delete${pojo.shortName}" description="Deletes existing ${pojo.shortName}">
        <webtest name="delete${pojo.shortName}">
            &config;
            <steps>
                [#if hasSecurity]&login;[/#if]
                <invoke description="View ${pojo.shortName} List" url="/${util.getPluralForWord(pojoNameLower)}"/>
                <clicklink label="2"/>
                <clickbutton label="${'$'}{button.delete}" description="Click button 'Delete'"/>
                <verifytitle description="display ${pojo.shortName} List" text=".*${'$'}{${pojoNameLower}List.title}.*" regex="true"/>
                <verifytext description="verify success message" text="${'$'}{${pojoNameLower}.deleted}"/>
            </steps>
        </webtest>
    </target>
    <!--${pojo.shortName}-END-->
</project>
