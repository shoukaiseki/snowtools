package com.shoukaiseki.gui.jtree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

import com.shoukaiseki.sql.ConnectionKonnfigu;

public class JMenuItemJTree extends JMenuItem {
	public JMenuItemJTree jmijt = this;
	public IconJTreeObject ijto = null;
	public ConnectionKonnfigu ck = null;
	public DefaultMutableTreeNode dmtn = null;

	public JMenuItemJTree(String text) {
		super(text);
		initActionListener();
	}

	public JMenuItemJTree(String text, IconJTreeObject ijto) {
		super(text);
		this.ijto = ijto;
		initActionListener();
	}

	public JMenuItemJTree(String text, Icon icon, IconJTreeObject ijto) {
		super(text, icon);
		this.ijto = ijto;
		initActionListener();
	}

	private void initActionListener() {
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jmijt.actionPerformed(e, jmijt, ijto, ck, dmtn);
			}
		});
	}

	public IconJTreeObject getIconJTreeObject() {
		return this.ijto;
	}

	public void setConnectionKonnfigu(ConnectionKonnfigu ck) {
		this.ck = ck;
	}

	public ConnectionKonnfigu getConnectionKonnfigu() {
		return this.ck;
	}

	public void setDefaultMutableTreeNode(DefaultMutableTreeNode dmtn) {
		this.dmtn = dmtn;
	}

	/**
	 * actionPerformed监听,需要重写 public void actionPerformed(ActionEvent
	 * e,JMenuItemJTree jmijt, IconJTreeObject ijto, ConnectionKonnfigu ck)
	 */
	public void actionPerformed(ActionEvent e, JMenuItemJTree jmijt,
			IconJTreeObject ijto, ConnectionKonnfigu ck,
			DefaultMutableTreeNode dmtn) {

	}
}


