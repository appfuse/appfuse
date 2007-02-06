package org.appfuse.webapp.pages;

import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.appfuse.model.LabelValue;

public class OptionsModel implements IPropertySelectionModel {
    private List options = null;

    public OptionsModel(List roles) {
        this.options = roles;
    }

    public int getOptionCount() {
        return this.options.size();
    }

    public Object getOption(int index) {
        LabelValue option = (LabelValue) this.options.get(index);

        return option.getValue();
    }

    public String getLabel(int index) {
        LabelValue option = (LabelValue) this.options.get(index);

        return option.getLabel();
    }

    public String getValue(int index) {
        LabelValue option = (LabelValue) this.options.get(index);

        return option.getValue();
    }

    public Object translateValue(String value) {
        return value;
    }
}
