package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.config.Config;

import java.util.logging.Logger;

import javax.swing.JPanel;

public class BottomPanel 
{
	private final static int SYS_PANEL_HEIGHT 	= Integer.parseInt( Config.getItem( "sys_panel_height" ) );	
	private static Logger log = Logger.getLogger(BottomPanel.class.getName());
	
	public static JPanel create( int frameWidth, int frameHeight )
	{
		int x = 0;
		int y = frameHeight - SYS_PANEL_HEIGHT;
		int w = frameWidth;
		int h = SYS_PANEL_HEIGHT;
		
		JPanel bottomPanel = new JPanel( );
		bottomPanel.setBounds( x, y, w, h );
		bottomPanel.setBackground( ApplicationColors.PANEL_BACKGROUND_COLOR );
		
		log.info( "Status bar is drawn..." );
		
		return bottomPanel;
	}
	
}