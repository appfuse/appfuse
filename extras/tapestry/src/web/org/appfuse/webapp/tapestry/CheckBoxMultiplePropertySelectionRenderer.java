package org.appfuse.webapp.tapestry;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.form.IMultiplePropertySelectionRenderer;
import org.apache.tapestry.contrib.form.MultiplePropertySelection;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  Implementation of {@link IMultiplePropertySelectionRenderer} that
 *  produces checkbox (&lt;input type=checkbox&gt;) elements with ids
 *  on the labels.
 *
 *  @author Matt Raible
 *
 **/
public class CheckBoxMultiplePropertySelectionRenderer
    implements IMultiplePropertySelectionRenderer {
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
        writer.begin("input");
        writer.attribute("type", "checkbox");
        writer.attribute("name", component.getName());

        String id = component.getName() + "." + model.getValue(index);
        writer.attribute("id", id);
        writer.attribute("value", model.getValue(index));

        if (component.isDisabled()) {
            writer.attribute("disabled", "disabled");
        }

        if (selected) {
            writer.attribute("checked", "checked");
        }

        writer.end();

        writer.println();

        writer.printRaw("&nbsp;");
        writer.begin("label");
        writer.attribute("for", id);
        writer.print(model.getLabel(index));
        writer.end(); // <label>

        writer.printRaw("&nbsp;");

        writer.println();
    }
}
