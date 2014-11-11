package org.appfuse.webapp.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptModuleConfiguration;
import org.apache.tapestry5.services.javascript.ModuleManager;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.AppFuseSymbolConstants;
import org.appfuse.webapp.data.FileData;
import org.appfuse.webapp.modules.EnableJQueryModule;
import org.appfuse.webapp.services.impl.*;
import org.slf4j.Logger;

import java.io.IOException;


/**
 * Application global configurations
 *
 * @author Serge Eby
 * @version $Id: AppModule.java 5 2008-08-30 09:59:21Z serge.eby $
 */
@ImportModule({EnableJQueryModule.class})
public class AppModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(SecurityContext.class, SpringSecurityContext.class);
        binder.bind(CountryService.class, CountryServiceImpl.class);
        binder.bind(EmailService.class, EmailServiceImpl.class);
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES,
            "de,en,es,fr,it,ko,nl,no,pt_BR,pt,tr,zh_CN,zh_TW,en_US");

        // Turn off GZip Compression since it causes issues with SiteMesh
        configuration.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, "false");

        // The factory default is true but during the early stages of an application
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

        // Maximum upload size is 2MB (size is in bytes)
        configuration.add(UploadSymbols.FILESIZE_MAX, "2048000");

        // HHAC recommended for better security as of Tapestry 5.3.6
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "AppFuse Tapestry is Great");

        // Workaround for Bootstrap buttons tied together
        // See thread on SO here: http://stackoverflow.com/questions/9689584/buttons-run-together-in-bootstrap-2-0-1
        // Commented out as this break the unit tests
        // configuration.add(SymbolConstants.COMPRESS_WHITESPACE, "false");

        // Spring Security
        configuration.add(AppFuseSymbolConstants.SECURITY_URL, "/j_security_check");

        // Combine JS
        configuration.add(SymbolConstants.COMBINE_SCRIPTS, "true");

    }

    @Contribute(ValueEncoderSource.class)
    public static void provideEncoders(
        MappedConfiguration<Class, ValueEncoderFactory> configuration,
        UserManager userManager,
        RoleManager roleManager) {

        contributeEncoder(configuration, User.class, new UserEncoder(userManager));
        contributeEncoder(configuration, Role.class, new RoleEncoder(roleManager));
        contributeEncoder(configuration, FileData.class, new FileDataEncoder());
    }

    private static <T> void contributeEncoder(MappedConfiguration<Class, ValueEncoderFactory> configuration,
                                              Class<T> clazz, final ValueEncoder<T> encoder) {

        ValueEncoderFactory<T> factory = new ValueEncoderFactory<T>() {

            public ValueEncoder<T> create(Class<T> clazz) {
                return encoder;
            }
        };

        configuration.add(clazz, factory);
    }


    /**
     * Decorate Error page
     *
     * @param logger
     * @param renderer
     * @param componentSource
     * @param productionMode
     * @param service
     * @return
     */
    public RequestExceptionHandler decorateRequestExceptionHandler(
        final Logger logger, final ResponseRenderer renderer,
        final ComponentSource componentSource,
        @Symbol(SymbolConstants.PRODUCTION_MODE)
        boolean productionMode,
        Object service) {

        if (!productionMode) {
            return null;
        }

        return new RequestExceptionHandler() {
            public void handleRequestException(Throwable exception) throws IOException {
                logger.error("Unexpected runtime exception: " + exception.getMessage(), exception);
                ExceptionReporter error = (ExceptionReporter) componentSource.getPage("Error");
                error.reportException(exception);
                renderer.renderPageMarkupResponse("Error");
            }
        };
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add("webjars", "META-INF/resources/webjars");
        configuration.add("modules", "META-INF/modules");
    }

    @Contribute(ModuleManager.class)
    public static void setupModule(MappedConfiguration<String, Object> configuration,
                                   @Path("META-INF/modules/app/login.js") Resource appLogin) {
        configuration.add("app/login", new JavaScriptModuleConfiguration(appLogin)
            .dependsOn("jquery").dependsOn("t5/core/console"));
    }
}
