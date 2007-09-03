<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-reverse-engineering
  SYSTEM "http://hibernate.sourceforge.net/hibernate-reverse-engineering-3.0.dtd" >

<hibernate-reverse-engineering>

    <type-mapping>
        <!-- jdbc-type is name fom java.sql.Types -->
        <sql-type jdbc-type="VARCHAR" length='1' hibernate-type="yes_no"/>
        <!-- length, scale and precision can be used to specify the mapping precisly -->
        <sql-type jdbc-type="NUMERIC" precision='1' hibernate-type="boolean"/>
        <!-- the type-mappings are ordered. This mapping will be consulted last,
        thus overriden by the previous one if precision=1 for the column -->
        <sql-type jdbc-type="BIGINT" hibernate-type="java.lang.Long"/>
        <sql-type jdbc-type="INTEGER" hibernate-type="java.lang.Long"/>
        <sql-type jdbc-type="NUMERIC" hibernate-type="java.lang.Long"/>
    </type-mapping>

    <!-- BIN$ is recycle bin tables in Oracle -->
    <table-filter match-name="BIN$.*" exclude="true"/>

    <!-- Exclude AppFuse tables from all catalogs/schemas -->
    <table-filter match-name="app_user" exclude="true"/>
    <table-filter match-name="role" exclude="true"/>
    <table-filter match-name="user_role" exclude="true"/>
</hibernate-reverse-engineering>
