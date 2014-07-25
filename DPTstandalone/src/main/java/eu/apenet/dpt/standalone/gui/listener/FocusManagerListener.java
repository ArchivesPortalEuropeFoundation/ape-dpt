package eu.apenet.dpt.standalone.gui.listener;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JViewport;

import eu.apenet.dpt.standalone.gui.commons.swingstructures.TextAreaScrollable;

public class FocusManagerListener implements PropertyChangeListener {
	
	private static final int MARGIN_Y = 60;
	private JPanel panel;

	public FocusManagerListener(JPanel panel) {
		super();
		this.panel = panel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
        if ("focusOwner".equals(propertyName)){
        	if (evt.getNewValue() instanceof JComponent) {
	        	JViewport viewport = (JViewport) this.panel.getParent();
		        if(evt.getNewValue() instanceof JTextField || evt.getNewValue() instanceof TextAreaScrollable){
		        	JComponent focused = (JComponent) evt.getNewValue();
			        if (this.panel.isAncestorOf(focused)) {
			            if(focused.getY()==0 && focused.getParent()!=null && focused.getParent().getParent()!=null && focused.getParent().getParent().getY()>0){
			            	int y = (int)focused.getParent().getParent().getY();
			            	this.panel.scrollRectToVisible(new Rectangle(0,(y>=MARGIN_Y)?y-MARGIN_Y:0, 0, (int) viewport.getVisibleRect().getHeight()));
			            }else{
			            	int y = (int)focused.getY();
			            	this.panel.scrollRectToVisible(new Rectangle(0,(y>=MARGIN_Y)?y-MARGIN_Y:0, 0, (int) viewport.getVisibleRect().getHeight()));
			            }
			        }
		        }
	        }
        }
	}
	
}