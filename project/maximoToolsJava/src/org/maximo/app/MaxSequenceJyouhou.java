package org.maximo.app;

public class MaxSequenceJyouhou {
	/**
	 * 表名称
	 */
	private String tbName;
	/**
	 * 唯一列名
	 */
	private String name;
	private String maxReserved;
	private String maxValue;
	private String range;
	/**
	 * 序列名
	 */
	private String sequenceName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaxReserved() {
		return maxReserved;
	}
	public void setMaxReserved(String maxReserved) {
		this.maxReserved = maxReserved;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getSequenceName() {
		return sequenceName;
	}
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	public String getTbName() {
		return tbName;
	}
	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

}
