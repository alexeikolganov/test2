package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.buttons.HMenuButton;
import heritage.ui.modal.ModalEdit;
import heritage.ui.modal.Modal;
import heritage.ui.modal.ReadOnlyModal;

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
import javax.swing.Timer;

public class TopPanel 
{
	private final static int SYS_PANEL_HEIGHT 		= Integer.parseInt( Config.getItem( "sys_panel_height" ) );
	private final static int UNLINKED_PANEL_WIDTH 	= Integer.parseInt( Config.getItem( "unlinked_panel_width" ) );
		
	public static JLayeredPane topPanel;
	
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
		HMenuButton newAccountButton = new HMenuButton( BUTTON_LABEL, "icons/plus_15.png" );
		newAccountButton.setBounds( 10, 0, 150, SYS_PANEL_HEIGHT );
			
		newAccountButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				Contact temp = new Contact( "", "", true );
				new ModalEdit( BUTTON_LABEL, temp );				
			}
			
		});
		
		topPanel.add( newAccountButton, 10 );
		
		HMenuButton newAccountButton2 = new HMenuButton( "read-only", "icons/plus_15.png" );
		newAccountButton2.setBounds( 200, 0, 150, SYS_PANEL_HEIGHT );
			
		newAccountButton2.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				new ReadOnlyModal( BUTTON_LABEL );				
			}
			
		});
		
		topPanel.add( newAccountButton2, 10 );
		
		HMenuButton newAccountButton3 = new HMenuButton( "abstract", "icons/plus_15.png" );
		newAccountButton3.setBounds( 400, 0, 150, SYS_PANEL_HEIGHT );
			
		newAccountButton3.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				Contact temp = new Contact( "", "", true );
				new Modal( BUTTON_LABEL, temp );				
			}
			
		});
		
		topPanel.add( newAccountButton3, 10 );
	}
	
	private static void createUnlinkedContactsButton( final int frameWidth )
	{
		HMenuButton unlinkedContactsButton = new HMenuButton( );
		unlinkedContactsButton.setBounds( frameWidth-SYS_PANEL_HEIGHT-10, 0, SYS_PANEL_HEIGHT, SYS_PANEL_HEIGHT );
		
		try 
		{
			Image img = ImageIO.read( new FileInputStream("icons/left-arrow_15.png") );
			unlinkedContactsButton.setIcon( new ImageIcon(img) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find 'left-arrow_15.png': ", ex );
		}
		
		unlinkedContactsButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{		
				HMenuButton toggler = (HMenuButton)ev.getSource();
				
				final JLayeredPane unlinkedPane = (JLayeredPane) HeritageUI.getComponentByName("unlinked");
				String icon = "icons/left-arrow_15.png";
				if( unlinkedPane.getX() >= frameWidth )
				{
					icon = "icons/right-arrow_15.png";
					//System.out.println( "1. x="+unlinkedPane.getX() +", w="+ frameWidth);
					new Timer( 1, new ActionListener() 
			        {
						public void actionPerformed( ActionEvent e ) 
						{
							// move to the left
							unlinkedPane.setLocation( unlinkedPane.getX() - 1, SYS_PANEL_HEIGHT*2 );
			                if( unlinkedPane.getX() == frameWidth - UNLINKED_PANEL_WIDTH ) 
			                {
			                	((Timer) e.getSource()).stop( );
			                    
			                }
						}
			         }).start();
				}
				else
				{
					icon = "icons/left-arrow_15.png";
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
					Image img = ImageIO.read( new FileInputStream(icon) );
					toggler.setIcon( new ImageIcon(img) );
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