package org.example.antbook.xdoclet;

import java.sql.Timestamp;

import java.text.*;
import java.util.*;

import xdoclet.XDocletException;
import xdoclet.tagshandler.*;
import xdoclet.util.TypeConversionUtil;
import xjavadoc.*;

/**
 * This class is an extension to xdoclet.tagshandler.MethodTagsHandler with
 * extra features designed for appgen.
 *
 * @author hzhang(mb4henry@yahoo.com.au)
 *
 */
public class MethodExTagsHandler extends MethodTagsHandler {
    //
    private static String datePattern = "yyyy-MM-dd";
    private static final List numericTypes = new ArrayList();
    private static final String rootClassName = getCurrentClass().getName();
    private static final String rootClassNameLower = Character.toLowerCase(rootClassName.charAt(0)) + rootClassName.substring(1);
    private String curprefix = "";

    static {
        numericTypes.add("java.lang.Integer");
        numericTypes.add("int");
        numericTypes.add("java.lang.Float");
        numericTypes.add("float");
        numericTypes.add("java.lang.Long");
        numericTypes.add("long");
        numericTypes.add("java.lang.Double");
        numericTypes.add("double");
        numericTypes.add("java.lang.Short");
        numericTypes.add("short");
        numericTypes.add("java.lang.Byte");
        numericTypes.add("byte");
    }

    public String className() {
        return rootClassName;
    }


    /**
     * Returns the transformed return type of the current method, without java.lang.
     *
     * @param attributes
     * @return                      Description of the Returned Value
     * @exception XDocletException  Description of Exception
     * @doc.tag                     type="content"
     */
    public String methodType(Properties attributes) throws XDocletException {
        String name = transformedMethodType(attributes);
        if (name.startsWith("java.lang.")) {
            name = name.substring(10);
        }
        return name;
    }

    /**
     * Iterates over all methods of current class and evaluates the body of the
     * tag for each method. <br>
     * The reason to override this method is to add nested form support when
     * iterating over all properties.
     *
     * @see xdoclet.tagshandler.MethodTagsHandler#forAllMethods(java.lang.String,
     *      java.util.Properties)
     */
    public void forAllMethods(String template, Properties attributes)
    throws XDocletException {


        if (getCurrentClass() == null) {
            throw new XDocletException("currentClass == null!!!");
        }

        forAllMembersEx(getCurrentClass(), template, attributes, FOR_METHOD, "");
    }

    /**
     * Method to print out an ActionForm, setter and parameter value
     *
     * @return @throws
     *         XDocletException
     */
    public String formSetterWithValue() throws XDocletException {
        XMethod method = super.getCurrentMethod();
        XMethod setter = method.getMutator();

        return rootClassNameLower + "Form." + curprefix + setter.getName() + "(" +
               randomValueForSetter() + ");";
    }

    /**
     * Method to print out a class, setter and a parameter value
     *
     * @return @throws
     *         XDocletException
     */
    public String setterWithValue() throws XDocletException {
        XMethod method = super.getCurrentMethod();
        XMethod setter = method.getMutator();

        return rootClassNameLower + "." + curprefix + setter.getName() + "(" +
               randomValueForSetter() + ");";
    }

    /**
     * A convenient way to get information of an id field. You can get the
     * property name, property type, setter name, getter name. When using this
     * method, you can pass in a parameter call "getType". And its value can be
     * one of
     * <ul>
     * <li>propertyName</li>
     * <li>propertyType</li>
     * <li>getterName</li>
     * <li>setterName</li>
     * </ul>
     * default will return property name.
     *
     * @param attributes
     * @return required information related to id field in the model class
     * @throws XDocletException
     */
    public String idField(Properties attributes) throws XDocletException {
        XClass currentClass = getCurrentClass();
        Collection members = null;
        members = currentClass.getMethods(true);

        String result = "";

        for (Iterator j = members.iterator(); j.hasNext();) {
            XMember member = (XMember) j.next();
            setCurrentMethod((XMethod) member);

            XMethod getter = (XMethod) member;

            if (super.isGetterMethod(getter)) {
                Properties pro = new Properties();
                pro.setProperty("tagName", "hibernate.id");

                if (super.hasTag(pro, FOR_METHOD)) {
                    break;
                }

                setCurrentClass(member.getContainingClass());
            }
        }

        String type = attributes.getProperty("getType");
        XMethod getter = super.getCurrentMethod();

        if ("propertyName".equals(type)) {
            result = getter.getPropertyName();
        } else if ("getterName".equals(type)) {
            result = getter.getName();
        } else if ("setterName".equals(type)) {
            result = getter.getMutator().getName();
        } else if ("propertyType".equals(type)) {
            result = getter.getPropertyType().getType().getTransformedName();
        } else {
            result = getter.getPropertyName();
        }

        return result;
    }

