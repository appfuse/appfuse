package org.appfuse.mojo.appfuse.utility;

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class contains static properties that are used to drive the appfuse mojos. They are the default settings that
 * support the current version of appfuse. The user can choose to override any of these properties however that is
 * discouraged.
 */
public class AppFuseProperties {
    // Beginning of Model parameters.
    /**
     * This is the key in the properties file that indicates the model package extention to be used if the default is
     * not sufficient.
     */
    public static final String MODEL_PACKAGE_EXTENSION_PROPETY_KEY = "appfuse.model.package.extension";

    /**
     * This is the extension that will be added to the base package name to construct the model package name and file
     * location.
     */
    public static final String DEFAULT_MODEL_PACKAGE_EXTENSION = "model";

    /**
     * The property key to set to find the model template name.
     */
    public static final String MODEL_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.model.template.name";

    /**
     * The name of the freemarker template to use to create model objects.
     */
    public static final String MODEL_TEMPLATE_NAME = "appfusepojo/Pojo.ftl";

    /**
     * The property key to set to find the model output pattern.
     */
    public static final String MODEL_OUTPUT_PATTERN_PROPERTY_KEY = "appfuse.model.output.pattern";

    /**
     * The output pattern to use for file output for the model objects.
     */
    public static final String MODEL_OUTPUT_PATTERN = "{class-name}.java";

    // Beginning of DAO parameters.
    /**
     * This is the key in the properties file that indicates the dao package extention to be used if the default is not
     * sufficient.
     */
    public static final String DAO_PACKAGE_EXTENSION_PROPETY_KEY = "appfuse.dao.package.extension";

    /**
     * This is the extension that will be added to the base package name to construct the dao package name and file
     * location.
     */
    public static final String DEFAULT_DAO_PACKAGE_EXTENSION = "dao";

    /**
     * The property key to set to find the dao template name.
     */
    public static final String DAO_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.dao.template.name";

    /**
     * The name of the freemarker template to use to create dao objects.
     */
    public static final String DAO_TEMPLATE_NAME = "appfusedao/daointerface.ftl";

    /**
     * The property key to set to find the dao output pattern.
     */
    public static final String DAO_OUTPUT_PATTERN_PROPERTY_KEY = "appfuse.dao.output.pattern";

    /**
     * The output pattern to use for file output for the dao objects.
     */
    public static final String DAO_OUTPUT_PATTERN = "{class-name}Dao.java";

    // Beginning of Hibernate Dao properties.
    /**
     * This is the key in the properties file that indicates the hibernate dao package extention to be used if the
     * default is not sufficient.
     */
    public static final String HIBERNATE_DAO_PACKAGE_EXTENSION_PROPETY_KEY = "appfuse.hibernate.dao.package.extension";

    /**
     * This is the extension that will be added to the base package name to construct the hibernate dao package name and
     * file location.
     */
    public static final String DEFAULT_HIBERNATE_DAO_PACKAGE_EXTENSION = "dao.hibernate";

    /**
     * The property key to set to find the hibernate dao template name.
     */
    public static final String HIBERNATE_DAO_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.hibernate.dao.template.name";

    /**
     * The name of the freemarker template to use to create hibernate dao objects.
     */
    public static final String HIBERNATE_DAO_TEMPLATE_NAME = "appfusedao/hibernatedao.ftl";

    /**
     * The property key to set to find the hibernate dao output pattern.
     */
    public static final String HIBERNATE_DAO_OUTPUT_PATTERN_PROPERTY_KEY = "appfuse.hibernate.dao.output.pattern";

    /**
     * The output pattern to use for file output for the hibernate dao objects.
     */
    public static final String HIBERNATE_DAO_OUTPUT_PATTERN = "{class-name}DaoHibernate.java";

    // Beginning of hibernate Context parameters.

    /**
     * The property key to set to find the hibernate context template name.
     */
    public static final String HIBERNATE_CONTEXT_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.hibernate.context.template.name";

    /**
     * The name of the freemarker template to use to create hibernate context objects.
     */
    public static final String HIBERNATE_CONTEXT_TEMPLATE_NAME = "appfusedao/hibernatedaocontext.ftl";

    /**
     * The property key to set to find the hibernate context output pattern.
     */
    public static final String HIBERNATE_CONTEXT_OUTPUT_PATTERN_PROPERTY_KEY =
        "appfuse.hibrnate.context.output.pattern";

