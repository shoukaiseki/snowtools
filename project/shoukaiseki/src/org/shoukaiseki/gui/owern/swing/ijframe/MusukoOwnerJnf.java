package org.shoukaiseki.gui.owern.swing.ijframe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;

import antlr.Version;

import org.shoukaiseki.gui.jbutton.TurubaGroup;
import org.shoukaiseki.gui.jlist.JListIcon;
import org.shoukaiseki.gui.owern.swing.jframe.JFrameWindowClose;
import org.shoukaiseki.gui.owern.swing.jframe.MusukoOwnerJFrame;
import org.shoukaiseki.gui.owern.swing.risuna.MusukoApurikeshonnShare;
import org.shoukaiseki.gui.tabbedpane.ClosableTabbedPane;
import org.shoukaiseki.gui.tabbedpane.ClosableTabbedPaneRisuna;
import org.shoukaiseki.gui.tuuyou.JLabelComponentAt;
import org.shoukaiseki.gui.tuuyou.StringIcon;
import org.tomaximo.puroguramu.plugins.AddKyoka;

public class MusukoOwnerJnf extends Jnf 
implements MusukoApurikeshonnShare ,
JFrameWindowClose,ClosableTabbedPaneRisuna{
	protected MusukoOwnerJFrame musukoOwner;
	protected JListIcon jlistApurikeshonn;
	Vector ver = new Vector();

	protected Vector addKyokaVec=new Vector();
	
	/**
	 * 应用程序
	 */
	protected TurubaGroup tg;
	public JLabelComponentAt jtpID=null;//
	
	public MusukoOwnerJnf() {
	}

	public void init() {
		musukoOwner = (MusukoOwnerJFrame) getOwnerJFrame();
		musukoOwner.getJListIcon().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(isThisIndex(e)){
					mouseClickedJListIcon(e);
				}
				if (e.getClickCount() == 2) { // Double-click
					// Get item index
					if(isThisIndex(e)){
						ApurikeshonnDaburuKurikku(e);
					}
				} else if (e.getClickCount() == 3) { // Triple-click
					// Get item index
					// Note that this list will receive a double-click event
					// before this triple-click event
				}
			}
		});
		musukoOwner.getJtpKita().addTabAboutToClose(this);
		setBorder(BorderFactory.createEmptyBorder());
	}
	/**
	 * 检查是否为该索引
	 * @param evt
	 * @return
	 */
	public boolean isThisIndex(MouseEvent e){
		JList list = (JList) e.getSource();
//		int i = list.getAnchorSelectionIndex();
		int index = list.locationToIndex(e.getPoint());
//		System.out.println(index);
		Object si=list.getSelectedValue();
		if (si instanceof StringIcon) {
//			System.out.println("instanceof"+index);
//			System.out.println("ver.size()"+ver.size());
			for (Object id : ver) {
//				System.out.println(((StringIcon) si).getID()+"instanceof"+(Integer)id);
				if(((StringIcon) si).getID()==(Integer)id){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 添加到应用
	 * 
	 * @param obj
	 */
	protected StringIcon addApurikeshonn(String name, ImageIcon icon) {
		StringIcon si = musukoOwner.addApurikeshonn(name, icon);
		ver.add(si.getID());
		return si;
	}

	/**
	 * 鼠标双击事件
	 * @param evt
	 * @return
	 */
	protected void ApurikeshonnDaburuKurikku(MouseEvent e) {

	}
	
	/**
	 * JListIcon的鼠标事件
	 */
	protected void mouseClickedJListIcon(MouseEvent e){
		
	}
	

	/**
	 * window关闭事件
	 */
	@Override
	public boolean windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	public void addTabAboutToClose(MusukoApurikeshonnShare mas,JLabelComponentAt jca){
		this.jtpID=jca;
		this.tg=jca.getTurubaGroup();
		addKyokaVec.add(mas);
		mas.setJLabelComponentAt(jtpID);
		mas.appInit();
	}
	
	/**
	 * 标签关闭事件
	 */
	@Override
	public boolean tabAboutToClose(int tabIndex,MouseEvent me) {
		// TODO Auto-generated method stub
		ClosableTabbedPane ctp=((ClosableTabbedPane)me.getSource());
		Object obj=(JLabelComponentAt) ctp.getTabComponentAt(ctp.getSelectedIndex()) ;
		JLabelComponentAt jlca=null;
		if(obj instanceof JLabelComponentAt){	
			 jlca=(JLabelComponentAt) obj;
		}else{
			return true;
		}
//		System.out.println("'"+tab+"'--'"+tabRaberuName+"'");
		for (Object object : addKyokaVec) {
			System.out.println("addKyokaVec.size("+addKyokaVec.size());
			MusukoApurikeshonnShare ak=((MusukoApurikeshonnShare)object);
			JLabelComponentAt jca=ak.getJLabelComponentAt();
			if(jlca.getID()==jca.getID()){
				System.out.println("jca.getID()"+jca.getID());
				System.out.println("tabuTojiruRisuna-------");
				if(!ak.tabuTojiruRisuna(tabIndex,me)){
					addKyokaVec.remove(object);//窗口已关闭,移除对象
					ak=null;
					return false;
				}
					
			}
		}
		return true;
	}

	@Override
	public void tabAboutToDoubleClicked(int tabIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tabAboutToMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JLabelComponentAt getJLabelComponentAt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJLabelComponentAt(JLabelComponentAt jca) {
		// TODO Auto-generated method stub
		this.jtpID=jca;
		this.tg=jca.getTurubaGroup();
		
	}

	@Override
	public boolean tabuTojiruRisuna(int tabIndex,MouseEvent me) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void appInit() {
		// TODO Auto-generated method stub
		
	}

}


