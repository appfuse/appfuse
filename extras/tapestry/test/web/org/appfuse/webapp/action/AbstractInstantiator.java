package org.appfuse.webapp.action;


//Copyright 2004 The Apache Software Foundation
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 * Used to instantiate an instance of an otherwise abstract class.
 * Analyzes the class and builds a sub-class where all abstract properties
 * have real implementations.
 *
 * Found at http://howardlewisship.com/blog/2004/05/tapestry-test-assist.html
 */
public class AbstractInstantiator {
    private static final Log LOG =
        LogFactory.getLog(AbstractInstantiator.class);

    private static class InstantiatorClassLoader extends ClassLoader {
        public InstantiatorClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class loadClass(String name, byte[] bytecodes) {
            Class result = defineClass(name, bytecodes, 0, bytecodes.length);

            resolveClass(result);

            return result;
        }
    }

    private ClassPool _classPool;

    /**
     * Map keyed on abstract Class, value is an enhanced Class.
     */
    private Map _enhancedClasses = new HashMap();
    private InstantiatorClassLoader _loader;
    private Map _primitives = new HashMap();
    private IResourceResolver _resolver;
    private int _uid = 0;

    {
        _primitives.put(boolean.class, "boolean");
        _primitives.put(short.class, "short");
        _primitives.put(byte.class, "byte");
        _primitives.put(char.class, "char");
        _primitives.put(int.class, "int");
        _primitives.put(long.class, "long");
        _primitives.put(float.class, "float");
        _primitives.put(double.class, "double");
    }

    public AbstractInstantiator() {
        this(new DefaultResourceResolver());
    }

    public AbstractInstantiator(IResourceResolver resolver) {
        _resolver = resolver;

        ClassLoader parentLoader = _resolver.getClassLoader();

        _loader = new InstantiatorClassLoader(parentLoader);

        _classPool = new ClassPool(null);

        ClassPath path = new LoaderClassPath(parentLoader);

        _classPool.appendClassPath(path);
    }

    private void addAccessorMethod(CtClass ctClass, PropertyDescriptor pd,
                                   String attributeName) {
        String methodName =
            getMethodName(pd.getReadMethod(), "get", pd.getName());

        CtClass returnType = getCtClass(pd.getPropertyType());

        CtMethod method =
            new CtMethod(returnType, methodName, new CtClass[0], ctClass);

        try {
            method.setBody("{ return " + attributeName + "; }");
            method.setModifiers(Modifier.PUBLIC);

            ctClass.addMethod(method);
        } catch (CannotCompileException ex) {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private void addDefaultConstructor(CtClass ctClass) {
    }

    private void addField(CtClass ctClass, String fieldName, Class fieldType) {
        CtClass ctType = getCtClass(fieldType);

        try {
            CtField field = new CtField(ctType, fieldName, ctClass);

            field.setModifiers(Modifier.PRIVATE);

            ctClass.addField(field);
        } catch (CannotCompileException ex) {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private void addMissingProperties(CtClass ctClass, BeanInfo info) {
        PropertyDescriptor[] pd = info.getPropertyDescriptors();

        for (int i = 0; i < pd.length; i++) {
            addMissingProperty(ctClass, pd[i]);
        }
    }

    private void addMissingProperty(CtClass ctClass, PropertyDescriptor pd) {
        Method readMethod = pd.getReadMethod();
        Method writeMethod = pd.getWriteMethod();

        boolean abstractRead = isAbstract(readMethod);
        boolean abstractWrite = isAbstract(writeMethod);

        if (!(abstractRead || abstractWrite)) {
            return;
        }

        String attributeName = "_$" + pd.getName();
        Class propertyType = pd.getPropertyType();

        addField(ctClass, attributeName, propertyType);

        if (abstractRead) {
            addAccessorMethod(ctClass, pd, attributeName);
        }

        if (abstractWrite) {
            addMutatorMethod(ctClass, pd, attributeName);
        }
    }

    private void addMutatorMethod(CtClass ctClass, PropertyDescriptor pd,
                                  String attributeName) {
        String methodName =
            getMethodName(pd.getWriteMethod(), "set", pd.getName());

        CtClass parameterType = getCtClass(pd.getPropertyType());

        CtMethod method =
            new CtMethod(CtClass.voidType, methodName,
                         new CtClass[] { parameterType }, ctClass);

        try {
            method.setBody("{ " + attributeName + " = $1; }");
            method.setModifiers(Modifier.PUBLIC);

            ctClass.addMethod(method);
        } catch (CannotCompileException ex) {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private String constructNewClassName(Class inputClass) {
        return inputClass.getName() + "$enhance_" + _uid++;
    }

    private Class createEnhancedClass(Class inputClass) {
        if (inputClass.isInterface()) {
            throw new IllegalArgumentException("Can not create instance of interface " +
                                               inputClass.getName() + ".");
        }

        if (!Modifier.isAbstract(inputClass.getModifiers())) {
            LOG.error("Class " + inputClass.getName() + " is not abstract.");

            return inputClass;
        }

        BeanInfo info = null;

        try {
            info = Introspector.getBeanInfo(inputClass, Object.class);
        } catch (IntrospectionException ex) {
            throw new ApplicationRuntimeException("Unable to introspect class " +
                                                  inputClass.getName() + ": " +
                                                  ex.getMessage(), ex);
        }

        String newName = constructNewClassName(inputClass);

        CtClass newClass =
            _classPool.makeClass(newName, getCtClass(inputClass));

        addMissingProperties(newClass, info);

        addDefaultConstructor(newClass);

        try {
            // newClass.writeFile();
            byte[] bytecode = _classPool.write(newName);

            return _loader.loadClass(newName, bytecode);
        } catch (Exception ex) {
            throw new ApplicationRuntimeException("Unable to instantiate enhanced subclass " +
                                                  newClass.getName() + ": " +
                                                  ex.getMessage(), ex);
        }
    }

    private CtClass getCtClass(Class inputClass) {
        String name = getCtName(inputClass);

        try {
            return _classPool.get(name);
        } catch (NotFoundException ex) {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private String getCtName(Class inputClass) {
        if (inputClass.isArray()) {
            return getCtName(inputClass.getComponentType()) + "[]";
        }

        if (inputClass.isPrimitive()) {
            return (String) _primitives.get(inputClass);
        }

        return inputClass.getName();
    }

    public Class getEnhancedClass(Class inputClass) {
        Class result = (Class) _enhancedClasses.get(inputClass);

        if (result == null) {
            result = createEnhancedClass(inputClass);

            _enhancedClasses.put(inputClass, result);
        }

        return result;
    }

    /**
     * Given a particular abstract class; will create an instance of that class. A subclass
     * is created with all abstract properties filled in with ordinary implementations.
     */
    public Object getInstance(Class abstractClass) {
        Class enhancedClass = getEnhancedClass(abstractClass);

        try {
            return enhancedClass.newInstance();
        } catch (Exception ex) {
            throw new ApplicationRuntimeException("Unable to instantiate instance of " +
                                                  enhancedClass.getName() +
                                                  ": " + ex.getMessage(), ex);
        }
    }

    private String getMethodName(Method m, String prefix, String propertyName) {
        if (m != null) {
            return m.getName();
        }

        StringBuffer buffer = new StringBuffer(prefix);

        buffer.append(propertyName.substring(0, 1).toUpperCase());
        buffer.append(propertyName.substring(1));

        return buffer.toString();
    }

    private boolean isAbstract(Method m) {
        return (m == null) || Modifier.isAbstract(m.getModifiers());
    }
}