    /**
     * @see xdoclet.tagshandler.MethodTagsHandler#forAllMembers
     * @param currentClass
     * @param template
     * @param attributes
     * @param forType
     * @param prefix
     * @throws XDocletException
     */
    protected void forAllMembersEx(XClass currentClass, String template,
                                   Properties attributes, int forType,
                                   String prefix) throws XDocletException {
        boolean superclasses =
            TypeConversionUtil.stringToBoolean(attributes.getProperty("superclasses"),
                                               false);
        boolean sort =
            TypeConversionUtil.stringToBoolean(attributes.getProperty("sort"),
                                               true);

        Collection members = null;

        switch (forType) {
            case FOR_METHOD:
                members = currentClass.getMethods(superclasses);

                break;

            default:
                throw new XDocletException("Bad type: " + forType);
        }

        if (sort) {
            // sort fields, but we should make a copy first, because members is
            // not a new copy, it's shared by all
            List sortedMembers = new ArrayList(members);

            members = sortedMembers;
        }

        for (Iterator j = members.iterator(); j.hasNext();) {
            XMember member = (XMember) j.next();

            switch (forType) {
                case FOR_METHOD:
                    setCurrentMethod((XMethod) member);

                    break;

                default:
                    throw new XDocletException("Bad type: " + forType);
            }

            XMethod getter = (XMethod) member;

            if (super.isGetterMethod(getter)) {
                Properties pro = new Properties();

                //iterate through sub-components only
                pro.setProperty("tagName", "hibernate.component");

                if (super.hasTag(pro, FOR_METHOD)) {
                    Type type = getter.getReturnType();
                    String temp =
                        prefix +
                        ("get" + type.getType().getName() + "Form().");
                    forAllMembersEx(type.getType(), template, attributes,
                                    forType, temp);
                }

                setCurrentClass(member.getContainingClass());
                curprefix = prefix;
                generate(template);
            }
        }

        setCurrentClass(currentClass);
    }

    /**
     * Generates a random value for a field. Return "0" for boolean type. used
     * to generate random values for sample-data.xml
     *
     * @return a random value
     * @throws XDocletException
     */
    public String randomValueForDbUnit() throws XDocletException {
        Properties pros = new Properties();
        pros.put("type", "content");

        String mtype = super.methodType(pros);

        StringBuffer result = new StringBuffer();

        if ("java.lang.Integer".equals(mtype) || "int".equals(mtype)) {
            result.append((int) ((Math.random() * Integer.MAX_VALUE)) );
        } else if ("java.lang.Float".equals(mtype) || "float".equals(mtype)) {
            result.append((float) ((Math.random() * Float.MAX_VALUE)));
        } else if ("java.lang.Long".equals(mtype) || "long".equals(mtype)) {
            result.append((long) ((Math.random() * Long.MAX_VALUE)));
        } else if ("java.lang.Double".equals(mtype) || "double".equals(mtype)) {
            result.append((double) ((Math.random() * Double.MAX_VALUE)));
        } else if ("java.lang.Short".equals(mtype) || "short".equals(mtype)) {
            result.append((short) ((Math.random() * Short.MAX_VALUE)));
        } else if ("java.lang.Byte".equals(mtype) || "byte".equals(mtype)) {
            result.append((byte) ((Math.random() * Byte.MAX_VALUE)));
        } else if ("java.lang.Boolean".equals(mtype) || "boolean".equals(mtype)) {
            result.append("0");
        } else if ("java.util.Date".equals(mtype)) {
            result.append(getDate(new Date()));
        } else if ("java.sql.Timestamp".equals(mtype)) {
            result.append(new Timestamp(new Date().getTime()).toString());
        } else if ("email".equalsIgnoreCase(super.propertyName())) {
            result.append(super.propertyName() + (int) ((Math.random() * Integer.MAX_VALUE)) + "@dev.java.net");
        } else if ("java.lang.String".equals(mtype)) {
            String stringWithQuotes = generateStringValue();
            result.append(stringWithQuotes.substring(1, stringWithQuotes.length()-1));
        } else {
            throw new XDocletException("<XDtMethodEx:randomValueForDbUnit/> called on unsupported type: " + mtype);
        }

        //System.out.println("propertyType: " + mtype + " | dbUnit value: " + result.toString());
        return result.toString();
    }

