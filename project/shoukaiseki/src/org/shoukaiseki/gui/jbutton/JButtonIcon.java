package org.shoukaiseki.gui.jbutton;

/**
 *  //设置按钮没有边缘线条以及可以按照图片形状得到按钮
 * 	button.setBorder(null);
 * 	button.setContentAreaFilled(false);
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;

public class JButtonIcon extends JButton {
	private Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private Color roverBorderColor = Color.gray;
	private boolean ninnmuKannryou = true;// 执行完任务后恢复

	public JButtonIcon() {
		super();
	}

	public JButtonIcon(ImageIcon icon) {
		super(icon);

		this.setOpaque(false);
		this.setBorder(null);
		this.setContentAreaFilled(false);// 开启透明色
		this.setFocusPainted(false);
		// this.setRolloverEnabled(true);
		final Border roverBorder = new Border() {
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				g.setColor(roverBorderColor);
				g.drawRect(x, y, width - 1, height - 1);
			}

			public Insets getBorderInsets(Component c) {
				return new Insets(1, 1, 1, 1);
			}

			public boolean isBorderOpaque() {
				return true;
			}
		};
		this.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				if (isRolloverEnabled()) {
					if (ninnmuKannryou) {
						setBorder(roverBorder);
					}
				}
			}

			public void mouseExited(MouseEvent e) {
				if (ninnmuKannryou) {
					setBorder(emptyBorder);
					setContentAreaFilled(false);// 开启透明色
					setBackground(new Color(184,201,227));
				}
			}

			public void mousePressed(MouseEvent e) {
				if (ninnmuKannryou) {
					setContentAreaFilled(true);// 关闭透明色
				}
			}

		});
	}

	/**
	 * 设置自动恢复按钮按下时的状态,与setJButtonIro()方法配合使用效果更佳
	 * @return
	 */
	public void setNinnmuKannryou(boolean ninnmuKannryou){
		this.ninnmuKannryou=ninnmuKannryou;
	}
	
	/**
	 * 自动恢复按钮按下时的状态
	 * @return
	 */
	public boolean getNinnmuKannryou(){
		return this.ninnmuKannryou;
	}
	
	/**
	 * 设置按钮被按下时的颜色,使用setJButtonIro()方法恢复
	 */
	public void setJButtonIro(){
		setNinnmuKannryou(false);
		setBackground(new Color(184,201,227));
	}
	public static void main(String args[]) {
		OwnerJFrame oj = new OwnerJFrame();
		oj.setDefaultCloseOperation(oj.EXIT_ON_CLOSE);
		ImageIcon setuzokuIcon = new ImageIcon("./image/oicons/commit.png");
		final JButtonIcon jbi = new JButtonIcon(setuzokuIcon);
		jbi.setPreferredSize(new Dimension(20, 20));
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp.add(jbi);
		oj.getContentPane().add(jp);

		jbi.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						jbi.setContentAreaFilled(true);// 开启透明色
						jbi.setNinnmuKannryou(false);
						jbi.setBackground(new Color(184,201,227));
//						jbi.setBackground(Color.red);
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {// 实例中的的方法

								for (int i = 0; i < 99999999; i++) {
									for (int j = 0; j < 33; j++) {

									}
								}
								jbi.setNinnmuKannryou(true);
							}
						}, 10);
					}
				});
	}
}


