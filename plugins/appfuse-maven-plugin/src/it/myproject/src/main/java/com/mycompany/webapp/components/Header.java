package com.mycompany.webapp.components;

import com.sun.org.apache.bcel.internal.generic.LoadClass;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.PersistentLocale;

import java.util.Locale;

/**
 * AppFuse Header component
 *
 * @author Serge Eby
 */
public class Header {

    @Inject
    private Locale locale;

    @Inject
    private PersistentLocale persistentLocale;

    public boolean isEnglish() {
         return locale.getLanguage().
               equals(new Locale("en", "", "").getLanguage());
    }

    Object onActionFromSwitch() {
        persistentLocale.set(Locale.ENGLISH);
        return this;
    }
}
