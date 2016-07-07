package heritage.controls.inputs;

import heritage.config.ApplicationColors;
import heritage.config.Config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class HTextField extends JTextField
{
	private static final long serialVersionUID = -2311079818256674307L;
	private String defaultText;
	
	private final int FIELD_WIDTH 					= Integer.parseInt( Config.getItem( "field_width" ) );
	private final int FIELD_HEIGHT 					= Integer.parseInt( Config.getItem( "field_height" ) );
	private final int BORDER_THICKNESS				= Integer.parseInt( Config.getItem( "border_thickness" ) );
	private final int TEXT_FONT_SIZE				= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final String TEXT_FONT_NAME				= Config.getItem( "app_font_name" );
	private final int PADDING 						= 8;
	
	public HTextField()
	{
		super( );
		setStyle( );
	}
	
	public HTextField( String deft )
	{
		super( deft );
		defaultText = deft;	
		setStyle( );	 
		setFont( new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE ) );
	}
	
	public HTextField( String deft, String value )
	{
		super( value );
		defaultText = deft;	
		setStyle( );	 
		setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
		setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
	}
		
	private void setStyle( )
	{
		setBackground( ApplicationColors.FIELD_BACKGROUND_COLOR );
		setSize( FIELD_WIDTH, FIELD_HEIGHT );
		setPreferredSize( new Dimension( FIELD_WIDTH, FIELD_HEIGHT ) );
		setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
	}
	
	 @Override
	 protected void paintComponent( Graphics g )
	 {
		 super.paintComponent( g );
	
		 // рисуем границу в зависимости от состояния поля
		 Color borderColor = ( this.hasFocus() ) ? ApplicationColors.FIELD_BORDER_FOCUSED : ApplicationColors.FIELD_BORDER_UNFOCUSED;
		 Border line = BorderFactory.createLineBorder( borderColor, BORDER_THICKNESS );
		 Border empty = new EmptyBorder( 0, PADDING, 0, PADDING );
		 CompoundBorder border = new CompoundBorder( line, empty );
		 setBorder( border );
		 	 
		 if( defaultText != null )
		 { 
			 if( this.hasFocus() )
			 {
				 if( this.getText().equals( defaultText ) )
				 {	
					 setText( "" ); 
				 }
				 setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
				 setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
			 }
			 else
			 { 
				 if( this.getText().equals("") )
				 {
					 this.setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
					 setFont( new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE ) );
					 this.setText( defaultText );
				 }
			 } 
		}
		else
		{
			setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
			setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
		}
		 
		this.repaint( );
	}
	 
	public void focusGained( FocusEvent e )
	{
		this.repaint();
	}

	public void focusLost( FocusEvent e )
	{
		this.repaint();
	}
}