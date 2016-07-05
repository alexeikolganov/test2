package heritage.controls.inputs;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.buttons.HButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

@SuppressWarnings("rawtypes")
public class HComboBox extends JComboBox
{
	private static final long serialVersionUID 		= 1L;
	private final int TEXT_FONT_SIZE				= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final int BORDER_THICKNESS				= Integer.parseInt( Config.getItem( "border_thickness" ) ); 
	private final String TEXT_FONT_NAME				= Config.getItem( "app_font_name" );
	
	@SuppressWarnings("unchecked")
	public HComboBox( String[] list, int idx )
	{
		super( list );
		setSelectedIndex( idx );
	
		Border line = BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_FOCUSED, BORDER_THICKNESS );
		setBorder( line );

		setRenderer( new ComboBoxRenderer() );
		setUI( new HComboBoxUI() );
		setBackground( ApplicationColors.FIELD_BACKGROUND_COLOR );
		
		setMaximumRowCount( getModel().getSize() );
		setFocusable( false );
		
		setFont( new Font( TEXT_FONT_NAME, ( idx == 0 )? Font.ITALIC : Font.PLAIN, TEXT_FONT_SIZE ) );
		setForeground( Color.gray );
				
		addPopupMenuListener(new PopupMenuListener() 
		{	
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
			{
				Border line = BorderFactory.createMatteBorder( BORDER_THICKNESS, BORDER_THICKNESS, 0, BORDER_THICKNESS, ApplicationColors.FIELD_BORDER_FOCUSED );
				Border empty = new EmptyBorder( 0, 0, BORDER_THICKNESS, 0 );
				CompoundBorder border = new CompoundBorder( line, empty );
				setBorder( border ); 
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) 
			{
				//Border line = BorderFactory.createLineBorder( BORDER_UNFOCUSED, BORDER_THICKNESS );
				//setBorder( line );
				if( getSelectedItem().toString().equals( getItemAt(0).toString() ) )
				{
					setFont( new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE ) );
					setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
					Border line = BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_UNFOCUSED, BORDER_THICKNESS );
					setBorder( line );
				}
				else
				{
					setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
					setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
					Border line = BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_FOCUSED, BORDER_THICKNESS );
					setBorder( line );
				}	
			}

			public void popupMenuCanceled(PopupMenuEvent e) 
			{
				//Border line = BorderFactory.createLineBorder( BORDER_UNFOCUSED, BORDER_THICKNESS );
				//setBorder( line );
				if( getSelectedItem().toString().equals( getItemAt(0).toString() ) )
				{
					setFont( new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE ) );
					setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
					Border line = BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_UNFOCUSED, BORDER_THICKNESS );
					setBorder( line );
				}
				else
				{
					setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
					setForeground( ApplicationColors.FIELD_FOREGROUND_SELECTED_COLOR );
					Border line = BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_FOCUSED, BORDER_THICKNESS );
					setBorder( line );
				}
			}
		});
	}
	
	@Override
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		// если есть подсказка выделенного поля, определяем ее состояние
		Color labelColor = ( this.hasFocus() ) ? ApplicationColors.FIELD_SELECTION_HINT_COLOR : null;
		boolean isOpaque = ( this.hasFocus() ) ? true : false;
		Component c = this.getParent().getComponents()[0];
		if( c instanceof JLabel )
		{
			JLabel labelFor = (JLabel) this.getParent().getComponents()[0];
			labelFor.setOpaque( isOpaque );
			labelFor.setBackground( labelColor );
		}
		 
		this.repaint( );
	}
}

class HComboBoxUI extends BasicComboBoxUI
{
	private final Color BORDER_FOCUSED = new Color( 150, 150, 150 );
	private final int BORDER_THICKNESS	= Integer.parseInt( Config.getItem( "border_thickness" ) ); 

	@Override
	protected HButton createArrowButton( )
	{
		HButton arrow = new HButton( );
		arrow.setBorder( null );
		ImageIcon icon = new ImageIcon( "icons/chevron_32.png" );
		arrow.setIcon( icon );
		arrow.setBackground( Color.white );
		return arrow;
	}
	
	@Override
	protected ComboPopup createPopup()
	{
		BasicComboPopup bcp = (BasicComboPopup) super.createPopup();
		bcp.setBorder( BorderFactory.createMatteBorder( 0, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_FOCUSED ) );
		return bcp;
	}
}

class ComboBoxRenderer extends BasicComboBoxRenderer
{
	private static final long serialVersionUID = 1L;
	private final int PADDING 			= 8;
	private final int TEXT_FONT_SIZE	= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final String TEXT_FONT_NAME	= Config.getItem( "app_font_name" );
	
	public ComboBoxRenderer( )
	{
		super( );
		setOpaque( true );
	}

	@Override
	public void paint( Graphics g ) 
	{
		super.paint( g );
	}
	
	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent( JList list, Object value, int index,boolean isSelected, boolean cellHasFocus)
	{ 
		setText( value.toString() );
		setPreferredSize( new Dimension( 200, 25 ) ); 
		setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) ); 
		Border empty = new EmptyBorder( 0, PADDING, 0, 0 );
		setBorder( empty );
		if( isSelected ) 
		{
			setBackground( new Color(200, 200, 200) ); 
		}
		else 
		{
			setBackground( Color.white );
		} 
		return this;
	}
}

