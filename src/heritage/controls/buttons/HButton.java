package heritage.controls.buttons;

import heritage.config.ApplicationColors;
import heritage.config.Config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/*
 * �������������� ������ ��� ����������
 * ������������ �����, ������� ����� �������������� ��� �� ���� ���� ��� �������� ��� �������� �������
 */
public class HButton extends JButton 
{	
	private final int TEXT_FONT_SIZE				= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final String TEXT_FONT_NAME				= Config.getItem( "app_font_name" );
	
	private Color foregroundColor;
	private Color backgroundColor;
	private Color pressedColor;
	private Color hoverColor;
		
	private static final long serialVersionUID = 1L;

	/**
	 * ����������� ��� ������ ��� ������ � ����������������� �������
	 */
	public HButton( )
	{
		super( );
		super.setContentAreaFilled( false );
		
		foregroundColor = ApplicationColors.BUTTON_DEFAULT_FOREGROUND_COLOR;
		backgroundColor = ApplicationColors.BUTTON_DEFAULT_BACKGROUND_COLOR;
		pressedColor	= ApplicationColors.BUTTON_DEFAULT_PRESSED_COLOR;
		hoverColor		= ApplicationColors.BUTTON_DEFAULT_HOVER_COLOR;
		
		setStyle( );

	}
	
	/**
	 * ����������� ��� ������ � ������� � ����������������� �������
	 */
	public HButton( String text )
	{
		super( text );
		super.setContentAreaFilled( false );
		
		foregroundColor = ApplicationColors.BUTTON_DEFAULT_FOREGROUND_COLOR;
		backgroundColor = ApplicationColors.BUTTON_DEFAULT_BACKGROUND_COLOR;
		pressedColor	= ApplicationColors.BUTTON_DEFAULT_PRESSED_COLOR;
		hoverColor		= ApplicationColors.BUTTON_DEFAULT_HOVER_COLOR;
		
		setStyle( );
	}
	
	/**
	 * ����������� ��� ������ ��� ������ � �������, ������������� � �������� ������
	 */
	public HButton( Color fgC, Color bgC, Color pC, Color hC )
	{
		super( );
		super.setContentAreaFilled( false );
		
		foregroundColor = fgC;
		backgroundColor = bgC;
		pressedColor	= pC;
		hoverColor		= hC;
		
		setStyle( );

	}
	
	/**
	 * ����������� ��� ������ � ������� � �������, ������������� � �������� ������
	 */
	public HButton( String text, Color fgC, Color bgC, Color pC, Color hC )
	{
		super( text );
		super.setContentAreaFilled( false );
		
		foregroundColor = fgC;
		backgroundColor = bgC;
		pressedColor	= pC;
		hoverColor		= hC;
		
		setStyle( );

	}
	
	/**
	 * ���������� ������, ����������� �� ���� ����� ������
	 */
	private void setStyle( )
	{
		setForeground( foregroundColor );
		setBackground( backgroundColor );
		setFocusPainted( false );
		setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		setBorder( emptyBorder );
	}
	
	
	@Override
	public Dimension getPreferredSize( ) 
	{
	    Dimension size = super.getPreferredSize( );
	    size.setSize( 100, 30 );
	    return size;
	}
	
	 @Override
     protected void paintComponent( Graphics g ) 
	 { 
		 if( getModel().isPressed() ) 
         {
			 //System.out.println("isPressed");
			 g.setColor( pressedColor );
         } 
         else if( getModel().isRollover() ) 
         {
             g.setColor( hoverColor );
         } 
         else 
         {
             g.setColor( getBackground() );
         }
         g.fillRect( 0, 0, getWidth(), getHeight() );
         super.paintComponent(g);
     }
}