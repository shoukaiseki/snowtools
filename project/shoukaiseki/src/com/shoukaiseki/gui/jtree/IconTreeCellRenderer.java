package com.shoukaiseki.gui.jtree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.tomaximo.puroguramu.tuuyou.IconSigenn;

/**
 * JTree设置,图标,背景色
 * 
 * @author 蒋カイセキ
 * 
 */
public class IconTreeCellRenderer extends DefaultTreeCellRenderer implements
		MouseMotionListener, MouseListener, TreeExpansionListener {
	/**
	 * 默认渲染方案
	 */
	private IconJTreeObject iconJTreeObject = new IconJTreeObject("");;
	private boolean mouseListener = true;
	public Vector<JMenuItem> jMenuItemVector = null;// 右键菜单

	public IconTreeCellRenderer() {
		setOpaque(true);
		// setOpenIcon(Sigenn.FOLDEROPEN);
		// setLeafIcon(Sigenn.FILE);
		// setClosedIcon(Sigenn.FOLDERCLOSED);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if (mouseListener) {
			tree.addMouseListener(this);
			tree.addMouseMotionListener(this);
			tree.addTreeExpansionListener(this);
			mouseListener = false;
			if (tree.getToolTipText() == null) {
				tree.setToolTipText("");// 提示开启一次后才有效,永远不关会显示一点
			}
		}
		DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) value;

		Object ob = selectNode.getUserObject();
		if (!(ob instanceof IconJTreeObject)) {
			iconJTreeObject.setName((String) ob);
			ob = iconJTreeObject;
		}
		if (selected) {
			if (((IconJTreeObject) ob).BackgroundSelectionColor == null) {
				setBackground(iconJTreeObject.BackgroundSelectionColor);
			} else {
				setBackground(((IconJTreeObject) ob).BackgroundSelectionColor);
			}

			if (((IconJTreeObject) ob).ForegroundSelectionColor == null) {
				setForeground(iconJTreeObject.ForegroundSelectionColor);
			} else {
				setForeground(((IconJTreeObject) ob).ForegroundSelectionColor);
			}

		} else {
			if (((IconJTreeObject) ob).BackgroundNonSelectionColor == null) {
				if (iconJTreeObject.BackgroundNonSelectionColor == null) {
					setBackground(getBackgroundNonSelectionColor());
				} else {
					setBackground(iconJTreeObject.BackgroundNonSelectionColor);
				}
			} else {
				setBackground(((IconJTreeObject) ob).BackgroundNonSelectionColor);
			}
			if (((IconJTreeObject) ob).ForegroundNonSelectionColor == null) {
				if (iconJTreeObject.ForegroundNonSelectionColor == null) {
					setForeground(getTextNonSelectionColor());
				} else {
					setForeground(iconJTreeObject.ForegroundNonSelectionColor);
				}
			} else {
				setForeground(((IconJTreeObject) ob).ForegroundNonSelectionColor);
			}
		}

		// System.out.println("-------" + ((IconJTreeObject) ob).name);
		// TreePath[] tp = tree.getSelectionPaths();
		// if (tp != null) {
		// for (TreePath treePath : tp) {
		// if (tree.isCollapsed(treePath)) {
		// System.out.println(++id + "hasBeenExpanded");
		// }
		// }
		// }
		setText(((IconJTreeObject) ob).name);
		if (value == null) {
			return this;
		}

		if (((IconJTreeObject) ob).closedIcon == null) {
			setIcon(iconJTreeObject.closedIcon);
		} else {
			setIcon(((IconJTreeObject) ob).closedIcon);
		}
		// TODO 如果 expanded 为 true，则当前扩展该节点,展开后
		if (expanded) {
			if (((IconJTreeObject) ob).openIcon == null) {
				setIcon(iconJTreeObject.openIcon);
			} else {
				setIcon(((IconJTreeObject) ob).openIcon);
			}

		}
		// TODO 如果 leaf 为 true，则该节点表示叶节点
		if (leaf) {
			if (((IconJTreeObject) ob).leafIcon == null) {
				setIcon(iconJTreeObject.leafIcon);
			} else {
				setIcon(((IconJTreeObject) ob).leafIcon);
			}
		}
		// TODO 如果 hasFocus 为 true，则该节点当前拥有焦点
		if (hasFocus && leaf) {
			if (((IconJTreeObject) ob).leafIconSelected == null) {
				setIcon(iconJTreeObject.leafIconSelected);
			} else {
				setIcon(((IconJTreeObject) ob).leafIconSelected);
			}
		}
		if (hasFocus && !leaf) {
			if (expanded) {
				if (((IconJTreeObject) ob).openIconSelected == null) {
					setIcon(iconJTreeObject.openIconSelected);
				} else {
					setIcon(((IconJTreeObject) ob).openIconSelected);
				}
			} else {
				if (((IconJTreeObject) ob).closedIconSelected == null) {
					setIcon(iconJTreeObject.openIconSelected);
				} else {
					setIcon(((IconJTreeObject) ob).closedIconSelected);
				}
			}
		}
		if (hasFocus) {
			// System.out.print("hasFocus--");
			if (((IconJTreeObject) ob).getTooleTipText() != null) {
				if (tree.getToolTipText() == null) {
					tree.setToolTipText("");
					// System.out.println("空格");
				}
			} else if (tree.getToolTipText() != null) {
				if (tree.getToolTipText().isEmpty()) {
					tree.setToolTipText(null);
					// System.out.println("null");
				}
			}
		}
		// System.out.println(((IconJTreeObject) ob).name + "---"
		// + ((IconJTreeObject) ob).getTooleTipText());
		setToolTipText(((IconJTreeObject) ob).getTooleTipText());
		return this;
	}

	/**
	 * 设置默认渲染方案
	 * 
	 * @param iconJTreeObject
	 */
	public void setIconJTreeObject(IconJTreeObject iconJTreeObject) {
		this.iconJTreeObject = iconJTreeObject;
	}

	/**
	 * 默认菜单
	 * 
	 * @param jMenuItemVector
	 */
	public void setJMenuItemVector(Vector<JMenuItem> jMenuItemVector) {
		this.jMenuItemVector = jMenuItemVector;
	}

	/**
	 * 默认菜单
	 * 
	 * @return
	 */
	public Vector<JMenuItem> getJMenuItemVector() {
		return this.jMenuItemVector;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("mouseDragged");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		JTree tree = (JTree) e.getSource();
		TreePath path = tree.getPathForLocation(e.getX(), e.getY());
		if (path == null) {
			return;
		}
		if (tree.getToolTipText() != null) {
			if (tree.getToolTipText().isEmpty()) {
				tree.setToolTipText(null);
			}
		}
		// /**
		// * TreePath 获取 DefaultMutableTreeNode
		// */
		// Object ob=path.getLastPathComponent() ;
		// if (ob instanceof DefaultMutableTreeNode) {
		// if(((DefaultMutableTreeNode) ob).isRoot()){
		// tree.setEnabled(false);
		// // System.out.println("isRoot");
		// }
		// // System.out.println("ob---instanceof");
		// }

	}

	@Override
	public void mouseClicked(MouseEvent er) {
		JTree tree = (JTree) er.getSource();
		// System.out.println("mouseReleased");
		TreePath path = path = tree.getPathForLocation(er.getX(), er.getY());
		if (path == null)
			return;
		try {
			// 是否右键单击
			if (er.getClickCount() == 1) {

				Object ob = path.getLastPathComponent();
				// System.out.println("ob---getLastPathComponent");

				if (ob instanceof DefaultMutableTreeNode) {
					ob = ((DefaultMutableTreeNode) ob).getUserObject();
					if (ob instanceof IconJTreeObject) {
						if (tree.isExpanded(path)) {
							if (((IconJTreeObject) ob).isKurikkuCollapsed()) {
								tree.collapsePath(path);
							}
						} else {
							if (((IconJTreeObject) ob).isKurikkuExpanded()) {
								tree.expandPath(path);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent er) {
		// TODO Auto-generated method stub
		JTree tree = (JTree) er.getSource();
		// System.out.println("mouseReleased");
		TreePath path = tree.getPathForLocation(er.getX(), er.getY());
		if (path == null)
			return;

		tree.setSelectionPath(path);
		DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		Object ob = selectNode.getUserObject();
		if (ob instanceof IconJTreeObject) {
			((IconJTreeObject) ob).mouseReleased(er, tree, path, selectNode);
			// 是否右键单击
			if (er.getClickCount() == 1
					&& SwingUtilities.isRightMouseButton(er)) {

				IconJTreeObject si = ((IconJTreeObject) ob);
				Vector<JMenuItem> jPov = si.getJMenuItem();
				JPopupMenu jPo = new JPopupMenu();
				if (jPov != null) {
					// 右键菜单添加JPopupMenu,JPopupMenu 添加另一个 JPopupMenu
					// jPopupMenu.removeAll();
					if (jMenuItemVector != null && si.isJMenuItemed()) {
						for (JMenuItem ji : jMenuItemVector) {
							jPo.add(ji);
						}
						jPo.addSeparator();// 添加分割符
					}
					if (jPov != null) {
						for (JMenuItem ji : jPov) {
							jPo.add(ji);
						}
					}
					tree.add(jPo);
					jPo.show(tree, er.getX(), er.getY());
					return;
				}
			}
		}

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		// TODO Auto-generated method stub
		/**
		 * TreePath 获取 DefaultMutableTreeNode
		 */
		JTree tree = (JTree) event.getSource();
		TreePath path = event.getPath();
		Object ob = path.getLastPathComponent();
		// System.out.println("treeCollapsed"+"----"+path);
		// System.out.println("ob---getLastPathComponent");
		if (ob instanceof DefaultMutableTreeNode) {
			ob = ((DefaultMutableTreeNode) ob).getUserObject();
			if (ob instanceof IconJTreeObject) {
				boolean collapsed = ((IconJTreeObject) ob).isCollapsed();
				// System.out.println(collapsed+"----"+((IconJTreeObject)
				// ob).name);
				if (!collapsed) {
					if (tree.isCollapsed(path)) {
						tree.expandPath(path);
					}
				}
			}
		}

	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		// TODO Auto-generated method stub
		/**
		 * TreePath 获取 DefaultMutableTreeNode
		 */
		JTree tree = (JTree) event.getSource();
		TreePath path = event.getPath();
		Object ob = path.getLastPathComponent();
		// System.out.println("treeExpanded"+"----"+path);
		// System.out.println("ob---getLastPathComponent");
		if (ob instanceof DefaultMutableTreeNode) {
			ob = ((DefaultMutableTreeNode) ob).getUserObject();
			if (ob instanceof IconJTreeObject) {
				boolean expanded = ((IconJTreeObject) ob).isExpanded();
				boolean collapsed = ((IconJTreeObject) ob).isCollapsed();
				// System.out.println(expanded+"----"+((IconJTreeObject)
				// ob).name);
				if (!expanded) {
					if (!tree.isCollapsed(path)) {
						if (collapsed) {
							// 如果两者都开启则展开优先,不做折叠,否则循环
							tree.collapsePath(path);
						}
					}
				}
			}
		}
	}
}


