package org.shoukaiseki.gui.tuuyou;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
/**
 * 渲染使用的对象
 * @author 蒋カイセキ
 *
 */
public class StringIcon implements Cloneable{
	/**
	 * 唯一ID,比较此值,判断是否为此监听对象
	 */
	private static int idEQU = 0;
	private int ID = 0;
	public String name;
	public String toolTipText;//设置工具提示字符串
	
	public ImageIcon icon;
	public ImageIcon closedIcon;// 用于显示无扩展的非叶节点的图标
	public ImageIcon closedIconSelected;// 父节点关闭时选中图标
	public ImageIcon openIcon;// 用于显示扩展的非叶节点的图标
	public ImageIcon openIconSelected;// 父节点展开后选中图标
	public ImageIcon leafIcon;// 用于显示叶节点的图标
	public ImageIcon leafIconSelected;// 子节点选中图标

	public Color BackgroundSelectionColor;//选中背景色
	public Color ForegroundSelectionColor;//选中前景色
	public Color BackgroundNonSelectionColor;//未选中时背景色
	public Color ForegroundNonSelectionColor;//未选中背景色
	public Object object;//预留
	
	public StringIcon(String name, ImageIcon icon) {
		ID = ++idEQU;
		this.name = name;
		this.icon = icon;
	}
	/**
	 * 使用StringIcon(String name, ImageIcon icon)实例化将采用默认的图标
	 * @param name					显示名称
	 * @param closedIcon			用于显示无扩展的非叶节点的图标
	 * @param closedIconselected	父节点关闭时选中图标
	 * @param openIcon				用于显示扩展的非叶节点的图标
	 * @param openIconselected		父节点展开后选中图标
	 * @param leafIcon				用于显示叶节点的图标
	 * @param leafIconselected		子节点选中图标
	 */
	public StringIcon(String name, ImageIcon closedIcon,
			ImageIcon closedIconSelected, ImageIcon openIcon,
			ImageIcon openIconSelected, ImageIcon leafIcon,
			ImageIcon leafIconSelected) {
		ID = ++idEQU;
		this.name = name;
		this.closedIcon = closedIcon;
		this.closedIconSelected = closedIconSelected;
		this.openIcon = openIcon;
		this.openIconSelected = openIconSelected;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIconSelected;
	}
	/**
	 * 设置Tree各状态图标
	 * @param closedIcon			用于显示无扩展的非叶节点的图标
	 * @param closedIconselected	父节点关闭时选中图标
	 * @param openIcon				用于显示扩展的非叶节点的图标
	 * @param openIconselected		父节点展开后选中图标
	 * @param leafIcon				用于显示叶节点的图标
	 * @param leafIconselected		子节点选中图标
	 */
	public void setTreeIcon(ImageIcon closedIcon,
			ImageIcon closedIconSelected, ImageIcon openIcon,
			ImageIcon openIconSelected, ImageIcon leafIcon,
			ImageIcon leafIconSelected){
		this.closedIcon = closedIcon;
		this.closedIconSelected = closedIconSelected;
		this.openIcon = openIcon;
		this.openIconSelected = openIconSelected;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIconSelected;
	}
	/**
	 * 设置根叶目录的图标,只有这两个状态
	 * @param rootIcon	根目录图标closedIcon,closedIconSelected,openIcon,openIconSelected
	 * @param leafIcon	叶目录图标leafIcon,leafIconSelected
	 */
	public void setTreeRootLeafIcon(ImageIcon rootIcon,
			ImageIcon leafIcon){
		this.closedIcon = rootIcon;
		this.closedIconSelected = rootIcon;
		this.openIcon = rootIcon;
		this.openIconSelected = rootIcon;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIcon;
	}
	/**
	 * 设置根叶目录的图标,只有这三个状态
	 * @param rootCloseIcon 根目录折叠时图标closedIcon,closedIconSelected
	 * @param rootOpenIcon 	根目录展开时图标openIcon,openIconSelected
	 * @param leafIcon 		叶目录图标leafIcon,leafIconSelected
	 */
	public void setTreeRootLeafIcon(ImageIcon rootCloseIcon,ImageIcon rootOpenIcon,
			ImageIcon leafIcon){
		this.closedIcon = rootCloseIcon;
		this.closedIconSelected = rootCloseIcon;
		this.openIcon = rootOpenIcon;
		this.openIconSelected = rootOpenIcon;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIcon;
	}
		
	/**
	 * 设置颜色
	 * @param BackgroundSelectionColor		//选中背景色
	 * @param ForegroundSelectionColor		//选中前景色
	 * @param BackgroundNonSelectionColor	//未选中时背景色
	 * @param ForegroundNonSelectionColor	//未选中背景色
	 */
	public void setColor(Color BackgroundSelectionColor,Color ForegroundSelectionColor,
			Color BackgroundNonSelectionColor,Color ForegroundNonSelectionColor){
		this.BackgroundSelectionColor=BackgroundSelectionColor;
		this.ForegroundSelectionColor=ForegroundSelectionColor;
		this.BackgroundNonSelectionColor=BackgroundNonSelectionColor;
		this.ForegroundNonSelectionColor=ForegroundNonSelectionColor;
		
	}

	public int getID() {
		return ID;
	}
	public void setName(String name){
		this.name=name;
	}
	/**
	 * 设置预留对象
	 * @param object
	 */
	public void setObject(Object object){
		this.object=object;
	}
	public Object getObject(){
		return this.object;
	}
	/**
	 * 设置工具提示字符串
	 * @param object
	 */
	public void setToolTipText(String text){
		this.toolTipText=text;
	}
	public String getTooleTipText(){
		return this.toolTipText;
	}

	/**复制对象,需要Cloneable接口
	 * 创建并返回一个新对象
	 * @return	创建一个新对象
	 */
	public Object clone(){
		StringIcon si=null;
		try {
			si=(StringIcon) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		si.ID = ++si.idEQU;
		return (StringIcon) si;
	}
}


