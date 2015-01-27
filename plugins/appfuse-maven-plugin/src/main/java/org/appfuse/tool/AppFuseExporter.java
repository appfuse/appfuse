package org.appfuse.tool;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.GenericExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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

            // DAO Bean Definition - only used when genericCore == true
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

        String daoFramework = getProperties().getProperty("daoframework");

        // iBATIS SQL Map files
        if (daoFramework.equals("ibatis")) {
            configureExporter("appfuse/dao/ibatis/sql-map-config.ftl",
                "src/main/resources/{class-name}-sql-map-config.xml").start();
            configureExporter("appfuse/dao/ibatis/sql-map.ftl",
                "src/main/resources/sqlmaps/{class-name}SQL.xml").start();
            configureExporter("appfuse/dao/ibatis/compass-gps.ftl",
                "src/main/resources/compass-gps.xml").start();
            configureExporter("appfuse/dao/ibatis/select-ids.ftl",
                "src/main/resources/{class-name}-select-ids.xml").start();
        }

        // Manager Bean Definition - only used when genericCore == true
        configureExporter("appfuse/service/manager-bean.ftl", "src/main/resources/{class-name}Manager-bean.xml").start();
    }

    private void generateWeb() {
        String packaging = getProperties().getProperty("packaging");
        boolean isAppFuse = Boolean.valueOf(getProperties().getProperty("isAppFuse"));
        boolean webProject = packaging != null && packaging.equalsIgnoreCase("war");

        if (!webProject) return;

        String webFramework = getProperties().getProperty("webframework");
        Resource jwebUnitTemplate = new ClassPathResource("appfuse/web/" + webFramework + "/jwebunit-tests.ftl");
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
        } else if (webFramework.equalsIgnoreCase("spring") || webFramework.equalsIgnoreCase("spring-security") || webFramework.equalsIgnoreCase("spring-freemarker")) {
            // tests
            configureExporter("appfuse/web/spring/controller-test.ftl", "src/test/java/{basepkg-name}/webapp/controller/{class-name}ControllerTest.java").start();
            configureExporter("appfuse/web/spring/formcontroller-test.ftl", "src/test/java/{basepkg-name}/webapp/controller/{class-name}FormControllerTest.java").start();

            // controllers
            configureExporter("appfuse/web/spring/controller.ftl", "src/main/java/{basepkg-name}/webapp/controller/{class-name}Controller.java").start();
            configureExporter("appfuse/web/spring/formcontroller.ftl", "src/main/java/{basepkg-name}/webapp/controller/{class-name}FormController.java").start();

            // views
            if (!webFramework.equalsIgnoreCase("spring-freemarker")) {
                configureExporter("appfuse/web/spring/list-view.ftl", "src/main/webapp/WEB-INF/pages/{class-name}s.jsp").start();
                configureExporter("appfuse/web/spring/form-view.ftl", "src/main/webapp/WEB-INF/pages/{class-name}form.jsp").start();
            } else {
                configureExporter("appfuse/web/spring-freemarker/list-view.ftl", "src/main/webapp/{class-name}list.ftl").start();
                configureExporter("appfuse/web/spring-freemarker/form-view.ftl", "src/main/webapp/{class-name}form.ftl").start();
            }

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

            GenericExporter exporter = configureExporter("appfuse/web/struts/struts.ftl", "src/main/resources/{class-name}-struts.xml");
            exporter.getProperties().put("pagesPath", isAppFuse ? "/WEB-INF/pages" : "");
            exporter.start();

            // validation
            configureExporter("appfuse/web/struts/model-validation.ftl", "src/main/resources/{basepkg-name}/model/{class-name}-validation.xml").start();
            configureExporter("appfuse/web/struts/action-validation.ftl", "src/main/resources/{basepkg-name}/webapp/action/{class-name}Action-validation.xml").start();
        } else if (webFramework.equalsIgnoreCase("stripes")) {
            // tests
            configureExporter("appfuse/web/stripes/list-action-test.ftl", "src/test/java/{basepkg-name}/webapp/action/{class-name}ListBeanTest.java").start();
            configureExporter("appfuse/web/stripes/form-action-test.ftl", "src/test/java/{basepkg-name}/webapp/action/{class-name}FormBeanTest.java").start();

            // actions
            configureExporter("appfuse/web/stripes/list-action.ftl", "src/main/java/{basepkg-name}/webapp/action/{class-name}ListBean.java").start();
            configureExporter("appfuse/web/stripes/form-action.ftl", "src/main/java/{basepkg-name}/webapp/action/{class-name}FormBean.java").start();

            // views
            configureExporter("appfuse/web/stripes/list-view.ftl", "src/main/webapp/{class-name}List.jsp").start();
            configureExporter("appfuse/web/stripes/form-view.ftl", "src/main/webapp/{class-name}Form.jsp").start();
        } else if (webFramework.equalsIgnoreCase("tapestry")) {
            // tests
            configureExporter("appfuse/web/tapestry/list-test.ftl", "src/test/java/{basepkg-name}/webapp/pages/{class-name}ListTest.java").start();
            configureExporter("appfuse/web/tapestry/form-test.ftl", "src/test/java/{basepkg-name}/webapp/pages/{class-name}FormTest.java").start();

            // pages
            configureExporter("appfuse/web/tapestry/list.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}List.java").start();
            configureExporter("appfuse/web/tapestry/form.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}Form.java").start();

            // views
            configureExporter("appfuse/web/tapestry/list-view.ftl", "src/main/webapp/{class-name}List.tml").start();
            configureExporter("appfuse/web/tapestry/form-view.ftl", "src/main/webapp/{class-name}Form.tml").start();
        } else if (webFramework.equalsIgnoreCase("wicket")) {
            // tests
            configureExporter("appfuse/web/wicket/list-test.ftl", "src/test/java/{basepkg-name}/webapp/pages/{class-name}ListTest.java").start();
            configureExporter("appfuse/web/wicket/form-test.ftl", "src/test/java/{basepkg-name}/webapp/pages/{class-name}FormTest.java").start();

            // pages
            configureExporter("appfuse/web/wicket/model.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}Model.java").start();
            configureExporter("appfuse/web/wicket/data-provider.ftl", "src/main/java/{basepkg-name}/webapp/pages/Sortable{class-name}DataProvider.java").start();
            configureExporter("appfuse/web/wicket/list.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}List.java").start();
            configureExporter("appfuse/web/wicket/form.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}Form.java").start();

            // views
            configureExporter("appfuse/web/wicket/list-view.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}List.html").start();
            configureExporter("appfuse/web/wicket/form-view.ftl", "src/main/java/{basepkg-name}/webapp/pages/{class-name}Form.html").start();
        } else {
            log.warn("Your project's web framework '" + webFramework + "' is not supported by AMP at this time.");
            log.warn("See enter an issue in JIRA if you'd like to see support. http://issues.appfuse.org/browse/APF");
        }

        // menu
        if (!webFramework.equalsIgnoreCase("tapestry")) {
            configureExporter("appfuse/web/menu.ftl", "src/main/webapp/common/{class-name}-menu.jsp").start();
            if (webFramework.equalsIgnoreCase("spring-freemarker")) {
                configureExporter("appfuse/web/menu-light-freemarker.ftl", "src/main/webapp/common/{class-name}-menu-light.ftl").start();
            } else {
                configureExporter("appfuse/web/menu-light.ftl", "src/main/webapp/common/{class-name}-menu-light.jsp").start();
            }
            configureExporter("appfuse/web/menu-config.ftl", "src/main/webapp/WEB-INF/{class-name}-menu-config.xml").start();
        } else {
            configureExporter("appfuse/web/tapestry/menu.ftl", "src/main/webapp/{class-name}-menu.tml").start();
        }

        // i18n
        configureExporter("appfuse/web/ApplicationResources.ftl", "src/main/resources/{class-name}-ApplicationResources.properties").start();

        // canoo tests
        if (!webFramework.equals("spring-security") && !webFramework.equalsIgnoreCase("spring-freemarker") && !webFramework.equalsIgnoreCase("stripes")) {
            configureExporter("appfuse/web/" + webFramework + "/web-tests.ftl", "src/test/resources/{class-name}-web-tests.xml").start();
        }
        // jwebunit tests
        if (webFramework.equalsIgnoreCase("spring-freemarker")) {
            webFramework = "spring";
            jwebUnitTemplate = new ClassPathResource("appfuse/web/" + webFramework + "/jwebunit-tests.ftl");
        }
        if (jwebUnitTemplate.exists()) {
            configureExporter("appfuse/web/" + webFramework + "/jwebunit-tests.ftl", "src/test/java/{basepkg-name}/webapp/{class-name}WebTest.java").start();
        }
    }

    private String getDaoFilename(String daoFramework) {
        if (daoFramework.equalsIgnoreCase("ibatis")) {
            return "iBatis";
        } else if (daoFramework.equalsIgnoreCase("jpa")) {
            return "Jpa";
        } else {
            return Character.toUpperCase(daoFramework.charAt(0)) + daoFramework.substring(1);
        }
    }

    private GenericExporter configureExporter(String template, String pattern) {

        // Add custom template path if specified
        String[] templatePaths;
        if (getProperties().getProperty("templatedirectory") != null) {
            templatePaths = new String[getTemplatePaths().length + 1];
            templatePaths[0] = getProperties().getProperty("templatedirectory");
            if (getTemplatePaths().length > 1) {
                for (int i = 1; i < getTemplatePaths().length; i++) {
                    templatePaths[i] = getTemplatePaths()[i-1];
                }
            }
        } else {
            templatePaths = getTemplatePaths();
        }

        GenericExporter exporter = new GenericExporter(getConfiguration(), getOutputDirectory()) {
            @Override
            protected void exportPOJO(Map map, POJOClass element) {
                if (element.getShortName().equals(System.getProperty("appfuse.entity"))) {
                    super.exportPOJO(map, element);
                }
            }

            @Override
            protected String resolveFilename(POJOClass element) {
                String filename = super.resolveFilename(element);
                String packageLocation = getPackageNameForFile(element).replace(".", "/");

                String pojoName = System.getProperty("entity");
                if (pojoName == null) {
                    pojoName = System.getProperty("appfuse.entity");
                }

                // A dot in the entity name means the person is specifying the package.
                if (pojoName.contains(".")) {
                    packageLocation = pojoName.substring(0, pojoName.indexOf(".model"));
                    packageLocation = packageLocation.replace(".", "/");
                }

                if (packageLocation.endsWith("model") && packageLocation.indexOf('/') > -1) {
                    packageLocation = packageLocation.substring(0, packageLocation.lastIndexOf('/'));
                }
                filename = filename.replace("{basepkg-name}", packageLocation);
                return filename;
            }
        };
        exporter.setProperties((Properties) getProperties().clone());
        exporter.setTemplatePath(templatePaths);
        exporter.setTemplateName(template);
        exporter.setFilePattern(pattern);
        exporter.setArtifactCollector(getArtifactCollector());
        exporter.getProperties().put("data", new DataHelper());
        exporter.getProperties().put("util", new StringUtils());

        return exporter;
    }
}
