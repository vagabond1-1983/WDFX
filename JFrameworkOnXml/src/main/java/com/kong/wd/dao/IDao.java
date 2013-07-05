package com.kong.wd.dao;

import java.io.File;

import com.kong.wd.model.TestDataCollection;

public interface IDao {
	public TestDataCollection parse(File file);
}
