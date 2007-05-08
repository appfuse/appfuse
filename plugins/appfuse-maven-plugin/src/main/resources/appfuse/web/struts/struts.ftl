<!--${pojo.shortName}Action-START-->
        <action name="${pojo.shortName.toLowerCase()}s" class="${pojo.shortName.toLowerCase()}Action" method="list">
            <result>/WEB-INF/pages/${pojo.shortName.toLowerCase()}List.jsp</result>
        </action>

        <action name="edit${pojo.shortName}" class="${pojo.shortName.toLowerCase()}Action" method="edit">
            <result>/WEB-INF/pages/${pojo.shortName.toLowerCase()}Form.jsp</result>
            <result name="error">/WEB-INF/pages/${pojo.shortName.toLowerCase()}List.jsp</result>
        </action>

        <action name="save${pojo.shortName}" class="${pojo.shortName.toLowerCase()}Action" method="save">
            <result name="input">/WEB-INF/pages/${pojo.shortName.toLowerCase()}Form.jsp</result>
            <result name="cancel" type="redirect">${pojo.shortName.toLowerCase()}s.html</result>
            <result name="delete" type="redirect">${pojo.shortName.toLowerCase()}s.html</result>
            <result name="success" type="redirect">${pojo.shortName.toLowerCase()}s.html</result>
        </action>
        <!--${pojo.shortName}Action-END-->

        <!-- Add additional Actions here -->