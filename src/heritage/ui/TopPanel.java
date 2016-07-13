package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.buttons.HMenuButton;
import heritage.ui.modal.ModalEdit;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TopPanel 
{
	private final static int SYS_PANEL_HEIGHT 		= Integer.parseInt( Config.getItem( "sys_panel_height" ) );		
	public static JLayeredPane topPanel;
	
	private static final String ICONS_PATH  = Config.getItem( "icons_path" ) + "/";
	private static final String LEFT_ARROW  = ICONS_PATH + "left-arrow.png";
	private static final String RIGHT_ARROW = ICONS_PATH + "right-arrow.png";
	private static final String PLUS 		= ICONS_PATH + "plus.png";
	
	private static Logger log = Logger.getLogger(TopPanel.class.getName());
	
	public static JLayeredPane create( int frameWidth )
	{
		int x = 0;
		int y = SYS_PANEL_HEIGHT;
		int w = frameWidth;
		int h = SYS_PANEL_HEIGHT;
		
		topPanel = new JLayeredPane( );
		topPanel.setBounds( x, y, w, h );
		topPanel.setBackground( ApplicationColors.PANEL_BACKGROUND_COLOR );
		topPanel.setLayout( null );
		topPanel.setOpaque( true );
		topPanel.setName( "menu" );
				
		createNewAccountButton( );
		
		createUnlinkedContactsButton( frameWidth );
		
		log.info( "Menu panel is drawn..." );
				
		return topPanel;
	}
	
	/**
	 * Создаем кнопку дя содания нового контакта
	 */
	private static void createNewAccountButton( )
	{
		final String BUTTON_LABEL = Config.getItem( "new_contact_label" );
		HMenuButton newAccountButton = new HMenuButton( BUTTON_LABEL, PLUS );
		newAccountButton.setBounds( 10, 0, 150, SYS_PANEL_HEIGHT );
			
		newAccountButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				Contact temp = new Contact( "", true );
				new ModalEdit( BUTTON_LABEL, temp );				
			}
			
		});
		
		topPanel.add( newAccountButton, 10 );
	}
	
	private static void createUnlinkedContactsButton( final int frameWidth )
	{
		HMenuButton unlinkedContactsButton = new HMenuButton( );
		unlinkedContactsButton.setBounds( frameWidth-SYS_PANEL_HEIGHT-10, 0, SYS_PANEL_HEIGHT, SYS_PANEL_HEIGHT );
		
		try 
		{
			//Image img = ImageIO.read( new FileInputStream("icons/left-arrow.png") );
			//unlinkedContactsButton.setIcon( new ImageIcon(img) );
			
			Image img = ImageIO.read( new FileInputStream( LEFT_ARROW ) );
			Image newimg = img.getScaledInstance( HMenuButton.ICON_SIZE, HMenuButton.ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			unlinkedContactsButton.setIcon( new ImageIcon(newimg) );
			
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find '" + LEFT_ARROW + "': ", ex );
		}
		
		unlinkedContactsButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{		
				HMenuButton toggler = (HMenuButton)ev.getSource();
				
				final JPanel unlinkedPane = (JPanel) HeritageUI.getComponentByName("unlinked");
				String icon = LEFT_ARROW;
				if( unlinkedPane.getX() >= frameWidth )
				{
					icon = RIGHT_ARROW;
					//System.out.println( "1. x="+unlinkedPane.getX() +", w="+ frameWidth);
					new Timer( 1, new ActionListener() 
			        {
						public void actionPerformed( ActionEvent e ) 
						{
							// move to the left
							unlinkedPane.setLocation( unlinkedPane.getX() - 1, SYS_PANEL_HEIGHT*2 );
			                if( unlinkedPane.getX() == frameWidth - UnlinkedPanel.UNLINKED_PANEL_WIDTH ) 
			                {
			                	((Timer) e.getSource()).stop( );
			                    
			                }
						}
			         }).start();
				}
				else
				{
					icon = LEFT_ARROW;
					//System.out.println( "3. x="+unlinkedPane.getX() +", w="+ frameWidth);
					new Timer( 1, new ActionListener() 
		            {
						public void actionPerformed(ActionEvent e) 
						{
							// move to the right
							unlinkedPane.setLocation( unlinkedPane.getX() + 1, SYS_PANEL_HEIGHT*2 );
							if( unlinkedPane.getX() == frameWidth )
							{
								((Timer) e.getSource()).stop( );
								//System.out.println("Timer stopped");
							}
						}
		            }).start();
				}
				
				try 
				{
					//Image img = ImageIO.read( new FileInputStream(icon) );
					//toggler.setIcon( new ImageIcon(img) );
					
					Image img = ImageIO.read( new FileInputStream( icon ) );
					Image newimg = img.getScaledInstance( HMenuButton.ICON_SIZE, HMenuButton.ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
					toggler.setIcon( new ImageIcon(newimg) );
				} 
				catch( IOException ex ) 
				{
					log.log( Level.SEVERE, "Failed to find '" + icon + "': ", ex );
				}
			}
		});
		
		topPanel.add( unlinkedContactsButton, 10 );
	}
	
}