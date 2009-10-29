package org.appfuse.webapp.components;

import java.util.Map;
import java.util.List;
import java.util.Collections;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.services.Request;
import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newList;

/**
 * Multiple select component using checkboxes instead of dropdown list
 * This is inspired from the Palette component
 *
 * @author Serge Eby
 * @version $Id: CheckboxSelect.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class CheckboxSelect extends AbstractField {

    private final class CheckboxSelectModelRenderer implements SelectModelVisitor {
        private final MarkupWriter writer;

        private final ValueEncoder<Object> encoder;

        public CheckboxSelectModelRenderer(final MarkupWriter writer,
                                           ValueEncoder<Object> encoder) {
            this.writer = writer;
            this.encoder = encoder;
        }

        public void beginOptionGroup(OptionGroupModel groupModel) {

        }

        public void endOptionGroup(OptionGroupModel groupModel) {

        }

        @SuppressWarnings("unchecked")
        public void option(OptionModel optionModel) {
            Object optionValue = optionModel.getValue();

            if (encoder == null) {
                throw new TapestryException("value encoder cannot be null",
                        this, null);
            }

            String clientValue = encoder.toClient(optionValue);

            String clientId = getControlName() + "." + clientValue;

            // checkbox
            writer.element("input", "type", "checkbox", "name",
                    getControlName(), "id", clientId, "value", clientValue);

            if (isOptionSelected(optionModel)) {
                writer.attributes("checked", "checked");
            }

            writeDisabled(optionModel.isDisabled());
            writeAttributes(optionModel.getAttributes());

            writer.end(); // ..</input>

            // label
            writer.element("label", "for", clientId);
            writer.write(optionModel.getLabel());
            writer.end(); // ..</label>

        }

        private void writeDisabled(boolean disabled) {
            if (disabled)
                writer.attributes("disabled", "disabled");
        }

        private void writeAttributes(Map<String, String> attributes) {
            if (attributes == null) {
                return;
            }

            for (Map.Entry<String, String> e : attributes.entrySet()) {
                writer.attributes(e.getKey(), e.getValue());
            }
        }

        protected boolean isOptionSelected(OptionModel optionModel) {
            Object value = optionModel.getValue();
            return getSelected().contains(value);
        }

    }

    @Inject
    private Request request;

    @Parameter(required = true)
    private SelectModel model;

    @Parameter(required = true)
    private ValueEncoder<Object> encoder;

    @Parameter(required = true)
    private List<Object> selected;

    /**
     * Defaults the selected parameter to a container property whose name
     * matches this component's id.
     */
    final Binding defaultSelected() {
        return createDefaultParameterBinding("selected");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processSubmission(String elementName) {

        String[] primaryKeys = request.getParameters(elementName);

        List<Object> selected = this.selected;
        if (selected == null) {
            selected = newList();
        } else {
            selected.clear();
        }

        ValueEncoder encoder = this.encoder;

        if (primaryKeys != null) {
            for (String value : primaryKeys) {
                Object objectValue = encoder.toValue(value);
                selected.add(objectValue);
            }
        }
        this.selected = selected;

    }

    @BeforeRenderTemplate
    void options(MarkupWriter writer) {
        SelectModelVisitor renderer = new CheckboxSelectModelRenderer(writer,
                encoder);
        if (model == null) {
            throw new TapestryException("select model cannot be null", this,
                    null);
        }
        model.visit(renderer);
    }

    List<Object> getSelected() {
        if (selected == null) {
            return Collections.emptyList();
        }
        return selected;
    }

}
