package net.eq2online.macros.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.eq2online.console.Log;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacroEventProvider;
import net.eq2online.macros.scripting.api.IMacrosAPIModule;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.api.IVariableProvider;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class ModuleLoader
{
	/**
	 * Directory containing script addon modules
	 */
	private final File modulesDir;
	
	public ModuleLoader(File macrosPath)
	{
		this.modulesDir = new File(macrosPath, "/modules");
		
		try
		{
			this.modulesDir.mkdirs();
		}
		catch (Exception ex) {}
	}

	/**
	 * Load the script modules
	 */
	public void loadModules(IErrorLogger logger)
	{
		if (this.modulesDir.exists() && this.modulesDir.isDirectory())
		{
			try
			{
				LaunchClassLoader classLoader = (LaunchClassLoader)ScriptCore.class.getClassLoader();
	
				// Enumerate module files
				ArrayList<File> moduleFiles = new ArrayList<File>();
				
				for (File file : this.modulesDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) { return name.startsWith("module_") && (name.endsWith(".zip") || name.endsWith(".jar")); }
					}
				))
				{
					moduleFiles.add(file);
				}
				
				// Add modules files to class path
				for (File module : moduleFiles)
				{
					if (module.isFile())
					{
						classLoader.addURL(module.toURI().toURL());
					}
				}
				
				// Load actions and providers from modules
				for (File module : moduleFiles)
				{
					LoadedModuleInfo lmi = new LoadedModuleInfo(module);
					
					if (module.isFile() && module.getName().startsWith("module_"));
					{
						ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(module));
						
						do
						{
							ZipEntry zipentry = zipinputstream.getNextEntry();
							if (zipentry == null) break;
							
							String className = new File(zipentry.getName()).getName();
							
							if (!zipentry.isDirectory() && className.endsWith(".class") && !className.contains("$"))
							{
								String fullClassName = zipentry.getName().split("\\.")[0];
								IMacrosAPIModule newModule = null;
								
								if (className.startsWith("ScriptAction"))
								{
									newModule = lmi.addAction(this.addModule(classLoader, IScriptAction.class, "action", fullClassName, logger));
								}
								else if (className.startsWith("VariableProvider"))
								{
									newModule = lmi.addProvider(this.addModule(classLoader, IVariableProvider.class, "variable provider", fullClassName, logger));
								}
								else if (className.startsWith("ScriptedIterator"))
								{
									newModule = lmi.addIterator(this.addModule(classLoader, IScriptedIterator.class, "iterator", fullClassName, logger));
								}
								else if (className.startsWith("EventProvider"))
								{
									newModule = lmi.addEventProvider(this.addModule(classLoader, IMacroEventProvider.class, "event provider", fullClassName, logger));
								}
								else
								{
									continue;
								}
								
								if (newModule == null)
								{
									if (logger != null) logger.logError("API: Error initialising " + module.getName());
									break;
								}
							}
						}
						while (true);
						
						zipinputstream.close();
					}
					
					lmi.printStatus();
				}
			}
			catch (Throwable th)
			{
			}
		}
	}

	/**
	 * Add a new action class from a module
	 * 
	 * @param classLoader
	 * @param className
	 */
	private <ModuleType extends IMacrosAPIModule> ModuleType addModule(ClassLoader classLoader, Class<ModuleType> moduleClassType, String moduleType, String className, IErrorLogger logger)
	{
		try
		{
			className = className.replace('/', '.');
			Class<?> moduleClass = classLoader.loadClass(className);
			
			if (moduleClassType.isAssignableFrom(moduleClass))
			{
				if (!ModuleLoader.checkAPIVersion(moduleClass))
				{
					Log.info("Macros: API Error. Not loading custom {0} in {1}, bad API version.", moduleType, className);
					if (logger != null) logger.logError("API: Not loading " + moduleClass.getSimpleName() + ", bad API version");
					return null;
				}
				
				@SuppressWarnings("unchecked")
				ModuleType customScriptAction = (ModuleType)moduleClass.newInstance();
				
				if (customScriptAction != null)
				{
					customScriptAction.onInit();
					return customScriptAction;
				}
			}
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}

	/**
	 * If you can't work out what this function does then there's something wrong with your head
	 * 
	 * @param moduleClass
	 * @return
	 */
	private static final boolean checkAPIVersion(Class<?> moduleClass)
	{
		APIVersion versionAnnotation = moduleClass.getAnnotation(APIVersion.class);
		return (versionAnnotation != null && versionAnnotation.value() == 15); // TODO API Version
	}
}
