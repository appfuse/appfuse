package org.appfuse.webapp.taglib;

import org.appfuse.model.LabelValue;
import org.displaytag.tags.el.ExpressionEvaluator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Tag for creating multiple &lt;select&gt; options for displaying a list of
 * country names.
 *
 * <p>
 * <b>NOTE</b> - This tag requires a Java2 (JDK 1.2 or later) platform.
 * </p>
 *
 * @author Jens Fischer, Matt Raible
 * @version $Revision: 1.6 $ $Date: 2006/07/15 11:57:20 $
 *
 * @jsp.tag name="country" bodycontent="empty"
 */
public class CountryTag extends TagSupport {
    private static final long serialVersionUID = 3905528206810167095L;
    private String name;
    private String prompt;
    private String scope;
    private String selected;

    /**
     * @param name The name to set.
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param prompt The prompt to set.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @param selected The selected option.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setDefault(String selected) {
        this.selected = selected;
    }

    /**
     * Property used to simply stuff the list of countries into a
     * specified scope.
     *
     * @param scope
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setToScope(String scope) {
        this.scope = scope;
    }

    /**
     * Process the start of this tag.
     *
     * @return int status
     *
     * @exception JspException if a JSP exception has occurred
     *
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);

        if (selected != null) {
            selected = eval.evalString("default", selected);
        }

        Locale userLocale = pageContext.getRequest().getLocale();
        List<LabelValue> countries = this.buildCountryList(userLocale);

        if (scope != null) {
            if (scope.equals("page")) {
                pageContext.setAttribute(name, countries);
            } else if (scope.equals("request")) {
                pageContext.getRequest().setAttribute(name, countries);
            } else if (scope.equals("session")) {
                pageContext.getSession().setAttribute(name, countries);
            } else if (scope.equals("application")) {
                pageContext.getServletContext().setAttribute(name, countries);
            } else {
                throw new JspException("Attribute 'scope' must be: page, request, session or application");
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("<select name=\"").append(name).append("\" id=\"").append(name).append("\" class=\"form-control\">\n");

            if (prompt != null) {
                sb.append("    <option value=\"\" selected=\"selected\">");
                sb.append(eval.evalString("prompt", prompt)).append("</option>\n");
            }

            for (Iterator<LabelValue> i = countries.iterator(); i.hasNext();) {
                LabelValue country = i.next();
                sb.append("    <option value=\"").append(country.getValue()).append("\"");

                if ((selected != null) && selected.equals(country.getValue())) {
                    sb.append(" selected=\"selected\"");
                }

                sb.append(">").append(country.getLabel()).append("</option>\n");
            }

            sb.append("</select>");

            try {
                pageContext.getOut().write(sb.toString());
            } catch (IOException io) {
                throw new JspException(io);
            }
        }

        return super.doStartTag();
    }

    /**
     * Release aquired resources to enable tag reusage.
     *
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        super.release();
    }

    /**
     * Build a List of LabelValues for all the available countries. Uses
     * the two letter uppercase ISO name of the country as the value and the
     * localized country name as the label.
     *
     * @param locale The Locale used to localize the country names.
     *
     * @return List of LabelValues for all available countries.
     */
    @SuppressWarnings("unchecked")
    protected List<LabelValue> buildCountryList(Locale locale) {
        final String EMPTY = "";
        final Locale[] available = Locale.getAvailableLocales();

        List<LabelValue> countries = new ArrayList<>();

        for (int i = 0; i < available.length; i++) {
            final String iso = available[i].getCountry();
            final String name = available[i].getDisplayCountry(locale);

            if (!EMPTY.equals(iso) && !EMPTY.equals(name)) {
                LabelValue country = new LabelValue(name, iso);

                if (!countries.contains(country)) {
                    countries.add(new LabelValue(name, iso));
                }
            }
        }

        Collections.sort(countries, new LabelValueComparator(locale));

        return countries;
    }

    /**
     * Class to compare LabelValues using their labels with
     * locale-sensitive behaviour.
     */
    public class LabelValueComparator implements Comparator {
        private Comparator<Object> c;

        /**
         * Creates a new LabelValueComparator object.
         *
         * @param locale The Locale used for localized String comparison.
         */
        public LabelValueComparator(Locale locale) {
            c = Collator.getInstance(locale);
        }

        /**
         * Compares the localized labels of two LabelValues.
         *
         * @param o1 The first LabelValue to compare.
         * @param o2 The second LabelValue to compare.
         *
         * @return The value returned by comparing the localized labels.
         */
        public final int compare(Object o1, Object o2) {
            LabelValue lhs = (LabelValue) o1;
            LabelValue rhs = (LabelValue) o2;

            return c.compare(lhs.getLabel(), rhs.getLabel());
        }
    }
}
