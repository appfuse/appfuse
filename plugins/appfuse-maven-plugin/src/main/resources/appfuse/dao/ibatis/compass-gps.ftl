<!-- CompassGps allows to perform index operation based on the select statements defined in iBatis -->
    <bean id="compassGps" class="org.compass.gps.impl.SingleCompassGps" init-method="start" destroy-method="stop">
        <property name="compass" ref="compass"/>
        <property name="gpsDevices">
            <list>
                <bean class="org.compass.gps.device.ibatis.SqlMapClientGpsDevice">
                    <property name="name" value="ibatis"/>
                    <property name="sqlMapClient" ref="sqlMapClient"/>
                    <property name="selectStatementsIds">
                        <list>
                            <value>getUsers</value>
                        </list>
                    </property>
                </bean>
            </list>
        </property>
    </bean>

    <!-- Add new DAOs here -->