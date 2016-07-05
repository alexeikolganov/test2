package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.relationship.DPanel;
import heritage.relationship.RelationshipPanel;

import java.util.logging.Logger;

import javax.swing.BorderFactory;

public class LinkedPanel 
{
	private final static int SYS_PANEL_HEIGHT 	= Integer.parseInt( Config.getItem( "sys_panel_height" ) );
	private final static int TITLE_BAR_HEIGHT 	= Integer.parseInt( Config.getItem( "title_bar_height" ) );
		
	private static Logger log = Logger.getLogger(LinkedPanel.class.getName());
	
	public static DPanel create( int frameWidth, int frameHeight )
	{
		int x = 0;
		int y = SYS_PANEL_HEIGHT + TITLE_BAR_HEIGHT;
		int w = frameWidth;
		int h = frameHeight - 				// высота фрейма
				( TITLE_BAR_HEIGHT ) - 		// высота верхней панели с отступами
				( SYS_PANEL_HEIGHT ) - 		// высота верхней панели с отступами
				( SYS_PANEL_HEIGHT );		// высота нижней панели с отступами
				
		DPanel linkedPanel = RelationshipPanel.createPanel( w, h ); //new JPanel( );
		linkedPanel.setBounds( x, y, w, h );
		linkedPanel.setBackground( ApplicationColors.LINKED_PANEL_BACKGROUND_COLOR );
		linkedPanel.setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, ApplicationColors.FIELD_BORDER_UNFOCUSED ) );
		//linkedPanel.setLayout( null );
				
		log.info( "Linked panel is drawn..." );
		
		return linkedPanel;
	}
	
}