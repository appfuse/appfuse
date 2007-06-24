package org.appfuse.tool;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;
import org.hibernate.util.StringHelper;

import java.io.File;
import java.util.Map;
import java.util.Properties;

/**
 * This class is used to process FreeMarker templates and
 * produce files from them.
 *
 * @author mraible
 */
public class AppFuseExporter extends GenericExporter {

    public AppFuseExporter() {
        init();
    }

    public AppFuseExporter(Configuration cfg, File outputdir) {
        super(cfg, outputdir);
        init();
    }

    public void init() {
        super.setArtifactCollector(new NoXMLFormatArtifactCollector());
    }

    public String getName() {
        return "appfuse";
    }

    public void doStart() {
        String generateCore = getProperties().getProperty("generate-core");
        if (generateCore != null && generateCore.equals("true")) {
            generateCore();
        }

        String generateWeb = getProperties().getProperty("generate-web");
        if (!"true".equals(generateCore) && generateWeb != null && generateWeb.equals("true")) {
            generateWeb();
        }

        if (generateCore == null && generateWeb == null) {
            generateCore();
            generateWeb();
        }
    }

    private void generateCore() {
        //noinspection UnnecessaryUnboxing
        boolean genericCore = Boolean.valueOf(getProperties().getProperty("genericcore")).booleanValue();

        // generate sample-data.xml
        configureExporter("appfuse/dao/sample-data.ftl", "src/test/resources/{class-name}-sample-data.xml").start();

        // Encourage use of genericCore - less code to maintain!
        if (genericCore) {
            configureExporter("appfuse/service/generic-beans.ftl", "src/main/resources/{class-name}-generic-beans.xml").start();
        } else {
            // DAO Test
            configureExporter("appfuse/dao/dao-test.ftl", "src/test/java/{basepkg-name}/dao/{class-name}DaoTest.java").start();

            // DAO Interfaces
            configureExporter("appfuse/dao/dao.ftl", "src/main/java/{basepkg-name}/dao/{class-name}Dao.java").start();

            // DAO Bean Definition - // todo with CoC: get rid of need for a bean definition when classes exist
            configureExporter("appfuse/dao/dao-bean.ftl", "src/main/resources/{class-name}Dao-bean.xml").start();

            String daoFramework = getProperties().getProperty("daoframework");

            // DAO Implementation
            configureExporter("appfuse/dao/" + daoFramework + "/dao-impl.ftl",
                    "src/main/java/{basepkg-name}/dao/" + daoFramework + "/{class-name}Dao" +
                            getDaoFilename(daoFramework) + ".java").start();

            // Manager Test
            configureExporter("appfuse/service/manager-test.ftl",
                    "src/test/java/{basepkg-name}/service/impl/{class-name}ManagerImplTest.java").start();

            // Manager Interface
            configureExporter("appfuse/service/manager.ftl",
                    "src/main/java/{basepkg-name}/service/{class-name}Manager.java").start();

            // Manager Implementation
            configureExporter("appfuse/service/manager-impl.ftl",
                    "src/main/java/{basepkg-name}/service/impl/{class-name}ManagerImpl.java").start();
        }

        // Manager Bean Definition - // todo with CoC: get rid of need for a bean definition when classes exist
        configureExporter("appfuse/service/manager-bean.ftl", "src/main/resources/{class-name}Manager-bean.xml").start();
    }

