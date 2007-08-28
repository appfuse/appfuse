package org.appfuse.webapp.jsf;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.util.StringUtils;

/**
 * Override HtmlGridRendererBase (http://tinyurl.com/oqbxh) so &lt;h:panelGrid&gt; spits out &lt;ul&gt; and &lt;li&gt;
 * instead of &lt;table> and &lt;tr&gt;&lt;td&gt;.
 *
 * @author Matt Raible
 */
public class PanelGridRenderer extends HtmlRenderer {
    private static final Log log = LogFactory.getLog(PanelGridRenderer.class);

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent component)
            throws IOException {
        // all work done in encodeEnd()
    }

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        // all work done in encodeEnd()
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException {
        RendererUtils.checkParamValidity(facesContext, component, UIPanel.class);

        int columns;
        if (component instanceof HtmlPanelGrid) {
            columns = ((HtmlPanelGrid) component).getColumns();
        } else {
            Integer i = (Integer) component.getAttributes().get(org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr.COLUMNS_ATTR);
            columns = i != null ? i.intValue() : 0;
        }

        if (columns <= 0) {
            if (log.isErrorEnabled()) {
                log.error("Wrong columns attribute for PanelGrid " + component.getClientId(facesContext) + ": " + columns);
            }
            columns = 1;
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.UL_ELEM, component);
        HtmlRendererUtils.writeIdIfNecessary(writer, component, facesContext);
        HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.UL_PASSTHROUGH_ATTRIBUTES);

        writer.flush();

        renderChildren(facesContext, writer, component, columns);

        writer.endElement(HTML.UL_ELEM);
    }

    protected void renderChildren(FacesContext context,
                                  ResponseWriter writer,
                                  UIComponent component,
                                  int columns)
            throws IOException {

        String rowClasses;
        if (component instanceof HtmlPanelGrid) {
            rowClasses = ((HtmlPanelGrid) component).getRowClasses();
        } else {
            rowClasses = (String) component.getAttributes().get(JSFAttr.ROW_CLASSES_ATTR);
        }

        String[] rowClassesArray = (rowClasses == null)
                ? org.apache.myfaces.shared_tomahawk.util.ArrayUtils.EMPTY_STRING_ARRAY
                : StringUtils.trim(StringUtils.splitShortString(rowClasses, ','));
        int rowClassesCount = rowClassesArray.length;

        int childCount = getChildCount(component);
        if (childCount > 0) {
            int columnIndex = 0;
            int rowClassIndex = 0;
            boolean rowStarted = false;
            for (Iterator it = getChildren(component).iterator(); it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                if (child.isRendered()) {
                    if (columnIndex == 0) {
                        //start of new/next row
                        if (rowStarted) {
                            //do we have to close the last row?
                            writer.endElement(HTML.LI_ELEM);
                            HtmlRendererUtils.writePrettyLineSeparator(context);
                        }
                        writer.startElement(HTML.LI_ELEM, component);
                        if (rowClassIndex < rowClassesCount) {
                            writer.writeAttribute(HTML.CLASS_ATTR, rowClassesArray[rowClassIndex], null);
                        }
                        rowStarted = true;
                        rowClassIndex++;
                        if (rowClassIndex == rowClassesCount) {
                            rowClassIndex = 0;
                        }
                    }

                    RendererUtils.renderChild(context, child);

                    columnIndex++;
                    if (columnIndex >= columns) {
                        columnIndex = 0;
                    }
                }
            }

            if (rowStarted) {
                writer.endElement(HTML.LI_ELEM);
                HtmlRendererUtils.writePrettyLineSeparator(context);
            }
        }
    }
}
