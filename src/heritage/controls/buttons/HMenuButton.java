package heritage.controls.buttons;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.ui.TopPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

/*
 * Видоизмененные кнопки для приложения
 */
public class HMenuButton extends JButton 
{
	public static final int FONT_SIZE	  		= Integer.parseInt( Config.getItem( "menu_button_font_size" ) );
	public static final int ICON_SIZE	  		= Integer.parseInt( Config.getItem( "block_button_size" ) );
	
	private static final String FONT_NAME		= Config.getItem( "app_font_name" );
	
	private static Logger log = Logger.getLogger(TopPanel.class.getName());
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HMenuButton( )
	{
		super.setContentAreaFilled( false );
		
		setStyle();
	}
	
	public HMenuButton( String text )
	{
		super( text );
		super.setContentAreaFilled( false );
		
		setStyle( );
	}
	
	public HMenuButton( String text, String imagePath )
	{
		super( text );
		super.setContentAreaFilled( false );
		try 
		{
			//Image img = ImageIO.read( new FileInputStream( imagePath ) );
			//setIcon( new ImageIcon(img) );
			
			Image img = ImageIO.read( new FileInputStream( imagePath ) );
			Image newimg = img.getScaledInstance( ICON_SIZE, ICON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			setIcon( new ImageIcon(newimg) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find '" + imagePath + "': ", ex );
		}
		
		setStyle( );
	}
	
	private void setStyle()
	{
		setForeground( ApplicationColors.BUTTON_MENU_FOREGROUND_COLOR );
		setBackground( ApplicationColors.BUTTON_MENU_BACKGROUND_COLOR );
		setFocusPainted( false );
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder( emptyBorder );
		
		setFont( new Font( FONT_NAME, Font.PLAIN, FONT_SIZE ) );
	}
		

	@Override
	public Dimension getPreferredSize( ) 
	{
	    Dimension size = super.getPreferredSize();
	    size.setSize( 40, 40 );
	    return size;
	}
	
	 @Override
     protected void paintComponent( Graphics g ) 
	 { 
		 if( getModel().isPressed() ) 
         {
			 //System.out.println( PRESSED_COLOR);
			 g.setColor( ApplicationColors.BUTTON_MENU_PRESSED_COLOR );
         } 
         else if( getModel().isRollover() ) 
         {
        	 //System.out.println( HOVER_COLOR);
        	 g.setColor( ApplicationColors.BUTTON_MENU_HOVER_COLOR );
         } 
         else 
         {
             g.setColor( getBackground() );
         }
         g.fillRect( 0, 0, getWidth(), getHeight() );
         super.paintComponent( g );
     }
}
