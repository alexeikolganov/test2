package heritage.listener;

import heritage.controls.HPopupMenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Листенер для вызова контекстного меню для блоков
 * @author Home
 *
 */
public class PopupListener extends MouseAdapter 
{
	private HPopupMenu popup; 
	public PopupListener( HPopupMenu ppp )
	{
		super( );
		popup = ppp;
	}
	public void mousePressed( MouseEvent e ) 
	{
		togglePopup( e );
	}
	public void mouseReleased( MouseEvent e ) 
	{
		togglePopup( e );
	}
	private void togglePopup( MouseEvent e ) 
	{
		if( e.isPopupTrigger() )
		{
			popup.show( e.getComponent(), e.getX(), e.getY() );
		}
	}
}