package org.appfuse.webapp.components;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * Copied class from Tapestry's source tree since it
 * doesn't have any visibility.
 */
public class ValidMessages {
    private final static MessageFormatter _formatter = new MessageFormatter(ValidMessages.class);

    static String noDisplayName(IComponent label, IComponent field) {
        return _formatter.format("no-display-name", label.getExtendedId(), field.getExtendedId());
    }

}