This package is used to generate POJOs from database tables. Unfortunately,
it seems the Middlegen project is no longer active (their website is missing),
so it may be difficult to find Middlegen documentation. When using AppGen,
you are prompted to choose b/w generating from a POJO or a database table.
If you choose table, the build.xml file in this package is executed. You can
also build POJOs with this package standalone by typing "ant" in this directory.

NOTE: This package does not modify applicationContext-dao.xml to add
Hibernate mappings.