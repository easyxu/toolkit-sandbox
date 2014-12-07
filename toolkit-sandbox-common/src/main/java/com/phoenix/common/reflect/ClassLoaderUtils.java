package com.phoenix.common.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.phoenix.exceptions.ChainedException;





@SuppressWarnings("unchecked")
public class ClassLoaderUtils 
{
	public static URL getResource(String resourceName, Class callingClass)
	{
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				resourceName);

		if (url == null)
		{
			url = ClassLoaderUtils.class.getClassLoader().getResource(
					resourceName);
		}

		if (url == null)
		{
			ClassLoader cl = callingClass.getClassLoader();

			if (cl != null)
			{
				url = cl.getResource(resourceName);
			}
		}

		if ((url == null) && (resourceName != null)
				&& (resourceName.charAt(0) != '/'))
		{
			return getResource('/' + resourceName, callingClass);
		}

		return url;
	}

	public static InputStream getResourceAsStream(String resourceName,
			Class callingClass)
	{
		URL url = getResource(resourceName, callingClass);

		try
		{
			return (url != null) ? url.openStream() : null;
		}
		catch (IOException e)
		{
			return null;
		}
	}


	public static Class loadClass(String className, Class callingClass)
			throws ClassNotFoundException
	{
		try
		{
			ClassLoader cl = Thread.currentThread().getContextClassLoader();

			if (cl != null)
				return cl.loadClass(className);

			return loadClass2(className, callingClass);
		}
		catch (ClassNotFoundException e)
		{
			return loadClass2(className, callingClass);
		}
	}

	private static Class loadClass2(String className, Class callingClass)
			throws ClassNotFoundException
	{
		try
		{
			return Class.forName(className);
		}
		catch (ClassNotFoundException ex)
		{
			try
			{
				return ClassLoaderUtils.class.getClassLoader().loadClass(
						className);
			}
			catch (ClassNotFoundException exc)
			{
				return callingClass.getClassLoader().loadClass(className);
			}
		}
	}
	public static ClassLoader getStandardClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader getFallbackClassLoader()
    {
        return (ClassLoaderUtils.class).getClassLoader();
    }

	public static Object createNewInstance(String className) throws ChainedException
         
    {
        Class clazz;
        try
        {
            clazz = Class.forName(className, true, getStandardClassLoader());
        }
        catch(ClassNotFoundException e)
        {
            try
            {
                clazz = Class.forName(className, true, getFallbackClassLoader());
            }
            catch(ClassNotFoundException ex)
            {
                throw new ChainedException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
            }
        }
        Object newInstance;
        try
        {
            newInstance = clazz.newInstance();
        }
        catch(IllegalAccessException e)
        {
            throw new ChainedException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        }
        catch(InstantiationException e)
        {
            throw new ChainedException("Unable to load class " + className + ". Initial cause was " + e.getMessage(), e);
        }
        return newInstance;
    }
    
   
}
