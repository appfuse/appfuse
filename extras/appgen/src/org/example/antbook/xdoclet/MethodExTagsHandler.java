package org.example.antbook.xdoclet;

import java.sql.Timestamp;

import java.text.*;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import xdoclet.XDocletException;

import xdoclet.tagshandler.*;
import xdoclet.tagshandler.MethodTagsHandler;

import xdoclet.util.TypeConversionUtil;

import xjavadoc.*;
import xjavadoc.XClass;
import xjavadoc.XMember;
import xjavadoc.XMethod;
import xjavadoc.XParameter;
import xjavadoc.XType;

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
    private String rootClassName = "";
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
        XClass currentClass = getCurrentClass();
        rootClassName = currentClass.getName();

        char c = rootClassName.charAt(0);
        c = Character.toLowerCase(c);
        rootClassName = c + rootClassName.substring(1);

        if (currentClass == null) {
            throw new XDocletException("currentClass == null!!!");
        }

        forAllMembersEx(currentClass, template, attributes, FOR_METHOD, "");
    }

    /**
     * In appgen, The action test class need to set required fields. This method
     * is writen for that reason. This method will iterate through
     * sub-components to find all methods.
     *
     * @return @throws
     *         XDocletException
     */
    public String nestedMethodName() throws XDocletException {
        XMethod method = super.getCurrentMethod();
        XMethod setter = method.getMutator();

        return rootClassName + "Form." + curprefix + setter.getName() + "(\"" +
               randomValue() + "\");";
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
        members = currentClass.getMethods(false);

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
            result = getter.getPropertyType().getType().getQualifiedName();
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
                                               true);
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

    public String className() {
        return getCurrentClass().getName();
    }

    /**
     * Generates a random value for a field. Return "0" for boolean type. used
     * to generate random values for sample-data.xml
     *
     * @return a random value
     * @throws XDocletException
     */
    public String randomValue() throws XDocletException {
        Properties pros = new Properties();
        pros.put("type", "content");

        String mtype = super.methodType(pros);

        if (numericTypes.contains(mtype)) {
            return (int) ((Math.random() * 1000) + 1) + "";
        } else if ("java.lang.Boolean".equals(mtype) ||
                       "boolean".equals(mtype)) {
            return "0";
        } else if ("java.util.Date".equals(mtype)) {
            return getDate(new Date());
        } else if ("java.sql.Timestamp".equals(mtype)) {
            return new Timestamp(new Date().getTime()).toString();
        } else if ("email".equalsIgnoreCase(super.propertyName())) {
            return super.propertyName() + (int) ((Math.random() * 1000) + 1) +
                   "@dev.java.net";
        } else {
            return super.propertyName() + (int) ((Math.random() * 1000) + 1);
        }
    }

    /**
     * Generates a random value for a field. Return "true" for boolean type
     *
     * @return a random value
     * @throws XDocletException
     */
    public String randomValue2() throws XDocletException {
        Properties pros = new Properties();
        pros.put("type", "content");

        String mtype = super.methodType(pros);

        if (numericTypes.contains(mtype)) {
            return (int) ((Math.random() * 1000) + 1) + "";
        } else if ("java.lang.Boolean".equals(mtype) ||
                       "boolean".equals(mtype)) {
            return "true";
        } else if ("java.util.Date".equals(mtype)) {
            return getDate(new Date());
        } else if ("java.sql.Timestamp".equals(mtype)) {
            return new Timestamp(new Date().getTime()).toString();
        } else if ("email".equalsIgnoreCase(super.propertyName())) {
            return super.propertyName() + (int) ((Math.random() * 1000) + 1) +
                   "@appfuse.dev.java.net";
        } else {
            return super.propertyName() + (int) ((Math.random() * 1000) + 1);
        }
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
