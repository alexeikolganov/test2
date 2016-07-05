package heritage.controls.buttons;

import heritage.config.ApplicationColors;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/*
 * Видоизмененные кнопки для приложения
 */
public class HContolButton extends JButton 
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HContolButton( )
	{
		setStyling( );
	}
	
	public HContolButton( String text )
	{
		super( text );
		super.setContentAreaFilled( false );
		
		setStyling( );
	}
	
	public HContolButton( Action action )
	{
		super.setContentAreaFilled( false );
		super.setAction( action );
		
		setStyling( );
	}
		
	private void setStyling( )
	{
		setForeground( ApplicationColors.BUTTON_CONTROL_FOREGROUND_COLOR );
		setBackground( ApplicationColors.BUTTON_CONTROL_BACKGROUND_COLOR );
		setFocusPainted( false );

		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder( emptyBorder );
	}
	
	@Override
	public Dimension getPreferredSize( ) 
	{
	    Dimension size = super.getPreferredSize();
	    size.setSize( 24, 24 );
	    return size;
	}
	
	 @Override
     protected void paintComponent( Graphics g ) 
	 { 
		 if( getModel().isPressed() ) 
         {
			 //System.out.println("isPressed");
			 g.setColor( ApplicationColors.BUTTON_CONTROL_PRESSED_COLOR );
         } 
         else if( getModel().isRollover() ) 
         {
             g.setColor( ApplicationColors.BUTTON_CONTROL_HOVER_COLOR );
         } 
         else 
         {
             g.setColor( getBackground() );
         }
         g.fillRect( 0, 0, getWidth(), getHeight() );
         super.paintComponent(g);
     }
}