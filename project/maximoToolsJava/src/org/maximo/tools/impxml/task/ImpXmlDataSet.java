package org.maximo.tools.impxml.task;

import java.util.Iterator;
import java.util.List;

import org.maximo.app.MTException;

public interface ImpXmlDataSet {

	/** 
	 * @return
	 */
	public boolean hasNext();

	public Row next() throws MTException;

	/**
	 * 獲取主表
	 * @return 
	 */
	public Table getRootImpXmlTable();

}
