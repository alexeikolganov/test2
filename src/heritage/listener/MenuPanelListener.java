package heritage.listener;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Ћистенер дл€ подсветки блоков в контекстном меню
 * @author Home
 *
 */
public class MenuPanelListener extends MouseAdapter 
{
	private final Color ROLLOVER_COLOR 	 = new Color( 200, 200, 200 );
	private final Color BACKGROUND_COLOR = Color.white;
	
	public void mouseEntered( MouseEvent e )
	{
		JPanel panel = (JPanel) e.getSource();
		panel.setBackground( ROLLOVER_COLOR );
	}
	
	public void mouseExited( MouseEvent e )
	{
		JPanel panel = (JPanel) e.getSource();
		panel.setBackground( BACKGROUND_COLOR );
	}
}
