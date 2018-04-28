import java.awt.event.*;
public class PopupListener extends MouseAdapter{
	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			Render.popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
