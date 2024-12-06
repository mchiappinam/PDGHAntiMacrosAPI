package net.eq2online.macros.compatibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.eq2online.console.Log;
import net.eq2online.macros.scripting.IErrorLogger;
import net.minecraft.client.ClientBrandRetriever;

/**
 * Reflection functions
 *
 * @author Adam Mummery-Smith
 */
public class Reflection
{
	/**
	 * Reference to the modifiers field within Field
	 */
	private static Field fieldModifiers = null;
	
	/**
	 * True if FML is detected, signals use of Searge names for reflection
	 */
	private static boolean forgeModLoader = false;
	
	static
	{
		// Look for FML since we need to use Searge field names for reflection if running alongside forge
		forgeModLoader = ClientBrandRetriever.getClientModName().contains("fml");
		
		try
		{
			fieldModifiers = (java.lang.reflect.Field.class).getDeclaredField("modifiers");
			fieldModifiers.setAccessible(true);
		}
		catch (Throwable th) {}
	}
	
	/**
	 * ModLoader function, set a private value on the specified object
	 * 
	 * @param instanceClass Class which the member belongs to (may be a superclass of the actual instance object's class)
	 * @param instance Object instance to set the value in
	 * @param fieldName Name of the field to set
	 * @param value Value to set for the field
	 */
	public static void setPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName, String seargeFieldName, Object value) throws IllegalArgumentException, SecurityException, NoSuchFieldException
	{
		setPrivateValueInternal(instanceClass, instance, getObfuscatedFieldName(fieldName, obfuscatedFieldName, seargeFieldName), value);
	}
	
	/**
	 * ModLoader function, set a private value on the specified object
	 * 
	 * @param instanceClass Class which the member belongs to (may be a superclass of the actual instance object's class)
	 * @param instance Object instance to set the value in
	 * @param fieldName Name of the field to set
	 * @param value Value to set for the field
	 */
	public static void setPrivateValue(Class<?> instanceClass, Object instance, String fieldName, Object value) throws IllegalArgumentException, SecurityException, NoSuchFieldException
	{
		setPrivateValueInternal(instanceClass, instance, fieldName, value);
	}
	
	/**
	 * ModLoader function, get a private value from the specified object
	 * 
	 * @param instanceClass Class which the member belongs to (may be a superclass of the actual instance object's class)
	 * @param instance Object instance to get the value from
	 * @param fieldName Name of the field to get the value of
	 * @return Value of the field
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName, String obfuscatedFieldName, String seargeFieldName) throws IllegalArgumentException, SecurityException, NoSuchFieldException
	{
		return (T)getPrivateValueInternal(instanceClass, instance, getObfuscatedFieldName(fieldName, obfuscatedFieldName, seargeFieldName));
	}
	
	/**
	 * ModLoader function, get a private value from the specified object
	 * 
	 * @param instanceClass Class which the member belongs to (may be a superclass of the actual instance object's class)
	 * @param instance Object instance to get the value from
	 * @param fieldName Name of the field to get the value of
	 * @return Value of the field
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPrivateValue(Class<?> instanceClass, Object instance, String fieldName) throws IllegalArgumentException, SecurityException, NoSuchFieldException
	{
		return (T)getPrivateValueInternal(instanceClass, instance, fieldName);
	}
	
	/**
	 * Abstraction helper function for use with GetPrivateValue and SetPrivateValue, this returns obfuscated
	 * field names when running in obfuscated mode, or normal MCP field names if running inside the debugger
	 * 
	 * @param fieldName Name of field to get, returned unmodified if in debug mode
	 * @return Obfuscated field name if present
	 */
	private static String getObfuscatedFieldName(String fieldName, String obfuscatedFieldName, String seargeFieldName)
	{
		if (forgeModLoader) return seargeFieldName;
		return (!net.minecraft.client.renderer.Tessellator.instance.getClass().getSimpleName().equals("Tessellator")) ? obfuscatedFieldName : fieldName;
	}
	
	/**
	 * Non-generic reflective field set method
	 * 
	 * @param instanceClass
	 * @param instance
	 * @param fieldName
	 * @param value
	 */
	private static void setPrivateValueInternal(Class<?> instanceClass, Object instance, String fieldName, Object value) throws IllegalArgumentException, SecurityException, NoSuchFieldException
	{
		try
		{
			Field field = instanceClass.getDeclaredField(fieldName);
			int fieldFieldModifiers = fieldModifiers.getInt(field);
			
			if ((fieldFieldModifiers & Modifier.FINAL) != 0)
			{
				fieldModifiers.setInt(field, fieldFieldModifiers & ~Modifier.FINAL);
			}
			
			field.setAccessible(true);
			field.set(instance, value);
		}
		catch (IllegalAccessException illegalaccessexception)
		{
		}
	}
	
	/**
	 * @param instanceClass
	 * @param instance
	 * @param fieldName
	 * @return
	 */
	public static Object getPrivateValueInternal(Class<?> instanceClass, Object instance, String fieldName) throws IllegalArgumentException, SecurityException, NoSuchFieldException
	{
		try
		{
			Field field = instanceClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		}
		catch (IllegalAccessException illegalaccessexception)
		{
			return null;
		}
	}

	/**
	 * @param packageClass
	 * @return
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 */
	public static File getPackagePath(Class<?> packageClass) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException
	{
		File packagePath = null;
		
		URL protectionDomainLocation = packageClass.getProtectionDomain().getCodeSource().getLocation();
		if (protectionDomainLocation != null)
		{
			if (protectionDomainLocation.toString().indexOf('!') > -1 && protectionDomainLocation.toString().startsWith("jar:"))
			{
				protectionDomainLocation = new URL(protectionDomainLocation.toString().substring(4, protectionDomainLocation.toString().indexOf('!')));
			}
			
			packagePath = new File(protectionDomainLocation.toURI());
		}
		else
		{
			// Fix (?) for forge and other mods which screw up the protection domain 
			String reflectionClassPath = Reflection.class.getResource("/" + Reflection.class.getName().replace('.', '/') + ".class").getPath();
			
			if (reflectionClassPath.indexOf('!') > -1)
			{
				reflectionClassPath = URLDecoder.decode(reflectionClassPath, "UTF-8");
				packagePath = new File(reflectionClassPath.substring(5, reflectionClassPath.indexOf('!')));
			}
		}
		
		if (packagePath != null && packagePath.isFile() && packagePath.getName().endsWith(".class"))
		{
			String discoveredPath = "";
			String absolutePath = packagePath.getAbsolutePath();
			String classPath = System.getProperty("java.class.path");
			String classPathSeparator = System.getProperty("path.separator");
			String[] classPathEntries = classPath.split(classPathSeparator);
			
			for (String classPathEntry : classPathEntries)
			{
				try
				{
					String classPathPart = new File(classPathEntry).getAbsolutePath();
					if (absolutePath.startsWith(classPathPart) && classPathPart.length() > discoveredPath.length())
						discoveredPath = classPathPart;
				}
				catch (Exception ex) {}
			}
			
			if (discoveredPath.length() > 0)
			{
				packagePath = new File(discoveredPath);
			}
		}
		
		return packagePath;
	}
	
	/**
	 * Enumerate classes on the classpath which are subclasses of the specified class
	 * 
	 * @param superClass
	 * @return
	 */
	public static <T> LinkedList<Class<? extends T>> getSubclassesFor(Class<T> superClass, Class<?> packageClass, String prefix, IErrorLogger logger)
	{
		try
		{
			File packagePath = Reflection.getPackagePath(packageClass);
			
			if (packagePath != null)
			{
				LinkedList<Class<? extends T>> classes = new LinkedList<Class<? extends T>>();
				ClassLoader classloader = superClass.getClassLoader();
				
				if (packagePath.isDirectory())
				{
					enumerateDirectory(prefix, superClass, classloader, classes, packagePath, logger);
				}
				else if (packagePath.isFile() && (packagePath.getName().endsWith(".jar") || packagePath.getName().endsWith(".zip") || packagePath.getName().endsWith(".litemod")))
				{
					enumerateCompressedPackage(prefix, superClass, classloader, classes, packagePath, logger);
				}
				
				return classes;
			}
		}
		catch (Throwable th)
		{
			Log.printStackTrace(th);
			if (logger != null && th.getMessage() != null) logger.logError(th.getClass().getSimpleName() + " " + th.getMessage());			
		}
		
		return new LinkedList<Class<? extends T>>();
	}
	
	/**
	 * @param superClass
	 * @param classloader
	 * @param classes
	 * @param packagePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected static <T> void enumerateCompressedPackage(String prefix, Class<T> superClass, ClassLoader classloader, LinkedList<Class<? extends T>> classes, File packagePath, IErrorLogger logger) throws FileNotFoundException, IOException
	{
		FileInputStream fileinputstream = new FileInputStream(packagePath);
		ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
		
		ZipEntry zipentry = null;
		
		do
		{
			zipentry = zipinputstream.getNextEntry();
			
			if (zipentry != null && zipentry.getName().endsWith(".class"))
			{
				String classFileName = zipentry.getName();
				String className = classFileName.lastIndexOf('/') > -1 ? classFileName.substring(classFileName.lastIndexOf('/') + 1) : classFileName;
				
				if (prefix == null || className.startsWith(prefix))
				{
					try
					{
						String fullClassName = classFileName.substring(0, classFileName.length() - 6).replaceAll("/", ".");
						checkAndAddClass(classloader, superClass, classes, fullClassName, logger);
					}
					catch (Exception ex) {}
				}
			}
		}
		while (zipentry != null);
		
		fileinputstream.close();
	}
	
	/**
	 * Recursive function to enumerate classes inside a classpath folder
	 * 
	 * @param superClass
	 * @param classloader
	 * @param classes
	 * @param packagePath
	 * @param packageName
	 */
	private static <T> void enumerateDirectory(String prefix, Class<T> superClass, ClassLoader classloader, LinkedList<Class<? extends T>> classes, File packagePath, IErrorLogger logger)
	{
		enumerateDirectory(prefix, superClass, classloader, classes, packagePath, "", logger);
	}
	
	/**
	 * Recursive function to enumerate classes inside a classpath folder
	 * 
	 * @param superClass
	 * @param classloader
	 * @param classes
	 * @param packagePath
	 * @param packageName
	 */
	private static <T> void enumerateDirectory(String prefix, Class<T> superClass, ClassLoader classloader, LinkedList<Class<? extends T>> classes, File packagePath, String packageName, IErrorLogger logger)
	{
		File[] classFiles = packagePath.listFiles();
		
		for (File classFile : classFiles)
		{
			if (classFile.isDirectory())
			{
				enumerateDirectory(prefix, superClass, classloader, classes, classFile, packageName + classFile.getName() + ".", logger);
			}
			else
			{
				if (classFile.getName().endsWith(".class") && (prefix == null || classFile.getName().startsWith(prefix)))
				{
					String classFileName = classFile.getName();
					String className = packageName + classFileName.substring(0, classFileName.length() - 6);
					checkAndAddClass(classloader, superClass, classes, className, logger);
				}
			}
		}
	}
	
	/**
	 * @param classloader
	 * @param superClass
	 * @param classes
	 * @param className
	 */
	@SuppressWarnings("unchecked")
	protected static <T> void checkAndAddClass(ClassLoader classloader, Class<T> superClass, LinkedList<Class<? extends T>> classes, String className, IErrorLogger logger)
	{
		if (className.indexOf('$') > -1) return;
		
		try
		{
			Class<?> subClass = classloader.loadClass(className);
			
			if (subClass != null && !superClass.equals(subClass) && superClass.isAssignableFrom(subClass) && !classes.contains(subClass))
			{
				classes.add((Class<? extends T>)subClass);
			}
		}
		catch (Throwable th) {}
	}
}