    private void generateWeb() {
        String packaging = getProperties().getProperty("packaging");
        boolean webProject = packaging != null && packaging.equalsIgnoreCase("war");

        if (!webProject) return;

        String webFramework = getProperties().getProperty("webframework");
        if (webFramework.equalsIgnoreCase("jsf")) {
            // tests
            configureExporter("appfuse/web/jsf/list-test.ftl", "src/test/java/{basepkg-name}/webapp/action/{class-name}ListTest.java").start();
            configureExporter("appfuse/web/jsf/form-test.ftl", "src/test/java/{basepkg-name}/webapp/action/{class-name}FormTest.java").start();

            // managed beans
            configureExporter("appfuse/web/jsf/list.ftl", "src/main/java/{basepkg-name}/webapp/action/{class-name}List.java").start();
            configureExporter("appfuse/web/jsf/form.ftl", "src/main/java/{basepkg-name}/webapp/action/{class-name}Form.java").start();

            // views
            configureExporter("appfuse/web/jsf/list-view.ftl", "src/main/webapp/{class-name}s.xhtml").start();
            configureExporter("appfuse/web/jsf/form-view.ftl", "src/main/webapp/{class-name}Form.xhtml").start();

            // configuration
            configureExporter("appfuse/web/jsf/navigation.ftl", "src/main/webapp/WEB-INF/{class-name}-navigation.xml").start();
            configureExporter("appfuse/web/jsf/managed-beans.ftl", "src/main/webapp/WEB-INF/{class-name}-managed-beans.xml").start();
        } else if (webFramework.equalsIgnoreCase("spring")) {
            // tests
            configureExporter("appfuse/web/spring/controller-test.ftl", "src/test/java/{basepkg-name}/webapp/controller/{class-name}ControllerTest.java").start();
            configureExporter("appfuse/web/spring/formcontroller-test.ftl", "src/test/java/{basepkg-name}/webapp/controller/{class-name}FormControllerTest.java").start();

            // controllers
            configureExporter("appfuse/web/spring/controller.ftl", "src/main/java/{basepkg-name}/webapp/controller/{class-name}Controller.java").start();
            configureExporter("appfuse/web/spring/formcontroller.ftl", "src/main/java/{basepkg-name}/webapp/controller/{class-name}FormController.java").start();

            // views
            configureExporter("appfuse/web/spring/list-view.ftl", "src/main/webapp/WEB-INF/pages/{class-name}s.jsp").start();
            configureExporter("appfuse/web/spring/form-view.ftl", "src/main/webapp/WEB-INF/pages/{class-name}form.jsp").start();

            // configuration
            configureExporter("appfuse/web/spring/controller-beans.ftl", "src/main/webapp/WEB-INF/{class-name}-beans.xml").start();

            // validation
            configureExporter("appfuse/web/spring/form-validation.ftl", "src/main/webapp/WEB-INF/{class-name}-validation.xml").start();
        } else if (webFramework.equalsIgnoreCase("struts")) {
            // tests
            configureExporter("appfuse/web/struts/action-test.ftl", "src/test/java/{basepkg-name}/webapp/action/{class-name}ActionTest.java").start();

            // controllers
            configureExporter("appfuse/web/struts/action.ftl", "src/main/java/{basepkg-name}/webapp/action/{class-name}Action.java").start();

            // views
            configureExporter("appfuse/web/struts/list-view.ftl", "src/main/webapp/WEB-INF/pages/{class-name}List.jsp").start();
            configureExporter("appfuse/web/struts/form-view.ftl", "src/main/webapp/WEB-INF/pages/{class-name}Form.jsp").start();

            // configuration
            // This template is not used anymore (APF-798), but retained in case we do want to create definitions by default in the future
            configureExporter("appfuse/web/struts/action-beans.ftl", "src/main/webapp/WEB-INF/{class-name}-struts-bean.xml").start();

            configureExporter("appfuse/web/struts/struts.ftl", "src/main/resources/{class-name}-struts.xml").start();

            // validation
            configureExporter("appfuse/web/struts/model-validation.ftl", "src/main/resources/{basepkg-name}/model/{class-name}-validation.xml").start();
            configureExporter("appfuse/web/struts/action-validation.ftl", "src/main/resources/{basepkg-name}/webapp/action/{class-name}Action-validation.xml").start();
        } else if (webFramework.equalsIgnoreCase("tapestry")) {
            // tests
            configureExporter("appfuse/web/tapestry/list-test.ftl", "src/test/java/{basepkg-name}/webapp/pages/{class-name}ListTest.java").start();
            configureExporter("appfuse/web/tapestry/form-test.ftl", "src/test/java/{basepkg-name}/webapp/pages/{class-name}FormTest.java").start();

            // managed beans
            configureExporter("appfuse/web/tapestry/list.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}List.java").start();
            configureExporter("appfuse/web/tapestry/form.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}Form.java").start();

            // views
            configureExporter("appfuse/web/tapestry/list-view.ftl", "src/main/webapp/WEB-INF/tapestry/{class-name}List.html").start();
            configureExporter("appfuse/web/tapestry/form-view.ftl", "src/main/webapp/WEB-INF/tapestry/{class-name}Form.html").start();

            // configuration
            configureExporter("appfuse/web/tapestry/list-page.ftl", "src/main/webapp/WEB-INF/tapestry/{class-name}List.page").start();
            configureExporter("appfuse/web/tapestry/form-page.ftl", "src/main/webapp/WEB-INF/tapestry/{class-name}Form.page").start();
        }

        // menu
        configureExporter("appfuse/web/menu.ftl", "src/main/webapp/common/{class-name}-menu.jsp").start();
        configureExporter("appfuse/web/menu-config.ftl", "src/main/webapp/WEB-INF/{class-name}-menu-config.xml").start();

        // i18n
        configureExporter("appfuse/web/ApplicationResources.ftl", "src/main/resources/{class-name}-ApplicationResources.properties").start();

        // ui tests
        configureExporter("appfuse/web/" + webFramework + "/web-tests.ftl", "src/test/resources/{class-name}-web-tests.xml").start();
    }

    private String getDaoFilename(String daoFramework) {
        if (daoFramework.equalsIgnoreCase("ibatis")) {
            return "iBatis";
        } else if (daoFramework.equalsIgnoreCase("jpa-hibernate")) {
            return "Jpa";
        } else {
            return Character.toUpperCase(daoFramework.charAt(0)) + daoFramework.substring(1);
        }
    }

    private GenericExporter configureExporter(String template, String pattern) {

        GenericExporter exporter = new GenericExporter(getConfiguration(), getOutputDirectory()) {
            @Override
            protected void exportPOJO(Map map, POJOClass element) {
                if (element.getShortName().contains(System.getProperty("appfuse.entity"))) {
                    super.exportPOJO(map, element);
                }
            }

            @Override
            protected String resolveFilename(POJOClass element) {
                String filename = super.resolveFilename(element);
                String packageLocation = StringHelper.replace(getPackageNameForFile(element), ".", "/");
                if (packageLocation.endsWith("model") && packageLocation.indexOf('/') > -1) {
                    packageLocation = packageLocation.substring(0, packageLocation.lastIndexOf('/'));
                }
                filename = StringHelper.replace(filename, "{basepkg-name}", packageLocation);
                return filename;
            }
        };
        exporter.setProperties((Properties) getProperties().clone());
        exporter.setTemplatePath(getTemplatePaths());
        exporter.setTemplateName(template);
        exporter.setFilePattern(pattern);
        exporter.setArtifactCollector(getArtifactCollector());
        exporter.getProperties().put("data", new DataHelper());

        return exporter;
    }
}
