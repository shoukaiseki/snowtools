package com.tomaximo.puroguramu.ijframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.shoukaiseki.gui.jbutton.TurubaGroup;
import com.shoukaiseki.gui.jtable.JTableOperating;
import com.shoukaiseki.gui.owern.swing.ijframe.MusukoOwnerJnf;
import com.shoukaiseki.gui.owern.swing.jframe.MusukoOwnerJFrame;
import com.shoukaiseki.gui.tuuyou.StringIcon;
import com.shoukaiseki.sql.ConnectionKonnfigu;
import com.tomaximo.puroguramu.interfaces.DetabesuKonnfiguInterFace;
import com.tomaximo.puroguramu.jframe.SennyouNoMaximo;
import com.tomaximo.puroguramu.tuuyou.IconSigenn;

public class SennyouNoMaximoJnf extends MusukoOwnerJnf {
	protected SennyouNoMaximo sennyouNoMaximo;
	public JDialog aboutDialog;
	public JTableOperating AboutDialogtable ;
	public Container c=getContentPane();
	public void init() {
		super.init();
		sennyouNoMaximo = (SennyouNoMaximo) musukoOwner;
		
	}

    /**
     * 设置"关于"对话框的外观及响应事件,操作和JFrame一样都是在内容
     * 框架上进行的
     */
    public void initAboutDialog(){
		aboutDialog=new JDialog();
		aboutDialog.setTitle("关于");
	    Container con =aboutDialog.getContentPane();
		 String[] columnNames = { "","名称", "备注","版本","类名", "包名"}; // 列名
		 DefaultTableModel dtm=new DefaultTableModel(columnNames,0);
	    AboutDialogtable = new JTableOperating(dtm);
	    AboutDialogtable.add(new JLabel("撒旦法是的分撒旦"));
	     JScrollPane jScrollPane = new JScrollPane(AboutDialogtable);

	     AboutDialogtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//水平滚动条
        con.add(jScrollPane,BorderLayout.CENTER);//,BorderLayout.CENTER
		aboutDialog.setSize(550,425);
		aboutDialog.setLocationRelativeTo(null);            
		aboutDialog.setModal(true);   
        aboutDialog.addWindowListener(new WindowAdapter()
        {
            public void WindowClosing(WindowEvent e)
            {
            	aboutDialog.dispose();
            }                    
        });
    }
	public void setTurubaGroupJComboBox(
			DetabesuKonnfiguInterFace detabesuKonnfiguInterFace, TurubaGroup tg) {
		Map<String, ConnectionKonnfigu> map = detabesuKonnfiguInterFace
				.getConnectionKonnfigu();
		 Set<String> key = map.keySet();
		 Iterator it = key.iterator();
	        for (int i=1; it.hasNext();i++) {
	            String s = (String) it.next();
	            tg.addComboBoxItem(
	            		new StringIcon(s,IconSigenn.DATABASE));
	        }
		
	}
	
	public void setAboutDialogDataBesuBajyonn(Connection con){
		try {
			Statement st = con.createStatement();
			ResultSet r = st
					.executeQuery("SELECT UTL_INADDR.get_host_name() 服务器主机名," +
							"utl_inaddr.get_host_address 服务器IP ," +
							"SYS_CONTEXT('USERENV','TERMINAL'),sysdate,user FROM dual");

			ResultSetMetaData rsmd=r.getMetaData();
			int column=rsmd.getColumnCount();
			String[] columnNames=new String[column];
			for (int i = 0; i < column ; i++) {
				columnNames[i]=rsmd.getColumnName(i+1);
			}
			DefaultTableModel dtm=new DefaultTableModel(columnNames,0);
			AboutDialogtable.setModel(dtm);
			int row=0;
			int col=0;
			while (r.next()) {
				dtm.addRow(new String[]{});
				for (int i = 0; i < column; i++) {
					dtm.setValueAt(r.getString(i+1), row, col++);
				}
				row++;
			}
			
			r.close();
			st.close();
			
			//select SYS_CONTEXT('USERENV','TERMINAL') from dual
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