    /**
     * Generates a random value for a field. Return "true" for boolean type.
     * Returned values are used in junit tests to create new values of the
     * appropriate type for testing.
     *
     * @return a random value
     * @throws XDocletException
     */
    public String randomValueForSetter() throws XDocletException {
        Properties pros = new Properties();
        pros.put("type", "content");

        String mtype = super.methodType(pros);

        StringBuffer result = new StringBuffer();

        if ("java.lang.Integer".equals(mtype)) {
            result.append("new Integer(" + (int) ((Math.random() * Integer.MAX_VALUE)) + ")");
        } else if ("int".equals(mtype)) {
            result.append("(int) " + (int) ((Math.random() * Integer.MAX_VALUE)));
        } else if ("java.lang.Float".equals(mtype) ) {
            result.append("new Float(" + (float) ((Math.random() * Float.MAX_VALUE)) + ")");
        } else if ("float".equals(mtype)) {
            result.append("(float) " + (float) ((Math.random() * Float.MAX_VALUE)));
        } else if ("java.lang.Long".equals(mtype)) {
            // not sure why, but Long.MAX_VALUE results in too large a number
            result.append("new Long(" + (long) ((Math.random() * Integer.MAX_VALUE)) + ")");
        } else if ("long".equals(mtype)) {
            // not sure why, but Long.MAX_VALUE results in too large a number
            result.append((long) ((Math.random() * Integer.MAX_VALUE)));
        } else if ("java.lang.Double".equals(mtype) ) {
            result.append("new Double(" + (double) ((Math.random() * Double.MAX_VALUE)) + ")");
        } else if ("double".equals(mtype)) {
            result.append((double) ((Math.random() * Double.MAX_VALUE)));
        } else if ("java.lang.Short".equals(mtype) ) {
            result.append("new Short(\"" + (short) ((Math.random() * Short.MAX_VALUE)) + "\")");
        } else if ("short".equals(mtype)) {
            result.append("(short)" + (short) ((Math.random() * Short.MAX_VALUE)));
        } else if ("java.lang.Byte".equals(mtype)) {
            result.append("new Byte(\"" + (byte) ((Math.random() * Byte.MAX_VALUE)) + "\")");
        } else if ("byte".equals(mtype)) {
            result.append("(byte) " + (byte) ((Math.random() * Byte.MAX_VALUE)));
        } else if ("java.lang.Boolean".equals(mtype)) {
            result.append("new Boolean(\"false\")");
        } else if ("boolean".equals(mtype)) {
            result.append("false");
        } else if ("java.util.Date".equals(mtype)) {
            result.append("new Date(" + getDate(new Date()) + ")");
        } else if ("java.sql.Timestamp".equals(mtype)) {
            result.append("Timestamp.valueOf(\"" + new Timestamp(new Date().getTime()).toString() + "\")");
            //result.append("\"" + new Timestamp(new Date().getTime()).toString() + "\"";
        } else if ("email".equalsIgnoreCase(super.propertyName())) {
            result.append("\"" + super.propertyName() + (int) ((Math.random() * Integer.MAX_VALUE)) +
                   "@dev.java.net\"");
        } else if ("java.lang.String".equals(mtype)) {
            result.append(generateStringValue());
        } else {
            throw new XDocletException("<XDtMethodEx:randomValueForSetter/> called on unsupported type: " + mtype);
        }
        //System.out.println("propertyType: " + mtype + " | setter value: " + result.toString());
        return result.toString();
    }


    private String generateStringValue() throws XDocletException {

        int maxLen = getHibernateLength();
        if (maxLen == 0) { maxLen = 25; }

        StringBuffer result = new StringBuffer("\"");

        for (int i = 0; (i < maxLen); i++) {
            int j = 0;
            if (i % 2 == 0) {
                j = (int) ((Math.random() * 26) + 65);
            } else {
                j = (int) ((Math.random() * 26) + 97);
            }
            result.append(new Character((char)j).toString());
        }

        result.append("\"");

        return result.toString();
    }

    private int getHibernateLength() throws XDocletException {

        Properties props = new Properties();

        props.setProperty("tagName", "hibernate.property");
        props.setProperty("paramName", "length");

        int length = 0;

        if (super.hasTag(props, FOR_METHOD)) {
            String tagVal = super.getTagValue(props, FOR_METHOD);
            System.out.println("property has hibernate.property length= " + tagVal);
            length = Integer.valueOf(tagVal).intValue();
        }
        return length;
    }

    /**
     * copy from org.appfuse.util.DateUtil
     *
     * @param aDate
     * @return
     */
    private static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }
}
