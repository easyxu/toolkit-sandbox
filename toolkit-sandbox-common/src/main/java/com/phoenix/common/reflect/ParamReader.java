package com.phoenix.common.reflect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ParamReader extends ClassReader
{
	private String	methodName;

	private Map		methods	= new HashMap();

	private Class[]	paramTypes;

	/**
	 * process a class file, given it's class. We'll use the defining
	 * classloader to locate the bytecode.
	 * 
	 * @param c
	 * @throws java.io.IOException
	 */
	public ParamReader(Class c) throws IOException
	{
		this(getBytes(c));
	}

	/**
	 * process the given class bytes directly.
	 * 
	 * @param b
	 * @throws java.io.IOException
	 */
	public ParamReader(byte[] b) throws IOException
	{
		super(b, findAttributeReaders(ParamReader.class));

		// check the magic number
		if (readInt() != 0xCAFEBABE)
		{
			// not a class file!
			throw new IOException();// Messages.getMessage("badClassFile00"));
		}

		readShort(); // minor version
		readShort(); // major version

		readCpool(); // slurp in the constant pool

		readShort(); // access flags
		readShort(); // this class name
		readShort(); // super class name

		int count = readShort(); // ifaces count
		for (int i = 0; i < count; i++)
		{
			readShort(); // interface index
		}

		count = readShort(); // fields count
		for (int i = 0; i < count; i++)
		{
			readShort(); // access flags
			readShort(); // name index
			readShort(); // descriptor index
			skipAttributes(); // field attributes
		}

		count = readShort(); // methods count
		for (int i = 0; i < count; i++)
		{
			readShort(); // access flags
			int m = readShort(); // name index
			String name = resolveUtf8(m);
			int d = readShort(); // descriptor index
			this.methodName = name + resolveUtf8(d);
			readAttributes(); // method attributes
		}

	}

	/**
	 * Retrieve a list of function parameter names from a method Returns null if
	 * unable to read parameter names (i.e. bytecode not built with debug).
	 */
	public static String[] getParameterNamesFromDebugInfo(Method method)
	{
		// Don't worry about it if there are no params.
		int numParams = method.getParameterTypes().length;
		if (numParams == 0)
			return null;

		// get declaring class
		Class c = method.getDeclaringClass();

		// Don't worry about it if the class is a Java dynamic proxy
		if (Proxy.isProxyClass(c))
		{
			return null;
		}

		try
		{
			// get a parameter reader
			ParamReader pr = new ParamReader(c);
			// get the paramter names
			String[] names = pr.getParameterNames(method);
			return names;
		}
		catch (IOException e)
		{
			// log it and leave
			// log.info(Messages.getMessage("error00") + ":" + e);
			return null;
		}
	}

	public void readCode() throws IOException
	{
		readShort(); // max stack
		int maxLocals = readShort(); // max locals

		MethodInfo info = new MethodInfo(maxLocals);
		if (methods != null && methodName != null)
		{
			methods.put(methodName, info);
		}

		skipFully(readInt()); // code
		skipFully(8 * readShort()); // exception table
		// read the code attributes (recursive). This is where
		// we will find the LocalVariableTable attribute.
		readAttributes();
	}

	/**
	 * return the names of the declared parameters for the given constructor. If
	 * we cannot determine the names, return null. The returned array will have
	 * one name per parameter. The length of the array will be the same as the
	 * length of the Class[] array returned by Constructor.getParameterTypes().
	 * 
	 * @param ctor
	 * @return String[] array of names, one per parameter, or null
	 */
	public String[] getParameterNames(Constructor ctor)
	{
		paramTypes = ctor.getParameterTypes();
		return getParameterNames(ctor, paramTypes);
	}

	/**
	 * return the names of the declared parameters for the given method. If we
	 * cannot determine the names, return null. The returned array will have one
	 * name per parameter. The length of the array will be the same as the
	 * length of the Class[] array returned by Method.getParameterTypes().
	 * 
	 * @param method
	 * @return String[] array of names, one per parameter, or null
	 */
	public String[] getParameterNames(Method method)
	{
		paramTypes = method.getParameterTypes();
		return getParameterNames(method, paramTypes);
	}

	protected String[] getParameterNames(Member member, Class[] paramTypes)
	{
		// look up the names for this method
		MethodInfo info = (MethodInfo) methods.get(getSignature(member,
				paramTypes));

		// we know all the local variable names, but we only need to return
		// the names of the parameters.

		if (info != null)
		{
			String[] paramNames = new String[paramTypes.length];
			int j = Modifier.isStatic(member.getModifiers()) ? 0 : 1;

			boolean found = false; // did we find any non-null names
			for (int i = 0; i < paramNames.length; i++)
			{
				if (info.names[j] != null)
				{
					found = true;
					paramNames[i] = info.names[j];
				}
				j++;
				if (paramTypes[i] == double.class
						|| paramTypes[i] == long.class)
				{
					// skip a slot for 64bit params
					j++;
				}
			}

			if (found)
			{
				return paramNames;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	private static class MethodInfo
	{
		String[]	names;

		int			maxLocals;

		public MethodInfo(int maxLocals)
		{
			this.maxLocals = maxLocals;
			names = new String[maxLocals];
		}
	}

	private MethodInfo getMethodInfo()
	{
		MethodInfo info = null;
		if (methods != null && methodName != null)
		{
			info = (MethodInfo) methods.get(methodName);
		}
		return info;
	}

	/**
	 * this is invoked when a LocalVariableTable attribute is encountered.
	 * 
	 * @throws java.io.IOException
	 */
	public void readLocalVariableTable() throws IOException
	{
		int len = readShort(); // table length
		MethodInfo info = getMethodInfo();
		for (int j = 0; j < len; j++)
		{
			readShort(); // start pc
			readShort(); // length
			int nameIndex = readShort(); // name_index
			readShort(); // descriptor_index
			int index = readShort(); // local index
			if (info != null)
			{
				info.names[index] = resolveUtf8(nameIndex);
			}
		}
	}
}
