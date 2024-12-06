package net.eq2online.macros.scripting.api;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.eq2online.macros.scripting.ScriptCore;

public class ReturnValueArray implements IReturnValueArray
{
	private List<Boolean> bools = new LinkedList<Boolean>();
	
	private List<Integer> ints = new LinkedList<Integer>();
	
	private List<String> strings = new LinkedList<String>();

	private boolean append;
	
	public ReturnValueArray(boolean append)
	{
		this.append = append;
	}
	
	public void putStrings(List<String> strings)
	{
		this.strings = strings;
		this.ints.clear();
		this.bools.clear();
		
		for (String value : this.strings)
		{
			int intVal = ScriptCore.tryParseInt(value, 0);
			this.ints.add(intVal);
			this.bools.add(value == null || value.toLowerCase().equals("true") || intVal != 0);
		}
	}
	
	public void putInts(List<Integer> ints)
	{
		this.ints = ints;
		this.strings.clear();
		this.bools.clear();

		for (Integer value : this.ints)
		{
			this.strings.add(String.valueOf(value));
			this.bools.add(value != 0);
		}		
	}
	
	public void putBools(List<Boolean> bools)
	{
		this.bools = bools;
		this.strings.clear();
		this.ints.clear();
		
		for (Boolean value : this.bools)
		{
			this.strings.add(value ? "True" : "False");
			this.ints.add(value ? 1 : 0);
		}
	}
	
	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	@Override
	public boolean getBoolean()
	{
		return this.bools.size() > 0 ? this.bools.get(0) : false;
	}
	
	@Override
	public int getInteger()
	{
		return this.ints.size() > 0 ? this.ints.get(0) : 0;
	}
	
	@Override
	public String getString()
	{
		return this.strings.size() > 0 ? this.strings.get(0) : "";
	}
	
	@Override
	public String getLocalMessage()
	{
		return null;
	}
	
	@Override
	public String getRemoteMessage()
	{
		return null;
	}
	
	@Override
	public int size()
	{
		return this.strings.size();
	}
	
	@Override
	public boolean shouldAppend()
	{
		return this.append;
	}
	
	@Override
	public List<Boolean> getBooleans()
	{
		return Collections.unmodifiableList(this.bools);
	}
	
	@Override
	public List<Integer> getIntegers()
	{
		return Collections.unmodifiableList(this.ints);
	}
	
	@Override
	public List<String> getStrings()
	{
		return Collections.unmodifiableList(this.strings);
	}
	
}
