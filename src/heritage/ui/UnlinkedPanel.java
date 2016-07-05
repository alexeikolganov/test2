package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.config.Config;

import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

public class UnlinkedPanel 
{
	private final static int SYS_PANEL_HEIGHT 		= Integer.parseInt( Config.getItem( "sys_panel_height" ) );
	private final static int UNLINKED_PANEL_WIDTH 	= Integer.parseInt( Config.getItem( "unlinked_panel_width" ) );
		
	private static Logger log = Logger.getLogger(UnlinkedPanel.class.getName());
	
	public static JLayeredPane create( int frameWidth, int frameHeight )
	{
		int x = frameWidth;
		int y = SYS_PANEL_HEIGHT*2;
		int w = UNLINKED_PANEL_WIDTH;
		int h = frameHeight - SYS_PANEL_HEIGHT * 3;
				
		JLayeredPane unlinkedPanel = new JLayeredPane( );
		unlinkedPanel.setBounds( x, y, w, h );
		unlinkedPanel.setBackground( ApplicationColors.UNLINKED_PANEL_BACKGROUND_COLOR );
		unlinkedPanel.setOpaque( true );
		unlinkedPanel.setLayout( null );
		//unlinkedPanel.setVisible(false);
		unlinkedPanel.setName( "unlinked" );
		unlinkedPanel.setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, ApplicationColors.FIELD_BORDER_UNFOCUSED ) );
						
		log.info( "Unlinked panel is drawn..." );
		
		return unlinkedPanel;
	}
	
}