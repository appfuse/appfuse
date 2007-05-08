    <!-- runs ${pojo.shortName.toLowerCase()}-related tests -->
    <target name="${pojo.shortName}Tests" depends="Search${pojo.shortName}s,Edit${pojo.shortName},Save${pojo.shortName},Add${pojo.shortName},Delete${pojo.shortName}"
        description="Call and executes all ${pojo.shortName.toLowerCase()} test cases (targets)">
        <echo>Successfully ran all ${pojo.shortName} UI tests!</echo>
    </target>

    <!-- Verify the people list screen displays without errors -->
    <target name="Search${pojo.shortName}s" description="Tests search for and displaying all ${pojo.shortName.toLowerCase()}s">
        <webtest name="search${pojo.shortName}s">
            &config;
            <steps>
                &login;
                <invoke description="click View ${pojo.shortName}s link" url="/${pojo.shortName}List.html"/>
                <verifytitle description="we should see the ${pojo.shortName.toLowerCase()}List title"
                    text=".*${'$'}{${pojo.shortName.toLowerCase()}List.title}.*" regex="true"/>
            </steps>
        </webtest>
    </target>

    <!-- Verify the edit ${pojo.shortName.toLowerCase()} screen displays without errors -->
    <target name="Edit${pojo.shortName}" description="Tests editing an existing ${pojo.shortName}'s information">
        <webtest name="edit${pojo.shortName}">
            &config;
            <steps>
                &login;
                <invoke description="View ${pojo.shortName} List" url="/${pojo.shortName}List.html"/>
                <clicklink description="edit first record in list" label="1"/>
                <verifytitle description="we should see the ${pojo.shortName.toLowerCase()}Detail title"
                    text=".*${'$'}{${pojo.shortName.toLowerCase()}Detail.title}.*" regex="true"/>
            </steps>
        </webtest>
    </target>

    <!-- Edit a ${pojo.shortName.toLowerCase()} and then save -->
    <target name="Save${pojo.shortName}" description="Tests editing and saving a user">
        <webtest name="save${pojo.shortName}">
            &config;
            <steps>
                &login;
                <invoke description="View ${pojo.shortName} List" url="/${pojo.shortName}List.html"/>
                <clicklink description="edit first record in list" label="1"/>
                <verifytitle description="we should see the ${pojo.shortName.toLowerCase()}Detail title"
                    text=".*${'$'}{${pojo.shortName.toLowerCase()}Detail.title}.*" regex="true"/>

                <!-- update some of the required fields -->
                <setinputfield description="set firstName" name="firstNameField" value="firstName"/>
                <setinputfield description="set lastName" name="lastNameField" value="lastName"/>
                <clickbutton label="${'$'}{button.save}" description="Click Save"/>

                <verifytitle description="Page re-appears if save successful"
                    text=".*${'$'}{${pojo.shortName.toLowerCase()}Detail.title}.*" regex="true"/>
            </steps>
        </webtest>
    </target>

    <!-- Add a new ${pojo.shortName} -->
    <target name="Add${pojo.shortName}" description="Adds a new ${pojo.shortName}">
        <webtest name="add${pojo.shortName}">
            &config;
            <steps>
                &login;
                <invoke description="Click Add button" url="/${pojo.shortName}Form.html"/>
                <verifytitle description="we should see the ${pojo.shortName.toLowerCase()}Detail title"
                    text=".*${'$'}{${pojo.shortName.toLowerCase()}Detail.title}.*" regex="true"/>

                <!-- enter required fields -->
                <setinputfield description="set firstName" name="firstNameField" value="Canoo"/>
                <setinputfield description="set lastName" name="lastNameField" value="Test"/>
                <clickbutton label="${'$'}{button.save}" description="Click button 'Save'"/>

                <verifytitle description="${pojo.shortName} List appears if save successful"
                    text=".*${'$'}{${pojo.shortName.toLowerCase()}List.title}.*" regex="true"/>
                <verifytext description="verify success message" text="${pojo.shortName.toLowerCase()}.added}"/>
            </steps>
        </webtest>
    </target>

    <!-- Delete existing ${pojo.shortName.toLowerCase()} -->
    <target name="Delete${pojo.shortName}" description="Deletes existing ${pojo.shortName}">
        <webtest name="delete${pojo.shortName}">
            &config;
            <steps>
                &login;
                <invoke description="View ${pojo.shortName} List" url="/${pojo.shortName}List.html"/>
                <clicklink description="delete first record in list" label="1"/>
                <prepareDialogResponse description="Confirm delete" dialogType="confirm" response="true"/>
                <clickbutton label="${'$'}{button.delete}" description="Click button 'Delete'"/>
                <verifyNoDialogResponses/>
                <verifytitle description="display ${pojo.shortName} List" text=".*${'$'}{${pojo.shortName.toLowerCase()}List.title}.*" regex="true"/>
                <verifytext description="verify success message" text="${pojo.shortName.toLowerCase()}.deleted}"/>
            </steps>
        </webtest>
    </target>