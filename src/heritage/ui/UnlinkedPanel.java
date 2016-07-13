package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.HScrollBar;
import heritage.relationship.Relation;
import heritage.ui.block.Block;

import java.awt.Dimension;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class UnlinkedPanel 
{
	private final static int SYS_PANEL_HEIGHT 		= Integer.parseInt( Config.getItem( "sys_panel_height" ) );
	private final static int UNLINKED_PADDING 		= Integer.parseInt( Config.getItem( "unlinked_padding" ) );
	public final static int UNLINKED_PANEL_WIDTH 	= Block.BLOCK_WIDTH + UNLINKED_PADDING * 2 + 12;//Integer.parseInt( Config.getItem( "unlinked_panel_width" ) );
	private static JPanel unlinkedPanel;
	private static JPanel innerPanel;
	private static JScrollPane scrollFrame;
		
	private static Logger log = Logger.getLogger(UnlinkedPanel.class.getName());
	
	public static JPanel create( int frameWidth, int frameHeight )
	{
		int x = frameWidth;
		int y = SYS_PANEL_HEIGHT*2;
		int w = UNLINKED_PANEL_WIDTH;
		int h = frameHeight - SYS_PANEL_HEIGHT * 3;
				
		unlinkedPanel = new JPanel( );
		unlinkedPanel.setBounds( x, y, w, h );
		unlinkedPanel.setBackground( ApplicationColors.UNLINKED_PANEL_BACKGROUND_COLOR );
		unlinkedPanel.setOpaque( true );
		unlinkedPanel.setLayout( null );
		//unlinkedPanel.setLayout(new BoxLayout(unlinkedPanel, BoxLayout.PAGE_AXIS));
		unlinkedPanel.setName( "unlinked" );
		unlinkedPanel.setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, ApplicationColors.FIELD_BORDER_UNFOCUSED ) );
		
		innerPanel = new JPanel( );
		innerPanel.setBackground( ApplicationColors.UNLINKED_PANEL_BACKGROUND_COLOR );
		innerPanel.setLayout( new BoxLayout( innerPanel, BoxLayout.PAGE_AXIS ) );
		innerPanel.setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, ApplicationColors.FIELD_BORDER_UNFOCUSED ) );

        scrollFrame = new JScrollPane( innerPanel );
        scrollFrame.setBorder( null );
        scrollFrame.setPreferredSize( new Dimension( w, h ) );
        scrollFrame.setVerticalScrollBar( new HScrollBar( JScrollBar.VERTICAL ) );
        scrollFrame.setHorizontalScrollBar( new HScrollBar( JScrollBar.HORIZONTAL ) );
        scrollFrame.setBounds( 0, 0, w, h );
        unlinkedPanel.add( scrollFrame );
		
						
		log.info( "Unlinked panel is drawn..." );
		
		drawUnlinkedContacts( );
		
		return unlinkedPanel;
	}
	
	public static void drawUnlinkedContacts( )
	{
		resetPanel();
		
		List<Contact> contacts = Relation.getUnlinkedContacts();

		innerPanel.add(Box.createRigidArea( new Dimension( 0, UNLINKED_PADDING ) ) );
		for( Contact contact : contacts )
		{
			contact.isLinked = false;
			innerPanel.add( new Block( contact ) );
			innerPanel.add( Box.createRigidArea( new Dimension( 0, Block.BLOCK_MAX_HEIGHT - Block.BLOCK_HEIGHT + UNLINKED_PADDING ) ) );		
		}
	}
	
	public static void resetPanel()
	{
		innerPanel.removeAll();
		innerPanel.revalidate();
	    innerPanel.repaint();
	}
	
}