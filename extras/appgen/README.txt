This tool was based on contributions from Lance Lavandowska and Ben Gill.

You can read this easier online at http://raibledesigns.com/wiki/AppGen.html.

By default, AppGen will generate only Actions/Controllers, Action/Controller 
Tests, test data, i18n keys and JSPs. It will also configure Actions/Controllers 
for you. It uses the generic BaseManager and BaseDAOHibernate classes 
(configured as "manager" and "dao") to reduce the number of files that are 
generated. However, I realize that sometimes you will want to generate all the 
DAO and Manager classes (as well as their tests), so I've added that option too. 

To use the AppGen tool (after installing your web framework), perform the 
following steps: 

1. If you're using Hibernate, configure the mapping file for your POJO in 
   applicationContext-hibernate.xml. If you're using iBATIS, you can skip this 
   step.

2. cd into the extras/appgen directory and run "ant". You will be prompted to
   generate from a POJO or a Table. If you choose pojo, the .java file should
   already exist in your "model" package. If you choose table, Middlegen will be
   used to create a POJO from an existing database table. This generates all the
   files you create to test and perform CRUD on an object.
   
3. Finally, you will be asked to enter an application module or sub-package 
   name. This is an optional feature that will allow you to organize your 
   classes into sub-packages. For example, for a POJO "model" package of
   "org.appfuse.foo.model", just enter foo when prompted.

4. To install the generated files, run "ant install". You can run "ant install 
   -Dappgen.type=pojo -Dobject.name=person" if you want to do everything in one
   fell swoop.
   
   WARNING: You might want to backup your project before you do this - or at 
   least make sure it's checked into a source code repository. I've tested this
   code, and it works well - but it *is* modifying your source tree for you.

After generating the view layer, it's up to you to modify the *Form.jsp (*Form.html
for Tapestry) and make it look pretty.This is covered in Step 5 of each respective
web framework's "Create Action/Controller" tutorial.

NOTE: If you'd like to generate all the DAOs/Managers/Tests,
run "ant install-detailed" instead of "ant install". Before you install
anything, the files will be created in the extras/appgen/build/gen directory 
(in case you want to look at them before installing). If you just want to test 
the tool, you can cd to this directory and run "ant test" to see the 
contents of these tutorials created. 

I encourage you to read the tutorials even if you decide to generate all your 
code. That way you'll understand what's being generated for you and you'll 
only need to mailing list for asking smart questions. ;-) Hopefully this tool 
will remove the pain of writing simple CRUD code and let you concentrate on 
developing your business logic and fancy UIs!

For an updated version of this README, look in your docs directory, run "ant
wiki" or just go to the following URL:

http://raibledesigns.com/wiki/AppGen.html
