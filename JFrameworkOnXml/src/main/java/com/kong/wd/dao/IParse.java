package com.kong.wd.dao;

import com.kong.wd.model.Settings;
import com.kong.wd.model.TestItem;

//Do not know how to define the interface
public interface IParse<T> {
	public T findTargetUnderRoot(String strTarget);
	public Settings parseSettings(T settings);
	public TestItem parseTest(T testElm);
}
