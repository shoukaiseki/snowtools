package org.shoukaiseki.gui.tuuyou;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.shoukaiseki.gui.jlist.JListIcon;
import org.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;
import org.tomaximo.puroguramu.tuuyou.IconSigenn;

public class byuIconExe extends JList {
	DefaultListModel dlmDataModel=new DefaultListModel();
	JLabel label=new JLabel();
	public byuIconExe(){
		setModel(dlmDataModel);
		setCellRenderer(new IconListCellRenderer());
		
	}

	public byuIconExe(DefaultListModel dlmDataModel){
		this.dlmDataModel=dlmDataModel;
		setModel(dlmDataModel);
		setCellRenderer(new IconListCellRenderer());
		
	}

	/**
	 * DefaultComboBoxModel=在模型的末尾添加项
	 * @param name
	 * @param icon
	 * @return 返回StringIcon,调用getID()获得ID,比较此值,判断是否为此监听对象
	 */
	public StringIcon addElement(String name,ImageIcon icon){
		StringIcon si=new StringIcon(name,icon);
		dlmDataModel.addElement(si);
		return si;
	}
	/**
	 * DefaultComboBoxModel=在指定索引处添加项。
	 * @param name
	 * @param icon
	 * @param index
	 * @return 返回StringIcon,调用getID()获得ID,比较此值,判断是否为此监听对象
	 */
	public  StringIcon insertElementAt(String name,ImageIcon icon,int index){
		StringIcon si=new StringIcon(name,icon);
		dlmDataModel.insertElementAt(si,index);
		return si;
	}
	
	/**
	 *   返回 JComboBox 当前使用的数据模型。
	 * @return
	 */
	public DefaultListModel getDefaultComboBoxModel(){
		return dlmDataModel;
	}
	
	public static void main(String args[]){
		OwnerJFrame oj = new OwnerJFrame();
		oj.setTitle("查看图标");
		byuIconExe jli=new byuIconExe();
		oj.setDefaultCloseOperation(oj.EXIT_ON_CLOSE);


		
		jli.addElement("RUN",IconSigenn.RUN);
		jli.addElement("COMMIT",IconSigenn.COMMIT);
		jli.addElement("ROLLBACK",IconSigenn.ROLLBACK);

		jli.addElement("DATABASE",IconSigenn.DATABASE);

		jli.addElement("DBCONN",IconSigenn.DBCONN);

		jli.addElement("CLEAN",IconSigenn.CLEAR);
			/**
			*文件图标
			*/
		jli.addElement("OPEN",IconSigenn.OPEN);
		jli.addElement("FOLDERCLOSED",IconSigenn.FOLDERCLOSED);
		jli.addElement("FOLDEROPEN",IconSigenn.FOLDEROPEN);
		jli.addElement("FOLDER_SELECTED",IconSigenn.FOLDER_SELECTED);

			
		jli.addElement("SELECTED_FILE",IconSigenn.SELECTED_FILE);
		jli.addElement("FOLDERCLOSED_SELECTED",IconSigenn.FOLDERCLOSED_SELECTED);
		jli.addElement("FOLDEROPEN_SELECTED",IconSigenn.FOLDEROPEN_SELECTED);

			
		jli.addElement("FILE",IconSigenn.FILE);
		jli.addElement("FOLDER",IconSigenn.FOLDER);
		jli.addElement("FOLDERNEW",IconSigenn.FOLDERNEW);
		jli.addElement("UPFOLDER",IconSigenn.UPFOLDER);
		jli.addElement("STATICFOLDER",IconSigenn.STATICFOLDER);
		jli.addElement("FILESAVE",IconSigenn.FILESAVE);
		jli.addElement("REMOVE_FILE",IconSigenn.REMOVE_FILE);
		jli.addElement("SQLFILE",IconSigenn.SQLFILE);
		jli.addElement("XMLFILE",IconSigenn.XMLFILE);
		jli.addElement("JAVAFILE",IconSigenn.JAVAFILE);
		jli.addElement("FILE_UPLOAD",IconSigenn.FILE_UPLOAD);
		jli.addElement("FILECONN",IconSigenn.FILECONN);

		/**
		 * APP应用
		 */
		jli.addElement("APPLICATION",IconSigenn.APPLICATION);
		jli.addElement("KOKUMINNTOU",IconSigenn.KOKUMINNTOU);
		
		/**
		 * 大图标
		 */
		jli.addElement("SIMPLEFILE",IconSigenn.SIMPLEFILE);


		

		
		JScrollPane sjp=new JScrollPane(jli);
		oj.getContentPane().add(sjp,BorderLayout.CENTER);
		oj.setVisible(true);
	}
}


