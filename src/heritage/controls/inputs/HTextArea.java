package heritage.controls.inputs;

import heritage.config.ApplicationColors;
import heritage.config.Config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class HTextArea extends JTextArea
{
	private static final long serialVersionUID = -2311079818256674307L;
	private String defaultText;
	
	private final int FIELD_WIDTH 					= Integer.parseInt( Config.getItem( "field_width" ) );
	private final int FIELD_HEIGHT 					= Integer.parseInt( Config.getItem( "field_height" ) );
	private final int TEXT_FONT_SIZE				= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final int BORDER_THICKNESS				= Integer.parseInt( Config.getItem( "border_thickness" ) );
	private final int PADDING 						= 8;
	private final String TEXT_FONT_NAME				= Config.getItem( "app_font_name" );
	private final Color SELECTION_HINT_COLOR 		= new Color( 150, 150, 150 );
	
	public HTextArea()
	{
		super( );
		setStyle( );
	}
	
	public HTextArea( String deft )
	{
		super( deft );
		defaultText = deft;	
		setStyle( );	 
	}
	
	public HTextArea( String deft, String value )
	{
		super( value );
		defaultText = deft;	
		setStyle( );	 
		setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
		setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
	}
		
	private void setStyle( )
	{
		setBackground( ApplicationColors.FIELD_BACKGROUND_COLOR );
		setSize( FIELD_WIDTH, FIELD_HEIGHT );
		setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
		setFont( new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE ) );
	}
	
	 @Override
	 protected void paintComponent( Graphics g )
	 {
		 super.paintComponent( g );
	
		 JScrollPane scroll = (JScrollPane) this.getParent().getParent();

		 // рисуем границу в зависимости от состояния поля
		 
		 // для scrollpane
		 Color borderColor = ( this.hasFocus() ) ? ApplicationColors.FIELD_BORDER_FOCUSED : ApplicationColors.FIELD_BORDER_UNFOCUSED;
		 Border line = BorderFactory.createLineBorder( borderColor, BORDER_THICKNESS );
		 scroll.setBorder( line );
		 
		 // для textarea
		 Border empty = new EmptyBorder( PADDING, PADDING, PADDING, PADDING );
		 setBorder( empty );

		 // если есть подсказка выделенного поля, определяем ее состояние
		 Color labelColor = ( this.hasFocus() ) ? SELECTION_HINT_COLOR : null;
		 boolean isOpaque = ( this.hasFocus() ) ? true : false;
		 Component c = scroll.getParent().getComponents()[0];
		 
		 if( c instanceof JLabel )
		 {
			 JLabel labelFor = (JLabel) scroll.getParent().getComponents()[0];
			 labelFor.setOpaque( isOpaque );
			 labelFor.setBackground( labelColor );
		 }
		 
		 if( defaultText != null )
		 { 
			 if( this.hasFocus() )
			 {
				 if( this.getText().equals( defaultText ) )
				 {
					 setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
					 setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
					 setText( "" ); 
				 }
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