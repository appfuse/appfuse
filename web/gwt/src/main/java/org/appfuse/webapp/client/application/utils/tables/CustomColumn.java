package org.appfuse.webapp.client.application.utils.tables;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;

public abstract class CustomColumn<T, V> extends Column<T, V> {

    private static final SafeHtmlRenderer<Object> SAFE_HTML_RENDERER = new SafeHtmlRenderer<Object>() {
        @Override
        public SafeHtml render(Object object) {
            return (object == null) ? SafeHtmlUtils.EMPTY_SAFE_HTML : SafeHtmlUtils.fromString(object.toString());
        }

        @Override
        public void render(Object object, SafeHtmlBuilder appendable) {
            appendable.append(render(object));
        }
    };

    private SafeHtmlRenderer<V> renderer = (SafeHtmlRenderer<V>) SAFE_HTML_RENDERER;
    private FieldUpdater<T, V> fieldUpdater;
    private String propertyName;
    private boolean sortable = false;

    public CustomColumn(String propertyName) {
        this(propertyName, false, null);
    }

    /**
     * @param headerText
     * @param sortable
     */
    public CustomColumn(String propertyName, boolean sortable) {
        this(propertyName, sortable, null);
    }

    /**
     * @param anchorRenderer
     * @param headerText
     * @param valueUpdater
     * @param sortable
     */
    public CustomColumn(String propertyName, boolean sortable, FieldUpdater<T, V> fieldUpdater) {
        super((Cell) new ClickableTextCell());
        this.fieldUpdater = fieldUpdater;
        this.sortable = sortable;
        this.propertyName = propertyName;

        setSortable(isSortable());
        if (fieldUpdater != null) {
            setFieldUpdater(fieldUpdater);
        }
    }

    public abstract V getValue(T row);

    public void render(Context context, T object, SafeHtmlBuilder sb) {
        renderer.render(getValue(object), sb);
    }

    public FieldUpdater<T, V> getFieldUpdater() {
        return fieldUpdater;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isSortable() {
        return sortable;
    }
}
