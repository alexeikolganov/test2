package heritage.controls.inputs;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

public class HCheckBox extends JCheckBox
{

	private static final long serialVersionUID  = 1L;
	private final String ICON_PATH 				= "icons/";
	private final String DEFAULT_ICON 			= ICON_PATH + "checkbox_16.png";
	private final String CHECKED_ICON 			= ICON_PATH + "checkbox_checked_16.png";
	private final String DISABLED_ICON 			= ICON_PATH + "checkbox_16.png";
	private final String DISABLED_CHECKED_ICON 	= ICON_PATH + "checkbox_checked_16.png";
	private final String PRESSED_ICON 			= ICON_PATH + "checkbox_16.png";
	private final String ROLLOVER_ICON 			= ICON_PATH + "checkbox_16.png";
	private final String ROLLOVER_CHECKED_ICON 	= ICON_PATH + "checkbox_checked_16.png";
	
	public HCheckBox( )
	{
		super( );
		setStyling( );	
	}
	
	public HCheckBox( String text )
	{
		super( text );
		setStyling( );	
	}
	
	private void setStyling( )
	{
		//setSelected( true );
		setIcon( new ImageIcon( DEFAULT_ICON ) );
        setSelectedIcon( new ImageIcon( CHECKED_ICON ) );
        setDisabledIcon( new ImageIcon( DISABLED_ICON ) );
        setDisabledSelectedIcon( new ImageIcon( DISABLED_CHECKED_ICON ) );
        setPressedIcon( new ImageIcon( PRESSED_ICON ) );
        setRolloverIcon( new ImageIcon( ROLLOVER_ICON ) );
        setRolloverSelectedIcon( new ImageIcon( ROLLOVER_CHECKED_ICON ) );      
        setBackground( Color.white );
	}
}
