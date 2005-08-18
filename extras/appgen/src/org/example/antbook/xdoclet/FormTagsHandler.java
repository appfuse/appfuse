package org.example.antbook.xdoclet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.LinkedHashMap;

import xdoclet.XDocletException;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.MethodTagsHandler;
import xjavadoc.XClass;
import xjavadoc.XMethod;
import xjavadoc.XParameter;

public class FormTagsHandler extends AbstractProgramElementTagsHandler {
    XClass clazz = getCurrentClass();
    private String curFieldName;
    private final static List supportedTypes = new ArrayList();
    private boolean curFieldIsIdorVersion = false;
    private boolean curFieldIsBoolean = false;
    
    static {
        supportedTypes.add("java.lang.String");
        supportedTypes.add("java.lang.Integer");
        supportedTypes.add("int");
        supportedTypes.add("java.lang.Float");
        supportedTypes.add("float");
        supportedTypes.add("java.lang.Long");
        supportedTypes.add("long");
        supportedTypes.add("java.lang.Double");
        supportedTypes.add("double");
        supportedTypes.add("java.lang.Boolean");
        supportedTypes.add("boolean");
        supportedTypes.add("java.util.Date");
        supportedTypes.add("java.sql.Timestamp");
    }

    /**
     * Gets the package name for the parent of this Package.
     * @author Lance Lavandowska
     */
    public String parentPackageName() {
        String packageName = getCurrentPackage().getName();
        return packageName.substring(0, packageName.lastIndexOf("."));
    }

    /**
     * Iterates the body for each field of the current form requiring validation.
     *
     * @param template
     * @param attributes
     * @throws XDocletException
     */
    public void forAllFields(String template, Properties attributes) throws XDocletException {
        Map setters = new LinkedHashMap(getFields(clazz));

        for (Iterator iterator = setters.keySet().iterator(); iterator.hasNext();) {
            curFieldName = (String) iterator.next();

            XMethod field = (XMethod) setters.get(curFieldName);

            
            XMethod getter = field.getAccessor();
            setCurrentMethod(getter);
       	    curFieldIsIdorVersion = false;
            Properties pro = new Properties();
            pro.setProperty("tagName", "hibernate.id");

            if (hasTag(pro, FOR_METHOD)) {
                curFieldIsIdorVersion = true;
            }

            pro.setProperty("tagName", "hibernate.version");

            if (hasTag(pro, FOR_METHOD)) {
                curFieldIsIdorVersion = true;
            }

            String typename = field.getPropertyType().getType().getQualifiedName();
            curFieldIsBoolean = typename.equals("boolean") || typename.equals("java.lang.Boolean");

            setCurrentMethod(field);
            generate(template);
        }
    }

    /**
     * This method is added so that I can pick up a boolean field. When
     * generating a form page, checkbox is used for boolean fields.
     *
     * @author hzhang(mb4henry@yahoo.com.au)
     * @param template
     * @param attributes
     * @throws XDocletException
     */
    public void ifIsBooleanField(String template, Properties attributes) throws XDocletException {
        if (curFieldIsBoolean)
            generate(template);
    }

    public void ifIsNotBooleanField(String template, Properties attributes) throws XDocletException {
        if (!curFieldIsBoolean)
            generate(template);
    }

    /**
     * This method is used to determine id fields - this is used in the view
     * pages to set the ids as hidden fields.
     *
     * @param template
     * @param attributes
     * @throws XDocletException
     */
    public void ifIsIdOrVersionField(String template, Properties attributes) throws XDocletException {
        if (curFieldIsIdorVersion) {
            generate(template);
        }
    }

    public void ifIsNotIdOrVersionField(String template, Properties attributes) throws XDocletException {
        if (!curFieldIsIdorVersion) {
            generate(template);
        }
    }
    
    /**
     * Returns the current fields name.
     *
     * @param props
     * @return
     */
    public String fieldName(Properties props) {
        return curFieldName;
    }

    /**
     * @return Classname of the POJO with first letter in lowercase
     */
    public String classNameLower() {
        String name = clazz.getName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);   
    }
    
    public String className() {
        return clazz.getName();
    }

    /**
     * Name of the POJO in UPPERCASE, for usage in Constants.java.
     * @return
     */
    public String classNameUpper() {
        String name = clazz.getName();
        return name.toUpperCase();
    }

    public String fieldDescription(Properties props) {
        StringBuffer buffer = new StringBuffer();
        boolean nextUpper = false;
        for (int i = 0; i < curFieldName.length(); i++) {
            char c = curFieldName.charAt(i);

            if (i == 0) {
                buffer.append(Character.toUpperCase(c));
                continue;
            }

            if (Character.isUpperCase(c)) {
                buffer.append(' ');
                buffer.append(c);
                continue;
            }

            if (c == '.') {
                buffer.delete(0, buffer.length());
                nextUpper = true;
                continue;
            }

            char x = nextUpper ? Character.toUpperCase(c) : c;
            buffer.append(x);
            nextUpper = false;
        }

        return buffer.toString();
    }

    private Map getFields(XClass clazz) throws XDocletException {
        return getFields(clazz, "");
    }

    private Map getFields(XClass clazz, String prefix) throws XDocletException {
        Map fields = new LinkedHashMap();

        Collection curFields = clazz.getMethods(true);

        for (Iterator iterator = curFields.iterator(); iterator.hasNext();) {
            XMethod setter = (XMethod) iterator.next();

            if (MethodTagsHandler.isSetterMethod(setter)) {
                String name = MethodTagsHandler.getPropertyNameFor(setter);
                XParameter param = (XParameter) setter.getParameters().iterator().next();
                String type = param.getType().getQualifiedName();
                XMethod getter = setter.getAccessor();

                setCurrentClass(setter.getContainingClass());
                super.setCurrentMethod(getter);
                Properties pro = new Properties();
                pro.setProperty("tagName", "hibernate.component");
                
                if (super.hasTag(pro, FOR_METHOD)) {
                    name += "Form";
                    fields.putAll(getFields(param.getType(), prefix + name + "."));
                } else {
                    fields.put(prefix + name, setter);
                }
            }
        }

        return fields;
    }
}
