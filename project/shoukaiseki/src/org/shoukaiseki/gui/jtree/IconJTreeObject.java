package org.shoukaiseki.gui.jtree;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.tomaximo.puroguramu.tuuyou.IconSigenn;

/**
 * JTree专用对象,用于树中渲染图标,颜色,字符,是否可折叠,是否可展开,是否单击折叠,是否单击展开
 * 
 * @author 蒋カイセキ
 * 
 */
public class IconJTreeObject implements Cloneable {
	/**
	 * 唯一ID,比较此值,判断是否为此监听对象
	 */
	private static int idEQU = 0;
	private int ID = 0;
	private boolean collapsed = true;// 是否允许折叠
	private boolean expanded = true;// 是否允许展开
	private boolean kurikkuExpanded = true;// 是否单击展开
	private boolean kurikkuCollapsed = true;// 是否单击折叠
	public String name = null;
	public String toolTipText = null;// 设置工具提示字符串

	public ImageIcon closedIcon;// 用于显示无扩展的非叶节点的图标
	public ImageIcon closedIconSelected;// 父节点关闭时选中图标
	public ImageIcon openIcon;// 用于显示扩展的非叶节点的图标
	public ImageIcon openIconSelected;// 父节点展开后选中图标
	public ImageIcon leafIcon;// 用于显示叶节点的图标
	public ImageIcon leafIconSelected;// 子节点选中图标

	public Color BackgroundSelectionColor = new Color(184, 207, 229);// 选中背景色
	public Color ForegroundSelectionColor = Color.black;// 选中前景色
	public Color BackgroundNonSelectionColor;// 未选中时背景色
	public Color ForegroundNonSelectionColor;// 未选中背景色
	public Vector<JMenuItem> jMenuItemVector = null;// 右键菜单
	private boolean jMenuItemed = false;// 是否添加在默认菜单后面
	public Object object;// 预留

	/**
	 * 增加鼠标监听,重写 public void mouseReleased(MouseEvent er,JTree tree, TreePath
	 * path,DefaultMutableTreeNode selectNode);
	 * 
	 * @param name
	 */
	public IconJTreeObject(String name) {
		ID = ++idEQU;
		this.name = name;
	}

	/**
	 * 
	 * @param name
	 * @param b
	 *            是否使用IconJTreeObject的图标模板
	 */
	public IconJTreeObject(String name, boolean b) {
		ID = ++idEQU;
		this.name = name;
		if (b) {
			this.closedIcon = IconSigenn.FOLDERCLOSED;
			this.closedIconSelected = IconSigenn.FOLDERCLOSED_SELECTED;
			this.openIcon = IconSigenn.FOLDEROPEN;
			this.openIconSelected = IconSigenn.FOLDEROPEN_SELECTED;
			this.leafIcon = IconSigenn.FILE;
			this.leafIconSelected = IconSigenn.SELECTED_FILE;
		}
	}

