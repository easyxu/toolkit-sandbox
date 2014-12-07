package com.phoenix.common.reflect;



import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phoenix.common.collection.CollectionUtils;
@SuppressWarnings("unchecked")
public abstract class ClassUtils {
	/** Suffix for array class names */
	public static final String ARRAY_SUFFIX = "[]";

	/** The package separator character '.' */
	private static final char PACKAGE_SEPARATOR = '.';

	/** The inner class separator character '$' */
	private static final char INNER_CLASS_SEPARATOR = '$';

	/** The CGLIB class separator character "$$" */
	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	/** The ".class" file suffix */
	public static final String CLASS_FILE_SUFFIX = ".class";


	private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	/**
	 * Map with primitive wrapper type as key and corresponding primitive
	 * type as value, for example: Integer.class -> int.class.
	 */
	private static final Map primitiveWrapperTypeMap = new HashMap(8);

	/**
	 * Map with primitive type name as key and corresponding primitive
	 * type as value, for example: "int" -> "int.class".
	 */
	private static final Map primitiveTypeNameMap = new HashMap(8);

	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);

		for (Iterator it = primitiveWrapperTypeMap.values().iterator(); it.hasNext();) {
			Class primitiveClass = (Class) it.next();
			primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
		}
	}


	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
	 * class will be used as fallback.
	 * <p>Call this method if you intend to use the thread context ClassLoader
	 * in a scenario where you absolutely need a non-null ClassLoader reference:
	 * for example, for class path resource loading (but not necessarily for
	 * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
	 * reference as well).
	 * @return the default ClassLoader (never <code>null</code>)
	 * @see Thread#getContextClassLoader()
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			logger.debug("Cannot access thread context ClassLoader - falling back to system class loader", ex);
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtils.class.getClassLoader();
		}
		return cl;
	}

	/**
	 * Determine whether the {@link Class} identified by the supplied name is present
	 * and can be loaded. Will return <code>false</code> if either the class or
	 * one of its dependencies is not present or cannot be loaded.
	 * @param className the name of the class to check
	 * @return whether the specified class is present
	 */
	public static boolean isPresent(String className) {
		return isPresent(className, getDefaultClassLoader());
	}

	/**
	 * Determine whether the {@link Class} identified by the supplied name is present
	 * and can be loaded. Will return <code>false</code> if either the class or
	 * one of its dependencies is not present or cannot be loaded.
	 * @param className the name of the class to check
	 * @param classLoader the class loader to use
	 * (may be <code>null</code>, which indicates the default class loader)
	 * @return whether the specified class is present
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			forName(className, classLoader);
			return true;
		}
		catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Class [" + className + "] or one of its dependencies is not present: " + ex);
			}
			return false;
		}
	}

	/**
	 * Replacement for <code>Class.forName()</code> that also returns Class instances
	 * for primitives (like "int") and array class names (like "String[]").
	 * <p>Always uses the default class loader: that is, preferably the thread context
	 * class loader, or the ClassLoader that loaded the ClassUtils class as fallback.
	 * @param name the name of the Class
	 * @return Class instance for the supplied name
	 * @throws ClassNotFoundException if the class was not found
	 * @throws LinkageError if the class file could not be loaded
	 * @see Class#forName(String, boolean, ClassLoader)
	 * @see #getDefaultClassLoader()
	 */
	public static Class forName(String name) throws ClassNotFoundException, LinkageError {
		return forName(name, getDefaultClassLoader());
	}

	/**
	 * Replacement for <code>Class.forName()</code> that also returns Class instances
	 * for primitives (like "int") and array class names (like "String[]").
	 * @param name the name of the Class
	 * @param classLoader the class loader to use
	 * (may be <code>null</code>, which indicates the default class loader)
	 * @return Class instance for the supplied name
	 * @throws ClassNotFoundException if the class was not found
	 * @throws LinkageError if the class file could not be loaded
	 * @see Class#forName(String, boolean, ClassLoader)
	 */
	public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
	
		Class clazz = resolvePrimitiveClassName(name);
		if (clazz != null) {
			return clazz;
		}
		if (name.endsWith(ARRAY_SUFFIX)) {
			// special handling for array class names
			String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
			Class elementClass = forName(elementClassName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = getDefaultClassLoader();
		}
		return classLoaderToUse.loadClass(name);
	}

	/**
	 * Resolve the given class name into a Class instance. Supports
	 * primitives (like "int") and array class names (like "String[]").
	 * <p>This is effectively equivalent to the <code>forName</code>
	 * method with the same arguments, with the only difference being
	 * the exceptions thrown in case of class loading failure.
	 * @param className the name of the Class
	 * @param classLoader the class loader to use
	 * (may be <code>null</code>, which indicates the default class loader)
	 * @return Class instance for the supplied name
	 * @throws IllegalArgumentException if the class name was not resolvable
	 * (that is, the class could not be found or the class file could not be loaded)
	 * @see #forName(String, ClassLoader)
	 */
	public static Class resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
		try {
			return forName(className, classLoader);
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalArgumentException("Cannot find class [" + className + "]. Root cause: " + ex);
		}
		catch (LinkageError ex) {
			throw new IllegalArgumentException("Error loading class [" + className +
					"]: problem with class file or dependent class. Root cause: " + ex);
		}
	}

	/**
	 * Resolve the given class name as primitive class, if appropriate.
	 * @param name the name of the potentially primitive class
	 * @return the primitive class, or <code>null</code> if the name does not denote
	 * a primitive class
	 */
	public static Class resolvePrimitiveClassName(String name) {
		Class result = null;
		// Most class names will be quite long, considering that they
		// SHOULD sit in a package, so a length check is worthwhile.
		if (name != null && name.length() <= 8) {
			// Could be a primitive - likely.
			result = (Class) primitiveTypeNameMap.get(name);
		}
		return result;
	}

	/**
	 * Return the user-defined class for the given instance: usually simply
	 * the class of the given instance, but the original class in case of a
	 * CGLIB-generated subclass.
	 * @param instance the instance to check
	 * @return the user-defined class
	 */
	public static Class getUserClass(Object instance) {
	
		return getUserClass(instance.getClass());
	}

	/**
	 * Return the user-defined class for the given class: usually simply the given
	 * class, but the original class in case of a CGLIB-generated subclass.
	 * @param clazz the class to check
	 * @return the user-defined class
	 */
	public static Class getUserClass(Class clazz) {
		return (clazz != null && clazz.getName().indexOf(CGLIB_CLASS_SEPARATOR) != -1 ?
				clazz.getSuperclass() : clazz);
	}


	/**
	 * Get the class name without the qualified package name.
	 * @param className the className to get the short name for
	 * @return the class name of the class without the package name
	 * @throws IllegalArgumentException if the className is empty
	 */
	public static String getShortName(String className) {
		
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
		if (nameEndIndex == -1) {
			nameEndIndex = className.length();
		}
		String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
		shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
		return shortName;
	}

	/**
	 * Get the class name without the qualified package name.
	 * @param clazz the class to get the short name for
	 * @return the class name of the class without the package name
	 */
	public static String getShortName(Class clazz) {
		return getShortName(getQualifiedName(clazz));
	}

	/**
	 * Return the short string name of a Java class in decapitalized
	 * JavaBeans property format.
	 * @param clazz the class
	 * @return the short name rendered in a standard JavaBeans property format
	 * @see java.beans.Introspector#decapitalize(String)
	 */
	public static String getShortNameAsProperty(Class clazz) {
		return Introspector.decapitalize(getShortName(clazz));
	}

	/**
	 * Determine the name of the class file, relative to the containing
	 * package: e.g. "String.class"
	 * @param clazz the class
	 * @return the file name of the ".class" file
	 */
	public static String getClassFileName(Class clazz) {
	
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}

	/**
	 * Return the qualified name of the given class: usually simply
	 * the class name, but component type class name + "[]" for arrays.
	 * @param clazz the class
	 * @return the qualified name of the class
	 */
	public static String getQualifiedName(Class clazz) {
	
		if (clazz.isArray()) {
			return getQualifiedNameForArray(clazz);
		}
		else {
			return clazz.getName();
		}
	}

	/**
	 * Build a nice qualified name for an array:
	 * component type class name + "[]".
	 * @param clazz the array class
	 * @return a qualified name for the array class
	 */
	private static String getQualifiedNameForArray(Class clazz) {
		StringBuffer buffer = new StringBuffer();
		while (clazz.isArray()) {
			clazz = clazz.getComponentType();
			buffer.append(ClassUtils.ARRAY_SUFFIX);
		}
		buffer.insert(0, clazz.getName());
		return buffer.toString();
	}

	/**
	 * Return the qualified name of the given method, consisting of
	 * fully qualified interface/class name + "." + method name.
	 * @param method the method
	 * @return the qualified name of the method
	 */
	public static String getQualifiedMethodName(Method method) {
	
		return method.getDeclaringClass().getName() + "." + method.getName();
	}


	/**
	 * Determine whether the given class has a constructor with the given signature.
	 * <p>Essentially translates <code>NoSuchMethodException</code> to "false".
	 * @param clazz	the clazz to analyze
	 * @param paramTypes the parameter types of the method
	 * @return whether the class has a corresponding constructor
	 * @see Class#getMethod
	 */
	public static boolean hasConstructor(Class clazz, Class[] paramTypes) {
		return (getConstructorIfAvailable(clazz, paramTypes) != null);
	}

	/**
	 * Determine whether the given class has a constructor with the given signature,
	 * and return it if available (else return <code>null</code>).
	 * <p>Essentially translates <code>NoSuchMethodException</code> to <code>null</code>.
	 * @param clazz	the clazz to analyze
	 * @param paramTypes the parameter types of the method
	 * @return the constructor, or <code>null</code> if not found
	 * @see Class#getConstructor
	 */
	public static Constructor getConstructorIfAvailable(Class clazz, Class[] paramTypes) {
	
		try {
			return clazz.getConstructor(paramTypes);
		}
		catch (NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * Determine whether the given class has a method with the given signature.
	 * <p>Essentially translates <code>NoSuchMethodException</code> to "false".
	 * @param clazz	the clazz to analyze
	 * @param methodName the name of the method
	 * @param paramTypes the parameter types of the method
	 * @return whether the class has a corresponding method
	 * @see Class#getMethod
	 */
	public static boolean hasMethod(Class clazz, String methodName, Class[] paramTypes) {
		return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
	}

	/**
	 * Determine whether the given class has a method with the given signature,
	 * and return it if available (else return <code>null</code>).
	 * <p>Essentially translates <code>NoSuchMethodException</code> to <code>null</code>.
	 * @param clazz	the clazz to analyze
	 * @param methodName the name of the method
	 * @param paramTypes the parameter types of the method
	 * @return the method, or <code>null</code> if not found
	 * @see Class#getMethod
	 */
	public static Method getMethodIfAvailable(Class clazz, String methodName, Class... paramTypes) {
	
		try {
			return clazz.getMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * Return the number of methods with a given name (with any argument types),
	 * for the given class and/or its superclasses. Includes non-public methods.
	 * @param clazz	the clazz to check
	 * @param methodName the name of the method
	 * @return the number of methods with the given name
	 */
	public static int getMethodCountForName(Class clazz, String methodName) {
	
		int count = 0;
		for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
			Method method = clazz.getDeclaredMethods()[i];
			if (methodName.equals(method.getName())) {
				count++;
			}
		}
		Class[] ifcs = clazz.getInterfaces();
		for (int i = 0; i < ifcs.length; i++) {
			count += getMethodCountForName(ifcs[i], methodName);
		}
		if (clazz.getSuperclass() != null) {
			count += getMethodCountForName(clazz.getSuperclass(), methodName);
		}
		return count;
	}

	/**
	 * Does the given class and/or its superclasses at least have one or more
	 * methods (with any argument types)? Includes non-public methods.
	 * @param clazz	the clazz to check
	 * @param methodName the name of the method
	 * @return whether there is at least one method with the given name
	 */
	public static boolean hasAtLeastOneMethodWithName(Class clazz, String methodName) {
	
		for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
			Method method = clazz.getDeclaredMethods()[i];
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		Class[] ifcs = clazz.getInterfaces();
		for (int i = 0; i < ifcs.length; i++) {
			if (hasAtLeastOneMethodWithName(ifcs[i], methodName)) {
				return true;
			}
		}
		return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
	}

	/**
	 * Given a method, which may come from an interface, and a target class used
	 * in the current reflective invocation, find the corresponding target method
	 * if there is one. E.g. the method may be <code>IFoo.bar()</code> and the
	 * target class may be <code>DefaultFoo</code>. In this case, the method may be
	 * <code>DefaultFoo.bar()</code>. This enables attributes on that method to be found.
	 * @param method the method to be invoked, which may come from an interface
	 * @param targetClass the target class for the current invocation.
	 * May be <code>null</code> or may not even implement the method.
	 * @return the specific target method, or the original method if the
	 * <code>targetClass</code> doesn't implement it or is <code>null</code>
	 */
	public static Method getMostSpecificMethod(Method method, Class targetClass) {
		if (method != null && targetClass != null) {
			try {
				method = targetClass.getMethod(method.getName(), method.getParameterTypes());
			}
			catch (NoSuchMethodException ex) {
				// Perhaps the target class doesn't implement this method:
				// that's fine, just use the original method.
			}
		}
		return method;
	}

	/**
	 * Return a static method of a class.
	 * @param methodName the static method name
	 * @param clazz	the class which defines the method
	 * @param args the parameter types to the method
	 * @return the static method, or <code>null</code> if no static method was found
	 * @throws IllegalArgumentException if the method name is blank or the clazz is null
	 */
	public static Method getStaticMethod(Class clazz, String methodName, Class[] args) {
	
		try {
			Method method = clazz.getDeclaredMethod(methodName, args);
			if ((method.getModifiers() & Modifier.STATIC) != 0) {
				return method;
			}
		}
		catch (NoSuchMethodException ex) {
		}
		return null;
	}


	/**
	 * Check if the given class represents a primitive wrapper,
	 * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 * @param clazz the class to check
	 * @return whether the given class is a primitive wrapper class
	 */
	public static boolean isPrimitiveWrapper(Class clazz) {
	
		return primitiveWrapperTypeMap.containsKey(clazz);
	}

	/**
	 * Check if the given class represents a primitive (i.e. boolean, byte,
	 * char, short, int, long, float, or double) or a primitive wrapper
	 * (i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
	 * @param clazz the class to check
	 * @return whether the given class is a primitive or primitive wrapper class
	 */
	public static boolean isPrimitiveOrWrapper(Class clazz) {
	
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * Check if the given class represents an array of primitives,
	 * i.e. boolean, byte, char, short, int, long, float, or double.
	 * @param clazz the class to check
	 * @return whether the given class is a primitive array class
	 */
	public static boolean isPrimitiveArray(Class clazz) {
	
		return (clazz.isArray() && clazz.getComponentType().isPrimitive());
	}

	/**
	 * Check if the given class represents an array of primitive wrappers,
	 * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 * @param clazz the class to check
	 * @return whether the given class is a primitive wrapper array class
	 */
	public static boolean isPrimitiveWrapperArray(Class clazz) {

		return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
	}

	/**
	 * Determine if the given target type is assignable from the given value
	 * type, assuming setting by reflection. Considers primitive wrapper
	 * classes as assignable to the corresponding primitive types.
	 * @param targetType the target type
	 * @param valueType	the value type that should be assigned to the target type
	 * @return if the target type is assignable from the value type
	 */
	public static boolean isAssignable(Class targetType, Class valueType) {
	
		return (targetType.isAssignableFrom(valueType) ||
				targetType.equals(primitiveWrapperTypeMap.get(valueType)));
	}

	/**
	 * Determine if the given type is assignable from the given value,
	 * assuming setting by reflection. Considers primitive wrapper classes
	 * as assignable to the corresponding primitive types.
	 * @param type	the target type
	 * @param value the value that should be assigned to the type
	 * @return if the type is assignable from the value
	 */
	public static boolean isAssignableValue(Class type, Object value) {
	
		return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
	}


	/**
	 * Return a path suitable for use with <code>ClassLoader.getResource</code>
	 * (also suitable for use with <code>Class.getResource</code> by prepending a
	 * slash ('/') to the return value. Built by taking the package of the specified
	 * class file, converting all dots ('.') to slashes ('/'), adding a trailing slash
	 * if necesssary, and concatenating the specified resource name to this.
	 * <br/>As such, this function may be used to build a path suitable for
	 * loading a resource file that is in the same package as a class file,
	 * although {@link org.springframework.core.io.ClassPathResource} is usually
	 * even more convenient.
	 * @param clazz	the Class whose package will be used as the base
	 * @param resourceName the resource name to append. A leading slash is optional.
	 * @return the built-up resource path
	 * @see ClassLoader#getResource
	 * @see Class#getResource
	 */
	public static String addResourcePathToPackagePath(Class clazz, String resourceName) {
		
		if (!resourceName.startsWith("/")) {
			return classPackageAsResourcePath(clazz) + "/" + resourceName;
		}
		return classPackageAsResourcePath(clazz) + resourceName;
	}

	/**
	 * Given an input class object, return a string which consists of the
	 * class's package name as a pathname, i.e., all dots ('.') are replaced by
	 * slashes ('/'). Neither a leading nor trailing slash is added. The result
	 * could be concatenated with a slash and the name of a resource, and fed
	 * directly to <code>ClassLoader.getResource()</code>. For it to be fed to
	 * <code>Class.getResource</code> instead, a leading slash would also have
	 * to be prepended to the returned value.
	 * @param clazz the input class. A <code>null</code> value or the default
	 * (empty) package will result in an empty string ("") being returned.
	 * @return a path which represents the package name
	 * @see ClassLoader#getResource
	 * @see Class#getResource
	 */
	public static String classPackageAsResourcePath(Class clazz) {
		if (clazz == null) {
			return "";
		}
		String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf('.');
		if (packageEndIndex == -1) {
			return "";
		}
		String packageName = className.substring(0, packageEndIndex);
		return packageName.replace('.', '/');
	}

	/**
	 * Build a String that consists of the names of the classes/interfaces
	 * in the given array.
	 * <p>Basically like <code>AbstractCollection.toString()</code>, but stripping
	 * the "class "/"interface " prefix before every class name.
	 * @param classes a Collection of Class objects (may be <code>null</code>)
	 * @return a String of form "[com.foo.Bar, com.foo.Baz]"
	 * @see java.util.AbstractCollection#toString()
	 */
	public static String classNamesToString(Class[] classes) {
		return classNamesToString(Arrays.asList(classes));
	}

	/**
	 * Build a String that consists of the names of the classes/interfaces
	 * in the given collection.
	 * <p>Basically like <code>AbstractCollection.toString()</code>, but stripping
	 * the "class "/"interface " prefix before every class name.
	 * @param classes a Collection of Class objects (may be <code>null</code>)
	 * @return a String of form "[com.foo.Bar, com.foo.Baz]"
	 * @see java.util.AbstractCollection#toString()
	 */
	public static String classNamesToString(Collection classes) {
		if (CollectionUtils.isEmpty(classes)) {
			return "[]";
		}
		StringBuffer sb = new StringBuffer("[");
		for (Iterator it = classes.iterator(); it.hasNext(); ) {
			Class clazz = (Class) it.next();
			sb.append(clazz.getName());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}


	/**
	 * Return all interfaces that the given instance implements as array,
	 * including ones implemented by superclasses.
	 * @param instance the instance to analyse for interfaces
	 * @return all interfaces that the given instance implements as array
	 */
	public static Class[] getAllInterfaces(Object instance) {
	
		return getAllInterfacesForClass(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as array,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param clazz the class to analyse for interfaces
	 * @return all interfaces that the given object implements as array
	 */
	public static Class[] getAllInterfacesForClass(Class clazz) {
	
		if (clazz.isInterface()) {
			return new Class[] {clazz};
		}
		List interfaces = new ArrayList();
		while (clazz != null) {
			for (int i = 0; i < clazz.getInterfaces().length; i++) {
				Class ifc = clazz.getInterfaces()[i];
				if (!interfaces.contains(ifc)) {
					interfaces.add(ifc);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
	}

	/**
	 * Return all interfaces that the given instance implements as Set,
	 * including ones implemented by superclasses.
	 * @param instance the instance to analyse for interfaces
	 * @return all interfaces that the given instance implements as Set
	 */
	public static Set getAllInterfacesAsSet(Object instance) {
	
		return getAllInterfacesForClassAsSet(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as Set,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param clazz the class to analyse for interfaces
	 * @return all interfaces that the given object implements as Set
	 */

	public static Set getAllInterfacesForClassAsSet(Class clazz) {
	
		if (clazz.isInterface()) {
			return Collections.singleton(clazz);
		}
		Set interfaces = new HashSet();
		while (clazz != null) {
			for (int i = 0; i < clazz.getInterfaces().length; i++) {
				Class ifc = clazz.getInterfaces()[i];
				interfaces.add(ifc);
			}
			clazz = clazz.getSuperclass();
		}
		return interfaces;
	}

	/**
	 * Create a composite interface Class for the given interfaces,
	 * implementing the given interfaces in one single Class.
	 * <p>This implementation builds a JDK proxy class for the given interfaces.
	 * @param interfaces the interfaces to merge
	 * @param classLoader the ClassLoader to create the composite Class in
	 * @return the merged interface as Class
	 * @see java.lang.reflect.Proxy#getProxyClass
	 */
	public static Class createCompositeInterface(Class[] interfaces, ClassLoader classLoader) {
	
		return Proxy.getProxyClass(classLoader, interfaces);
	}
	
	//获得类运行路径
	public static String getAppPath(Class cls){   
	    //检查用户传入的参数是否为空   
	    if(cls==null)    
	     throw new IllegalArgumentException("参数不能为空！");
	    ClassLoader loader=cls.getClassLoader();   
	    //获得类的全名，包括包名   
	    String clsName=cls.getName()+".class";   
	    //获得传入参数所在的包   
	    Package pack=cls.getPackage();   
	    String path="";   
	    //如果不是匿名包，将包名转化为路径   
	    if(pack!=null){   
	        String packName=pack.getName();   
	       //此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库   
	       if(packName.startsWith("java.")||packName.startsWith("javax."))    
	          throw new IllegalArgumentException("不要传送系统类！");
	        //在类的名称中，去掉包名的部分，获得类的文件名   
	        clsName=clsName.substring(packName.length()+1);   
	        //判定包名是否是简单包名，如果是，则直接将包名转换为路径，   
	        if(packName.indexOf(".")<0) path=packName+"/";   
	        else{//否则按照包名的组成部分，将包名转换为路径   
	            int start=0,end=0;   
	            end=packName.indexOf(".");   
	            while(end!=-1){   
	                path=path+packName.substring(start,end)+"/";   
	                start=end+1;   
	                end=packName.indexOf(".",start);   
	            }   
	            path=path+packName.substring(start)+"/";   
	        }   
	    }   
	    //调用ClassLoader的getResource方法，传入包含路径信息的类文件名   
	    java.net.URL url =loader.getResource(path+clsName);   
	    //从URL对象中获取路径信息   
	    String realPath=url.getPath();   
	    //去掉路径信息中的协议名"file:"   
	    int pos=realPath.indexOf("file:");   
	    if(pos>-1) realPath=realPath.substring(pos+5);   
	    //去掉路径信息最后包含类文件信息的部分，得到类所在的路径   
	    pos=realPath.indexOf(path+clsName);   
	    realPath=realPath.substring(0,pos-1);   
	    //如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名   
	    if(realPath.endsWith("!"))   
	        realPath=realPath.substring(0,realPath.lastIndexOf("/"));   
	  /*------------------------------------------------------------  
	   ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径  
	    中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要  
	    的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的  
	    中文及空格路径  
	  -------------------------------------------------------------*/  
	  try{   
	    realPath=java.net.URLDecoder.decode(realPath,"utf-8");   
	   }catch(Exception e){throw new RuntimeException(e);}   
	   return realPath;   
	}//getAppPath定义结束
}