    /**
     * The output pattern to use for file output for the hibernate context objects.
     */
    public static final String HIBERNATE_CONTEXT_OUTPUT_PATTERN = "{class-name}-persist.xml";

    // Beginning of manager parameters.
    /**
     * This is the key in the properties file that indicates the manager package extention to be used if the default is
     * not sufficient.
     */
    public static final String MANAGER_PACKAGE_EXTENSION_PROPERTY_KEY = "appfuse.manager.package.extension";

    /**
     * This is the extension that will be added to the base package name to construct the manager package name and file
     * location.
     */
    public static final String DEFAULT_MANAGER_PACKAGE_EXTENSION = "service";

    /**
     * The property key to set to find the manager template name.
     */
    public static final String MANAGER_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.manager.template.name";

    /**
     * The name of the freemarker template to use to create manager objects.
     */
    public static final String MANAGER_TEMPLATE_NAME = "appfusemanager/managerinterface.ftl";

    /**
     * The property key to set to find the manager output pattern.
     */
    public static final String MANAGER_OUTPUT_PATTERN_PROPERTY_KEY = "appfuse.manager.output.pattern";

    /**
     * The output pattern to use for file output for the manager objects.
     */
    public static final String MANAGER_OUTPUT_PATTERN = "{class-name}Manager.java";

    // Beginning of manager impl parameters.
    /**
     * This is the key in the properties file that indicates the manager impl package extention to be used if the
     * default is not sufficient.
     */
    public static final String MANAGER_IMPL_PACKAGE_EXTENSION_PROPETY_KEY = "appfuse.manager.impl.package.extension";

    /**
     * This is the extension that will be added to the base package name to construct the manager impl package name and
     * file location.
     */
    public static final String DEFAULT_MANAGER_IMPL_PACKAGE_EXTENSION = "service.impl";

    /**
     * The property key to set to find the manager template name.
     */
    public static final String MANAGER_IMPL_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.manager.impl.template.name";

    /**
     * The name of the freemarker template to use to create manager objects.
     */
    public static final String MANAGER_IMPL_TEMPLATE_NAME = "appfusemanager/managerimpl.ftl";

    /**
     * The property key to set to find the manager impl output pattern.
     */
    public static final String MANAGER_IMPL_OUTPUT_PATTERN_PROPERTY_KEY = "appfuse.manager.impl.output.pattern";

    /**
     * The output pattern to use for file output for the manager impl objects.
     */
    public static final String MANAGER_IMPL_OUTPUT_PATTERN = "{class-name}ManagerImpl.java";

    // Beginning of Manager Context parameters.

    /**
     * The property key to set to find the manager context template name.
     */
    public static final String MANAGER_CONTEXT_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.manager.context.template.name";

    /**
     * The name of the freemarker template to use to create manager context objects.
     */
    public static final String MANAGER_CONTEXT_TEMPLATE_NAME = "appfusemanager/managercontext.ftl";

    /**
     * The property key to set to find the manager context output pattern.
     */
    public static final String MANAGER_CONTEXT_OUTPUT_PATTERN_PROPERTY_KEY = "appfuse.manager.context.output.pattern";

    /**
     * The output pattern to use for file output for the manager context objects.
     */
    public static final String MANAGER_CONTEXT_OUTPUT_PATTERN = "{class-name}-service.xml";

    /**
     * This is the name of the session factory to inject into the dao objects.
     */
    public static final String DEFAULT_SESSION_FACTORY_NAME = "sessionfactory";

    /**
     * This is the property key to locate the session factory name should the user decide to override the default.
     */
    public static final String SESSION_FACTORY_NAME_PROPERTY_KEY = "appfuse.session.factory.name";

    /**
     * The name of the transaction template proxy to inject into each manager object.
     */
    public static final String DEFAULT_TRANSACTION_TEMPLATE_NAME = "txProxyTemplate";

    /**
     * This is the property key to locate the transaction template proxy name in the properties file should the user
     * decide to override the default.
     */
    public static final String TRANSACTION_TEMPLATE_NAME_PROPERTY_KEY = "appfuse.transaction.template.name";

    /**
     * Name of a class to use inside the template to provide useful functions for additional processing.
     */
    public static final String DEFAULT_TEMPLATE_HELPER_CLASS = "org.codehaus.mojo.appfuse.template.Helper";

    /**
     * Key to look up a user supplied helper class in the maven properties. This class must extend the default appfuse
     * helper class.
     */
    public static final String TEMPLATE_HELPER_CLASS_PROPERTY_KEY = "appfuse.template.helper.class";
}