	/**
	 * 使用IconJTreeObject(String name, ImageIcon icon)实例化将采用默认的图标
	 * 
	 * @param name
	 *            显示名称
	 * @param closedIcon
	 *            用于显示无扩展的非叶节点的图标
	 * @param closedIconselected
	 *            父节点关闭时选中图标
	 * @param openIcon
	 *            用于显示扩展的非叶节点的图标
	 * @param openIconselected
	 *            父节点展开后选中图标
	 * @param leafIcon
	 *            用于显示叶节点的图标
	 * @param leafIconselected
	 *            子节点选中图标
	 */
	public IconJTreeObject(String name, ImageIcon closedIcon,
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
	 * 
	 * @param closedIcon
	 *            用于显示无扩展的非叶节点的图标
	 * @param closedIconselected
	 *            父节点关闭时选中图标
	 * @param openIcon
	 *            用于显示扩展的非叶节点的图标
	 * @param openIconselected
	 *            父节点展开后选中图标
	 * @param leafIcon
	 *            用于显示叶节点的图标
	 * @param leafIconselected
	 *            子节点选中图标
	 */
	public void setTreeIcon(ImageIcon closedIcon, ImageIcon closedIconSelected,
			ImageIcon openIcon, ImageIcon openIconSelected, ImageIcon leafIcon,
			ImageIcon leafIconSelected) {
		this.closedIcon = closedIcon;
		this.closedIconSelected = closedIconSelected;
		this.openIcon = openIcon;
		this.openIconSelected = openIconSelected;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIconSelected;
	}

	/**
	 * 设置根叶目录的图标,只有这两个状态
	 * 
	 * @param rootIcon
	 *            根目录图标closedIcon,closedIconSelected,openIcon,openIconSelected
	 * @param leafIcon
	 *            叶目录图标leafIcon,leafIconSelected
	 */
	public void setTreeRootLeafIcon(ImageIcon rootIcon, ImageIcon leafIcon) {
		this.closedIcon = rootIcon;
		this.closedIconSelected = rootIcon;
		this.openIcon = rootIcon;
		this.openIconSelected = rootIcon;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIcon;
	}

	/**
	 * 设置根叶目录的图标,只有这三个状态
	 * 
	 * @param rootCloseIcon
	 *            根目录折叠时图标closedIcon,closedIconSelected
	 * @param rootOpenIcon
	 *            根目录展开时图标openIcon,openIconSelected
	 * @param leafIcon
	 *            叶目录图标leafIcon,leafIconSelected
	 */
	public void setTreeRootLeafIcon(ImageIcon rootCloseIcon,
			ImageIcon rootOpenIcon, ImageIcon leafIcon) {
		this.closedIcon = rootCloseIcon;
		this.closedIconSelected = rootCloseIcon;
		this.openIcon = rootOpenIcon;
		this.openIconSelected = rootOpenIcon;
		this.leafIcon = leafIcon;
		this.leafIconSelected = leafIcon;
	}

	/**
	 * 
	 * @param rootOpenIcon
	 *            根目录展开时图标openIcon,openIconSelected
	 */
	public void setTreeOpenIcon(ImageIcon rootOpenIcon) {
		this.openIcon = rootOpenIcon;
		this.openIconSelected = rootOpenIcon;
	}

	/**
	 * 设置颜色
	 * 
	 * @param BackgroundSelectionColor
	 *            //选中背景色
	 * @param ForegroundSelectionColor
	 *            //选中前景色
	 * @param BackgroundNonSelectionColor
	 *            //未选中时背景色
	 * @param ForegroundNonSelectionColor
	 *            //未选中背景色
	 */
	public void setColor(Color BackgroundSelectionColor,
			Color ForegroundSelectionColor, Color BackgroundNonSelectionColor,
			Color ForegroundNonSelectionColor) {
		this.BackgroundSelectionColor = BackgroundSelectionColor;
		this.ForegroundSelectionColor = ForegroundSelectionColor;
		this.BackgroundNonSelectionColor = BackgroundNonSelectionColor;
		this.ForegroundNonSelectionColor = ForegroundNonSelectionColor;

	}

	public int getID() {
		return ID;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置预留对象
	 * 
	 * @param object
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return this.object;
	}

	/**
	 * 设置工具提示字符串
	 * 
	 * @param object
	 */
	public void setToolTipText(String text) {
		this.toolTipText = text;
	}

	public String getTooleTipText() {
		return this.toolTipText;
	}

	/**
	 * 设置是否允许折叠,true为可折叠
	 * 
	 * @param collapsed
	 */
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	/**
	 * 返回是否可折叠,true为可折叠
	 * 
	 * @param collapsed
	 */
	public boolean isCollapsed() {
		return this.collapsed;
	}

	/**
	 * 设置是否允许展开,true为可展开
	 * 
	 * @param expanded
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	/**
	 * 返回是否可展开,true为可展开
	 * 
	 * @param expanded
	 */
	public boolean isExpanded() {
		return this.expanded;
	}

	/**
	 * 是否增加在默认菜单后面,true为增加,默认为false
	 * 
	 * @param collapsed
	 */
	public void setJMenuItemed(boolean jMenuItemed) {
		this.jMenuItemed = jMenuItemed;
	}

	/**
	 * 返回是否增加在默认菜单后面,true为增加,默认为false
	 * 
	 * @param collapsed
	 */
	public boolean isJMenuItemed() {
		return this.jMenuItemed;
	}

	/**
	 * 设置是否允许单击展开,true为可单击展开
	 * 
	 * @param kurikkuExpanded
	 */
	public void setKurikkuExpanded(boolean kurikkuExpanded) {
		this.kurikkuExpanded = kurikkuExpanded;
	}

	/**
	 * 返回是否可单击展开,true为可单击展开
	 * 
	 * @param kurikkuExpanded
	 */
	public boolean isKurikkuExpanded() {
		return this.kurikkuExpanded;
	}

	/**
	 * 设置是否允许单击折叠,true为可单击折叠
	 * 
	 * @param kurikkuExpanded
	 */
	public void setKurikkuCollapsed(boolean kurikkuCollapsed) {
		this.kurikkuCollapsed = kurikkuCollapsed;
	}

	/**
	 * 返回是否可单击折叠,true为可单击折叠
	 * 
	 * @param kurikkuExpanded
	 */
	public boolean isKurikkuCollapsed() {
		return this.kurikkuCollapsed;
	}

	/**
	 * 右键菜单组
	 * 
	 * @param jMenuItemVector
	 * @param jMenuItemed
	 *            是否增加在默认菜单后面,true为增加,默认为false
	 */
	public void setJMenuItem(Vector<JMenuItem> jMenuItemVector,
			boolean jMenuItemed) {
		this.jMenuItemVector = jMenuItemVector;
		this.jMenuItemed = jMenuItemed;
	}

	/**
	 * 右键菜单组
	 * 
	 * @param jMenuItem
	 */
	public void setJMenuItem(Vector<JMenuItem> jMenuItemVector) {
		this.jMenuItemVector = jMenuItemVector;
	}

	/**
	 * 右键菜单组,将添加JPopupMenu进行弹出
	 * 
	 * @return
	 */
	public Vector<JMenuItem> getJMenuItem() {
		return this.jMenuItemVector;
	}

	/**
	 * 复制对象,需要Cloneable接口 创建并返回一个新对象
	 * 
	 * @return 创建一个新对象
	 */
	public IconJTreeObject clone() {
		IconJTreeObject si = null;
		try {
			si = (IconJTreeObject) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		si.ID = ++si.idEQU;
		return (IconJTreeObject) si;
	}

	/**
	 * 鼠标监听,需要重写
	 * 
	 * @param er
	 */
	public void mouseReleased(MouseEvent er, JTree tree, TreePath path,
			DefaultMutableTreeNode selectNode) {

	}
}


