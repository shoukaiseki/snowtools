package org.shoukaiseki.gui.jinternalframe;

/**关于JInternalFrame去掉Title bar的问题
 * http://www.blogjava.net/hkbmwcn/archive/2008/01/18/176341.html?1332261839
 */

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class MCOCInternalFrameK extends JInternalFrame {

	// private String lookAndFeel = null;
	BasicInternalFrameUI orgUi = null;
	BasicInternalFrameUI newUi = null;
	JComponent northPanel = null;
	private boolean isHidden = false;

	public MCOCInternalFrameK() {
		super();

		northPanel = ((javax.swing.plaf.basic.BasicInternalFrameUI) this
				.getUI()).getNorthPane();
		orgUi = ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI());
		newUi = new BasicInternalFrameUI(this);
	}

	/**
	 * 显示title bar
	 */
	public void showNorthPanel() {

		this.setUI(orgUi);
		this.putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
		isHidden = false;

	}

	/**
	 * 隐藏title bar
	 */
	public void hideNorthPanel() {
		this.setUI(newUi);
		((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI())
				.setNorthPane(null);
		this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
		isHidden = true;

	}

	/**
	 * 重写,因为界面被动态改变lookandfeel时，保证title bar上多的一小个bar出现
	 */
	public void updateUI() {

		super.updateUI();
		if (isHidden) {
			hideNorthPanel();
		}
	}

}

