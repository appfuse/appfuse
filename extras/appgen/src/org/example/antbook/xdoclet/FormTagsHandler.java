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

    private final static List supportedTypes = new ArrayList();
    private String curFieldName;
    private String curType;
    private boolean curFieldIsIdorVersion = false;
    private boolean curFieldIsBoolean = false;
    private boolean curFieldIsDate = false;
    private boolean lastField = false;
    
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
     * Gets the package name for the parent of this Package in directory format.
     * @return Parent package path.
     */
    public String parentPackageDir() {
        return parentPackageName().replace('.', '/');
    }

    /**
     * Iterates the body for each field of the current form requiring validation.
     *
     * @param template
     * @param attributes
     * @throws XDocletException
     */
    public void forAllFields(String template, Properties attributes) throws XDocletException {
        XClass clazz = getCurrentClass();
        Map setters = new LinkedHashMap(getFields(clazz));

        for (Iterator iterator = setters.keySet().iterator(); iterator.hasNext();) {
            curFieldName = (String) iterator.next();

            XMethod field = (XMethod) setters.get(curFieldName);
            
            XMethod getter = field.getAccessor();
            setCurrentMethod(getter);
            curFieldIsIdorVersion = false;
            Properties prop = new Properties();
            prop.setProperty("tagName", "hibernate.id");

            if (hasTag(prop, FOR_METHOD)) {
                prop.setProperty("paramName", "generator-class");
                String generatorClass = methodTagValue(prop);
                System.out.println("generatorClass: " + generatorClass);
                if (generatorClass != null && generatorClass.equals("assigned")) {
                    curFieldIsIdorVersion = false;
                } else {
                    curFieldIsIdorVersion = true;
                }
            } else {
                curFieldIsIdorVersion = false;
            }

            prop.setProperty("tagName", "hibernate.version");

            if (hasTag(prop, FOR_METHOD)) {
                curFieldIsIdorVersion = true;
            } 

            String typename = field.getPropertyType().getType().getQualifiedName();
            curFieldIsBoolean = typename.equals("boolean") || typename.equals("java.lang.Boolean");
            curFieldIsDate = typename.equals("java.util.Date") || typename.equals("java.sql.Timestamp");

            curType = typename;
            setCurrentMethod(field);

            if (!iterator.hasNext()) {
                lastField = true;
            } else {
                lastField = false;
            }

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

  /**
   * Method ifIsNotBooleanField
   *
   * @param template
   * @param attributes
   *
   * @throws XDocletException
   *
   */
    public void ifIsNotBooleanField(String template, Properties attributes) throws XDocletException {
        if (!curFieldIsBoolean)
            generate(template);
    }

    /**
     * This method is used so Date fields can be detected when building view templates.
     *
     * @param template
     * @param attributes
     * @throws XDocletException
     */
    public void ifIsDateField(String template, Properties attributes) throws XDocletException {
        if (curFieldIsDate)
            generate(template);
    }

  /**
   * Method ifIsDateField
   *
   * @param template
   * @param attributes
   *
   * @throws XDocletException
   *
   */
    public void ifIsNotDateField(String template, Properties attributes) throws XDocletException {
        if (!curFieldIsDate)
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

    public void ifIsNotLastField(String template, Properties attributes) throws XDocletException {
        if (lastField == false) {
           generate(template);
        }
    }

    /**
     * Method ifIsNotIdField
     *
     * @param template
     * @param attributes
     *
     * @throws XDocletException
    */
    public void ifIsNotIdOrVersionField(String template, Properties attributes) throws XDocletException {
        if (!curFieldIsIdorVersion) {
            generate(template);
        }
    }

    /**
     * Method ifFieldNameEquals
     *
     * @param template
     * @param attributes
     *
     * @throws XDocletException
     */
    public void ifFieldNameEquals(String template, Properties attributes) throws XDocletException{
        String name = attributes.getProperty("name");
  
        if ((name != null) && name.equals(curFieldName)) {
            generate(template);
        }
    }

    /**
     * Method ifFieldNameNotEquals
     *
     * @param template
     * @param attributes
     *
     * @throws XDocletException
     */
    public void ifFieldNameNotEquals(String template, Properties attributes) throws XDocletException {
        String name = attributes.getProperty("name");
    
        if ((name != null) && !name.equals(curFieldName)) {
            generate(template);
        }
    }

    /**
     * Method methodTagValue
     * @param attributes
     * @return
     * @throws XDocletException
     */
    public String methodTagValue(Properties attributes) throws XDocletException {
        XMethod method = getCurrentMethod();
        setCurrentMethod(method.getAccessor());
        String value = getTagValue(attributes, FOR_METHOD);
        setCurrentMethod(method);
        return value;
    }

    /**
     * Method columnName
     * @param attributes
     * @return
     * @throws XDocletException
     */
    public String columnName(Properties attributes) throws XDocletException {
        Properties prop = new Properties();
      
        prop.setProperty("tagName", "hibernate.property");
        prop.setProperty("paramName", "column");
      
        String column = methodTagValue(prop);
      
        if ((column == null) || (column.trim().length() < 1)) {
            prop.setProperty("tagName", "hibernate.id");
            column = methodTagValue(prop);
        }
      
        return column;
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
     * Returns the current field's java type.
     * @param props
     * @return
     */
    public String javaType(Properties props) {
      return curType;
    }
    
    /**
     * Returns the current field's jdbc type
     * @param props
     * @return
     */
    public String jdbcType(Properties props) {
        String jdbcType = "VARCHAR";
      
        if (curType != null) {
            String javaType = curType.toLowerCase();
        
            if (javaType.indexOf("date") > 0) {
                jdbcType = "TIMESTAMP";
            } else if (javaType.indexOf("timestamp") > 0) {
                jdbcType = "TIMESTAMP";
            } else if ((javaType.indexOf("int") > 0) || (javaType.indexOf("long") > 0) || (javaType.indexOf("short") > 0)) {
                jdbcType = "INTEGER";
            } else if (javaType.indexOf("double") > 0) {
                jdbcType = "DOUBLE";
            } else if (javaType.indexOf("float") > 0) {
                jdbcType = "FLOAT";
            }
        }
      
        return jdbcType;
    }
    
    /**
     * @return Classname of the POJO with first letter in lowercase
     */
    public String classNameLower() {
        String name = getCurrentClass().getName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);   
    }
    
    public String className() {
        return getCurrentClass().getName();
    }

    /**
     * Name of the POJO in UPPERCASE, for usage in Constants.java.
     * @return
     */
    public String classNameUpper() {
        String name = getCurrentClass().getName();
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
