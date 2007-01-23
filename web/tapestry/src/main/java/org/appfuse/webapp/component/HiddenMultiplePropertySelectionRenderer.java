package org.appfuse.webapp.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.form.IMultiplePropertySelectionRenderer;
import org.apache.tapestry.contrib.form.MultiplePropertySelection;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  Implementation of {@link IMultiplePropertySelectionRenderer} that
 *  produces hidden fields.  This is designed so some users can see checkboxes
 *  and others can't.
 *
 *  @author Matt Raible
 *
 **/
public class HiddenMultiplePropertySelectionRenderer
    implements IMultiplePropertySelectionRenderer {
    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     *  Writes the &lt;table&gt; element.
     *
     **/
    public void beginRender(MultiplePropertySelection component,
                            IMarkupWriter writer, IRequestCycle cycle) {
    }

    /**
     *  Closes the &lt;table&gt; element.
     *
     **/
    public void endRender(MultiplePropertySelection component,
                          IMarkupWriter writer, IRequestCycle cycle) {
    }

    public void renderOption(MultiplePropertySelection component,
                             IMarkupWriter writer, IRequestCycle cycle,
                             IPropertySelectionModel model, Object option,
                             int index, boolean selected) {
        if (selected) {
            writer.begin("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", component.getName());
    
            String id = component.getName() + "." + model.getValue(index);
            writer.attribute("id", id);
            writer.attribute("value", model.getValue(index));
            writer.end();
            writer.print(model.getLabel(index));
            //log.debug("optionCount: " + model.getOptionCount());
            //log.debug("index: " + index);
            if (index < (model.getOptionCount()-2)) {
                writer.printRaw(", ");
            }
            writer.println();
        }
    }
}
