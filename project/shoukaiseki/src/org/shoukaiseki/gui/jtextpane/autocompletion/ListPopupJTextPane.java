package org.shoukaiseki.gui.jtextpane.autocompletion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

import org.shoukaiseki.gui.tuuyou.ColorHouhou;

/**
 *
 * @author William Chen
 */
public class ListPopupJTextPane extends JPopupMenu implements MouseInputListener {
    
    private JList list;
    private JScrollPane pane;
    private ArrayList<ListSelectionListener> listeners=new ArrayList<ListSelectionListener>();
    public void addListSelectionListener(ListSelectionListener l){
        if(!listeners.contains(l))
            listeners.add(l);
    }
    public void setSelectedIndex(int index){
        if(index>=list.getModel().getSize())
            index=0;
        if(index<0)
            index=list.getModel().getSize()-1;
        list.ensureIndexIsVisible(index);
        list.setSelectedIndex(index);
    }
    public Object getSelectedValue(){
        return list.getSelectedValue();
    }
    public int getSelectedIndex(){
        return list.getSelectedIndex();
    }
    public boolean isSelected(){
    	if (this.isDisplayable()) {
            return list.getSelectedIndex()!=-1;
		}
    	return false;
    }
    public void setLastOneSelected(){
        int count=list.getModel().getSize();
        if(count>0){
            list.ensureIndexIsVisible(count-1);
            list.setSelectedIndex(count-1);
        }
    }
    public void removeListSelectionListener(ListSelectionListener l){
        if(listeners.contains(l))
            listeners.remove(l);
    }
    private void fireValueChanged(ListSelectionEvent e){
        for(ListSelectionListener l:listeners){
            l.valueChanged(e);
        }
    }
    public ListPopupJTextPane() {
        setLayout(new BorderLayout());
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(this);
        list.addMouseMotionListener(this);
        list.setModel(new DefaultListModel());
        pane = new JScrollPane(list);
        Color col=new Color(222,184,135);
        pane.setBorder(BorderFactory.createLineBorder(col));
        this.setBorder(BorderFactory.createLineBorder(col));
        add(pane, BorderLayout.CENTER);
        list.setBackground(ColorHouhou.hexToRGB("#C7EDCC"));
        this.setBackground(new Color(199,237,204));
        
    }
    
    public int getItemCount() {
        DefaultListModel model = (DefaultListModel) list.getModel();
        return model.getSize();
    }
    
    public Object getItem(int index) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        return model.get(index);
    }
    
    public void addItem(Object o) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.addElement(o);
        list.repaint();
    }
    
    public void removeItem(Object o) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeElement(o);
        list.repaint();
    }
    
    public void setList(Iterable iterable) {
        DefaultListModel model = new DefaultListModel();
        for (Object o : iterable) {
            model.addElement(o);
        }
        list.setModel(model);
        list.repaint();
    }
    
    public void setList(Enumeration e) {
        DefaultListModel model = new DefaultListModel();
        while (e.hasMoreElements()) {
            model.addElement(e.nextElement());
        }
        list.setModel(model);
        list.repaint();
    }
    
    public void setList(Object... objects) {
        DefaultListModel model = new DefaultListModel();
        for (Object o : objects) {
            model.addElement(o);
        }
        list.setModel(model);
        list.repaint();
    }
    
    public void setSize(Dimension d){
    	pane.setPreferredSize(d);
    }
    

    public void mouseClicked(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        if(list.getSelectedIndex()!=-1)
            fireValueChanged(new ListSelectionEvent(list, list.getSelectedIndex(), list.getSelectedIndex(), true));
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent anEvent) {
        if (anEvent.getSource() == list) {
            Point location = anEvent.getPoint();
            Rectangle r = new Rectangle();
            list.computeVisibleRect(r);
            if (r.contains(location)) {
                updateListBoxSelectionForEvent(anEvent, false);
            }
        }
    }
    
    protected void updateListBoxSelectionForEvent(MouseEvent anEvent, boolean shouldScroll) {

        Point location = anEvent.getPoint();
        if (list == null) {
            return;
        }
        int index = list.locationToIndex(location);
        if (index == -1) {
            if (location.y < 0) {
                index = 0;
            } else {
                index = list.getModel().getSize() - 1;
            }
        }
        if (list.getSelectedIndex() != index) {
            list.setSelectedIndex(index);
            if (shouldScroll) {
                list.ensureIndexIsVisible(index);
            }
        }
    }
    
    public void mouseDragged(MouseEvent e) {
    }
}


