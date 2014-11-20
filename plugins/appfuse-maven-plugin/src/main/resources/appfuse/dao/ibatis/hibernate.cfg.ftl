<!-- The following doctype doesn't work when offline, so default to old one, which does work.
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">-->
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <mapping class="${appfusepackage}.model.User"/>
        <mapping class="${appfusepackage}.model.Role"/>
    </session-factory>
</hibernate-configuration>
