package org.appfuse.webapp.modules;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.compatibility.Compatibility;
import org.apache.tapestry5.services.compatibility.Trait;

public class EnableJQueryModule {

    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void switchProviderToJQuery(MappedConfiguration<String, Object> configuration) {
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
    }

    @Contribute(Compatibility.class)
    public static void disableScriptaculous(MappedConfiguration<Trait, Boolean> configuration) {
        configuration.add(Trait.INITIALIZERS, false);
    }
}
