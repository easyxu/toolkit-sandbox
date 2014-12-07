package com.phoenix.common.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@SuppressWarnings("unchecked")
public abstract class ReflectionUtils {

	private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

	public static Object newObjectInstance(String object, Object[] args)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		Class objectfactory = Class.forName(object);
		Class[] argsClass = new Class[args.length];
		Constructor cons = null;
		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();

		}

		cons = objectfactory.getConstructor(argsClass);
		return cons.newInstance(args);
	}
	
	public static Object invokeMethod(Object owner, String methodName,
			Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException
	{
		Method method = null;

		Class ownerClass = owner.getClass();
		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++)
		{
			argsClass[i] = args[i].getClass();

		}

		method = ownerClass.getDeclaredMethod(methodName, argsClass);

		return method.invoke(owner, args);
	}

	/**
	 * Attempt to find a {@link java.lang.reflect.Method} on the supplied type with the supplied
	 * name and parameter types. Searches all superclasses up to
	 * <code>Object</code>.
	 * <p>
	 * Returns <code>null</code> if no {@link java.lang.reflect.Method} can be found.
	 * 
	 * @param clazz
	 *            the class to introspect
	 * @param name
	 *            the name of the method
	 * @param paramTypes
	 *            the parameter types of the method
	 * @return the Method object, or <code>null</code> if none found
	 */
	public static Method findMethod(Class clazz, String name, Class[] paramTypes) {
	
		Class searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods());
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName())
						&& Arrays
								.equals(paramTypes, method.getParameterTypes())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Invoke the specified {@link java.lang.reflect.Method} against the supplied target object
	 * with no arguments. The target object can be <code>null</code> when
	 * invoking a static {@link java.lang.reflect.Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException}.
	 * 
	 * @param method
	 *            the method to invoke
	 * @param target
	 *            the target object to invoke the method on
	 * @return the invocation result, if any
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, null);
	}

	/**
	 * Invoke the specified {@link java.lang.reflect.Method} against the supplied target object
	 * with the supplied arguments. The target object can be <code>null</code>
	 * when invoking a static {@link java.lang.reflect.Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException}.
	 * 
	 * @param method
	 *            the method to invoke
	 * @param target
	 *            the target object to invoke the method on
	 * @param args
	 *            the invocation arguments (may be <code>null</code>)
	 * @return the invocation result, if any
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target,
			Object[] args) {
		try {
			return method.invoke(target, args);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - "
							+ ex.getClass().getName() + ": " + ex.getMessage());
		} catch (InvocationTargetException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - "
							+ ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 * 
	 * @param ex
	 *            the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: "
					+ ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: "
					+ ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		throw new IllegalStateException("Unexpected reflection exception - "
				+ ex.getClass().getName() + ": " + ex.getMessage());
	}

	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of such a root
	 * cause. Throws an IllegalStateException else.
	 * 
	 * @param ex
	 *            the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(
			InvocationTargetException ex) {
		if (ex.getTargetException() instanceof RuntimeException) {
			throw (RuntimeException) ex.getTargetException();
		}
		if (ex.getTargetException() instanceof Error) {
			throw (Error) ex.getTargetException();
		}
		throw new IllegalStateException(
				"Unexpected exception thrown by method - "
						+ ex.getTargetException().getClass().getName() + ": "
						+ ex.getTargetException().getMessage());
	}

	/**
	 * Determine whether the given method explicitly declares the given
	 * exception or one of its superclasses, which means that an exception of
	 * that type can be propagated as-is within a reflective invocation.
	 * 
	 * @param method
	 *            the declaring method
	 * @param exceptionType
	 *            the exception to throw
	 * @return <code>true</code> if the exception can be thrown as-is;
	 *         <code>false</code> if it needs to be wrapped
	 */
	public static boolean declaresException(Method method, Class exceptionType) {
	
		Class[] declaredExceptions = method.getExceptionTypes();
		for (int i = 0; i < declaredExceptions.length; i++) {
			Class declaredException = declaredExceptions[i];
			if (declaredException.isAssignableFrom(exceptionType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine whether the given field is a "public static final" constant.
	 * 
	 * @param field
	 *            the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier
				.isFinal(modifiers));
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The <code>setAccessible(true)</code> method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param field
	 *            the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>
	 * The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by a {@link com.phoenix.common.reflect.ReflectionUtils.MethodFilter}.
	 * 
	 * @param targetClass
	 *            class to start looking at
	 * @param mc
	 *            the callback to invoke for each method
	 * @see #doWithMethods(Class, com.phoenix.common.reflect.ReflectionUtils.MethodCallback, com.phoenix.common.reflect.ReflectionUtils.MethodFilter)
	 */
	public static void doWithMethods(Class targetClass, MethodCallback mc)
			throws IllegalArgumentException {
		doWithMethods(targetClass, mc, null);
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>
	 * The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by the specified {@link com.phoenix.common.reflect.ReflectionUtils.MethodFilter}.
	 * 
	 * @param targetClass
	 *            class to start looking at
	 * @param mc
	 *            the callback to invoke for each method
	 * @param mf
	 *            the filter that determines the methods to apply the callback
	 *            to
	 */
	public static void doWithMethods(Class targetClass, MethodCallback mc,
			MethodFilter mf) throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		do {
			Method[] methods = targetClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (mf != null && !mf.matches(methods[i])) {
					continue;
				}
				try {
					mc.doWith(methods[i]);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access method '"
									+ methods[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null);
	}

	/**
	 * Get all declared methods on the leaf class and all superclasses. Leaf
	 * class methods are included first.
	 */
	public static Method[] getAllDeclaredMethods(Class leafClass)
			throws IllegalArgumentException {
		final List list = new LinkedList();
		doWithMethods(leafClass, new MethodCallback() {
			public void doWith(Method m) {
				list.add(m);
			}
		});
		return (Method[]) list.toArray(new Method[list.size()]);
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the
	 * class hierarchy to get all declared fields.
	 * 
	 * @param targetClass
	 *            the target class to analyze
	 * @param fc
	 *            the callback to invoke for each field
	 */
	public static void doWithFields(Class targetClass, FieldCallback fc)
			throws IllegalArgumentException {
		doWithFields(targetClass, fc, null);
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the
	 * class hierarchy to get all declared fields.
	 * 
	 * @param targetClass
	 *            the target class to analyze
	 * @param fc
	 *            the callback to invoke for each field
	 * @param ff
	 *            the filter that determines the fields to apply the callback to
	 */
	public static void doWithFields(Class targetClass, FieldCallback fc,
			FieldFilter ff) throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		do {
			// Copy each field declared on this class unless it's static or
			// file.
			Field[] fields = targetClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// Skip static and final fields.
				if (ff != null && !ff.matches(fields[i])) {
					continue;
				}
				try {
					fc.doWith(fields[i]);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access field '"
									+ fields[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null && targetClass != Object.class);
	}

	/**
	 * Given the source object and the destination, which must be the same class
	 * or a subclass, copy all fields, including inherited fields. Designed to
	 * work on objects with public no-arg constructors.
	 * 
	 * @throws IllegalArgumentException
	 *             if the arguments are incompatible
	 */
	public static void shallowCopyFieldState(final Object src, final Object dest)
			throws IllegalArgumentException {
		if (src == null) {
			throw new IllegalArgumentException(
					"Source for field copy cannot be null");
		}
		if (dest == null) {
			throw new IllegalArgumentException(
					"Destination for field copy cannot be null");
		}
		if (!src.getClass().isAssignableFrom(dest.getClass())) {
			throw new IllegalArgumentException("Destination class ["
					+ dest.getClass().getName()
					+ "] must be same or subclass as source class ["
					+ src.getClass().getName() + "]");
		}
		doWithFields(src.getClass(), new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				makeAccessible(field);
				Object srcValue = field.get(src);
				field.set(dest, srcValue);
			}
		}, ReflectionUtils.COPYABLE_FIELDS);
	}

	/**
	 * Action to take on each method.
	 */
	public static interface MethodCallback {

		/**
		 * Perform an operation using the given method.
		 * 
		 * @param method
		 *            the method which will have been made accessible before
		 *            this invocation
		 */
		void doWith(Method method) throws IllegalArgumentException,
				IllegalAccessException;
	}

	/**
	 * Callback optionally used to method fields to be operated on by a method
	 * callback.
	 */
	public static interface MethodFilter {

		/**
		 * Determine whether the given method matches.
		 * 
		 * @param method
		 *            the method to check
		 */
		boolean matches(Method method);
	}

	/**
	 * Callback interface invoked on each field in the hierarchy.
	 */
	public static interface FieldCallback {

		/**
		 * Perform an operation using the given field.
		 * 
		 * @param field
		 *            the field which will have been made accessible before this
		 *            invocation
		 */
		void doWith(Field field) throws IllegalArgumentException,
				IllegalAccessException;
	}

	/**
	 * Callback optionally used to filter fields to be operated on by a field
	 * callback.
	 */
	public static interface FieldFilter {

		/**
		 * Determine whether the given field matches.
		 * 
		 * @param field
		 *            the field to check
		 */
		boolean matches(Field field);
	}

	/**
	 * Pre-built FieldFilter that matches all non-static, non-final fields.
	 */
	public static  FieldFilter COPYABLE_FIELDS = new FieldFilter() {
		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()) || Modifier
					.isFinal(field.getModifiers()));
		}
	};
	
	/**
     * Utility to essentially do Class forName and allow configurable
     * Classloaders.
     * <p>The initial implementation makes use of the context classloader for
     * the current thread.
     * @param className The class to create
     * @return The class if it is safe or null otherwise.
     * @throws ClassNotFoundException If <code>className</code> is not valid
     */
    public static Class classForName(String className) throws ClassNotFoundException
    {
        // Class.forName(className);
        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }

    /**
     * Calling methods using reflection is useful for graceful fallback - this
     * is a helper method to make this easy
     * @param object The object to use as 'this'
     * @param method The method to call, can be null in which case null is returned
     * @param params The parameters to pass to the reflection call
     * @return The results of calling method.invoke() or null
     * @throws IllegalStateException If anything goes wrong
     */
    public static Object invoke(Object object, Method method, Object[] params) throws IllegalStateException
    {
        Object reply = null;
        if (method != null)
        {
            try
            {
                reply = method.invoke(object, params);
            }
            catch (InvocationTargetException ex)
            {
                throw new IllegalStateException("InvocationTargetException calling " + method.getName() + ": " + ex.getTargetException().toString());
            }
            catch (Exception ex)
            {
                throw new IllegalStateException("Reflection error calling " + method.getName() + ": " + ex.toString());
            }
        }

        return reply;
    }

    /**
     * Utility to essentially do Class forName with the assumption that the
     * environment expects failures for missing jar files and can carry on if
     * this process fails.
     * @param name The name for debugging purposes
     * @param className The class to create
     * @param impl The implementation class - what should className do?
     * @return The class if it is safe or null otherwise.
     */
    public static Class classForName(String name, String className, Class impl)
    {
        Class clazz;

        try
        {
            clazz = classForName(className);
        }
        catch (ClassNotFoundException ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to ClassNotFoundException on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to NoClassDefFoundError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to TransformerFactoryConfigurationError on " + className + ". Cause: " + ex.getMessage());
            log.debug("Maybe you need to add xalan.jar to your webserver?");
            return null;
        }

        // Check it is of the right type
        if (!impl.isAssignableFrom(clazz))
        {
            log.error("Class '" + clazz.getName() + "' does not implement '" + impl.getName() + "'.");
            return null;
        }

        // Check we can create it
        try
        {
            clazz.newInstance();
        }
        catch (InstantiationException ex)
        {
            log.error("InstantiationException for '" + name + "' failed:", ex);
            return null;
        }
        catch (IllegalAccessException ex)
        {
            log.error("IllegalAccessException for '" + name + "' failed:", ex);
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to NoClassDefFoundError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to TransformerFactoryConfigurationError on " + className + ". Cause: " + ex.getMessage());
            log.debug("Maybe you need to add xalan.jar to your webserver?");
            return null;
        }
        catch (Exception ex)
        {
            // For some reason we can't catch this?
            if (ex instanceof ClassNotFoundException)
            {
                // We expect this sometimes, hence debug
                log.debug("Skipping '" + name + "' due to ClassNotFoundException on " + className + ". Cause: " + ex.getMessage());
                return null;
            }
            else
            {
                log.error("Failed to load '" + name + "' (" + className + ")", ex);
                return null;
            }
        }

        return clazz;
    }

    /**
     * Utility to essentially do Class forName and newInstance with the
     * assumption that the environment expects failures for missing jar files
     * and can carry on if this process fails.
     * @param name The name for debugging purposes
     * @param className The class to create
     * @param impl The implementation class - what should className do?
     * @return The new instance if it is safe or null otherwise.
     */
    public static Object classNewInstance(String name, String className, Class impl)
    {
        Class clazz;

        try
        {
            clazz = ReflectionUtils.classForName(className);
        }
        catch (ClassNotFoundException ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to ClassNotFoundException on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to NoClassDefFoundError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to TransformerFactoryConfigurationError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }

        // Check it is of the right type
        if (!impl.isAssignableFrom(clazz))
        {
            log.error("Class '" + clazz.getName() + "' does not implement '" + impl.getName() + "'.");
            return null;
        }

        // Check we can create it
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException ex)
        {
            log.error("InstantiationException for '" + name + "' failed:", ex);
            return null;
        }
        catch (IllegalAccessException ex)
        {
            log.error("IllegalAccessException for '" + name + "' failed:", ex);
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            log.error("TransformerFactoryConfigurationError for '" + name + "' failed:", ex);
            return null;
        }
        catch (Exception ex)
        {
            log.error("Failed to load creator '" + name + "', classname=" + className + ": ", ex);
            return null;
        }
    }

    /**
     * InputStream closer that can cope if the input stream is null.
     * If anything goes wrong, the errors are logged and ignored.
     * @param in The resource to close
     */
    public static void close(InputStream in)
    {
        if (in == null)
        {
            return;
        }

        try
        {
            in.close();
        }
        catch (IOException ex)
        {
            log.warn(ex.getMessage(), ex);
        }
    }

    /**
     * InputStream closer that can cope if the input stream is null.
     * If anything goes wrong, the errors are logged and ignored.
     * @param in The resource to close
     */
    public static void close(RandomAccessFile in)
    {
        if (in == null)
        {
            return;
        }

        try
        {
            in.close();
        }
        catch (IOException ex)
        {
            log.warn(ex.getMessage(), ex);
        }
    }

    /**
     * Return a List of superclasses for the given class.
     * @param clazz the class to look up
     * @return the List of superclasses in order going up from this one
     */
    public static List getAllSuperclasses(Class clazz)
    {
        List classes = new ArrayList();

        Class superclass = clazz.getSuperclass();
        while (superclass != null)
        {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }

        return classes;
    }

    /**
     * Return a list of all fields (whatever access status, and on whatever
     * superclass they were defined) that can be found on this class.
     * <p>This is like a union of {@link Class#getDeclaredFields()} which
     * ignores and superclasses, and {@link Class#getFields()} which ignored
     * non-pubic fields
     * @param clazz The class to introspect
     * @return The complete list of fields
     */
    public static Field[] getAllFields(Class clazz)
    {
        List classes = getAllSuperclasses(clazz);
        classes.add(clazz);
        return getAllFields(classes);
    }

    /**
     * As {@link #getAllFields(Class)} but acts on a list of {@link Class}s and
     * uses only {@link Class#getDeclaredFields()}.
     * @param classes The list of classes to reflect on
     * @return The complete list of fields
     */
    private static Field[] getAllFields(List classes)
    {
        Set fields = new HashSet();
        for (Iterator it = classes.iterator(); it.hasNext();)
        {
            Class clazz = (Class) it.next();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }

        return (Field[]) fields.toArray(new Field[fields.size()]);
    }
    
    /**
     * �򵥵�ת��
     * A very simple conversion function for all the IoC style setup and
     * reflection that we are doing.
     * @param value The value to convert
     * @param paramType The type to convert to. Currently ony primitive types and
     * String are supported.
     * @return The converted object.
     */
    public static Object simpleConvert(String value, Class paramType)
    {
        if (paramType == String.class)
        {
            return value;
        }

        if (paramType == Character.class || paramType == Character.TYPE)
        {
            if (value.length() == 1)
            {
                return new Character(value.charAt(0));
            }
            else
            {
                throw new IllegalArgumentException("Can't more than one character in string - can't convert to char: '" + value + "'");
            }
        }

        String trimValue = value.trim();

        if (paramType == Boolean.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Boolean.valueOf(trimValue);
        }

        if (paramType == Boolean.TYPE)
        {
            return Boolean.valueOf(trimValue);
        }

        if (paramType == Integer.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Integer.valueOf(trimValue);
        }

        if (paramType == Integer.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return new Integer(0);
            }

            return Integer.valueOf(trimValue);
        }

        if (paramType == Short.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Short.valueOf(trimValue);
        }

        if (paramType == Short.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return new Short((short) 0);
            }

            return Short.valueOf(trimValue);
        }

        if (paramType == Byte.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Byte.valueOf(trimValue);
        }

        if (paramType == Byte.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return new Byte((byte) 0);
            }

            return Byte.valueOf(trimValue);
        }

        if (paramType == Long.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Long.valueOf(trimValue);
        }

        if (paramType == Long.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return new Long(0);
            }

            return Long.valueOf(trimValue);
        }

        if (paramType == Float.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Float.valueOf(trimValue);
        }

        if (paramType == Float.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return new Float(0);
            }

            return Float.valueOf(trimValue);
        }

        if (paramType == Double.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return Double.valueOf(trimValue);
        }

        if (paramType == Double.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return new Double(0);
            }

            return Double.valueOf(trimValue);
        }

        throw new IllegalArgumentException("Unsupported conversion type: " + paramType.getName());
    }
    
    
    /**
     * Set a property on an object using reflection
     * @param object The object to call the setter on
     * @param key The name of the property to set.
     * @param value The new value to use for the property
     * @throws NoSuchMethodException Passed on from reflection code
     * @throws SecurityException Passed on from reflection code
     * @throws IllegalAccessException Passed on from reflection code
     * @throws IllegalArgumentException Passed on from reflection code
     * @throws java.lang.reflect.InvocationTargetException Passed on from reflection code
     */
    public static void setProperty(Object object, String key, Object value) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Class real = object.getClass();

        String setterName = "set" + key.substring(0, 1).toUpperCase(Locale.ENGLISH) + key.substring(1);

        try
        {
            // Can we work with whatever type we were given?
            Method method = real.getMethod(setterName, new Class[] { value.getClass() });
            method.invoke(object, new Object[] { value });
            return;
        }
        catch (NoSuchMethodException ex)
        {
            // If it is a string then next we try to coerce it to the right type
            // otherwise we give up.
            if (!(value instanceof String))
            {
                throw ex;
            }
        }

        Method[] methods = real.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method setter = methods[i];

            if (setter.getName().equals(setterName) && setter.getParameterTypes().length == 1)
            {
                Class propertyType = setter.getParameterTypes()[0];
                try
                {
                    Object param = ReflectionUtils.simpleConvert((String) value, propertyType);
                    setter.invoke(object, new Object[] { param });
                    return;
                }
                catch (IllegalArgumentException ex)
                {
                    // The conversion failed - it was speculative anyway so we
                    // don't worry now
                }
            }
        }

        throw new NoSuchMethodException("Failed to find a property called: " + key + " on " + object.getClass().getName());
    }
    /**
     * Set use reflection to set the setters on the object called by the keys
     * in the params map with the corresponding values
     * @param object The object to setup
     * @param params The settings to use
     * @param ignore List of keys to not warn about if they are not properties
     *               Note only the warning is skipped, we still try the setter
     */
    public static void setParams(Object object, Map params, List ignore)
    {
        for (Iterator it = params.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            try
            {
                setProperty(object, key, value);
            }
            catch (NoSuchMethodException ex)
            {
                if (ignore != null && !ignore.contains(key))
                {
                    log.warn("No property '" + key + "' on " + object.getClass().getName());
                }
            }
            catch (InvocationTargetException ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + object.getClass().getName(), ex.getTargetException());
            }
            catch (Exception ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + object.getClass().getName(), ex);
            }
        }
    }
    
    
   
    public static boolean isJavaIdentifier(String test)
    {
        if (test == null || test.length() == 0)
        {
            return false;
        }

        if (!Character.isJavaIdentifierStart(test.charAt(0)) && test.charAt(0) != '_')
        {
            return false;
        }

        for (int i = 1; i < test.length(); i++)
        {
            if (!Character.isJavaIdentifierPart(test.charAt(i)) && test.charAt(i) != '_')
            {
                return false;
            }
        }

        return true;
    }
   
    public static boolean isLetterOrDigitOrUnderline(String test)
    {
        if (test == null || test.length() == 0)
        {
            return false;
        }

        for (int i = 0; i < test.length(); i++)
        {
            if (!Character.isLetterOrDigit(test.charAt(i)) && test.charAt(i) != '_')
            {
                return false;
            }
        }

        return true;
    }
}
