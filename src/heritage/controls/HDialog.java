package heritage.controls;

import heritage.config.ApplicationColors;
import heritage.config.Config;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HDialog extends JDialog 
{

	private static final long serialVersionUID 	= 1L;
	private final int BORDER_THICKNESS			= Integer.parseInt( Config.getItem( "border_thickness" ) );
	private final int TITLE_BAR_HEIGHT 			= Integer.parseInt( Config.getItem( "title_bar_height" ) );
	
	public HDialog( JFrame parent, String title, int width, int height ) 
	{
		super( parent );
		setUndecorated( true );
		
		setSize(width, height);
		
        setModalityType( ModalityType.APPLICATION_MODAL );
        setLocationRelativeTo( getParent() );
               	
		HTitleBar titleBar = new HTitleBar( title, width, this, true, false );
		getContentPane().add( titleBar );
	    
	    JPanel panel = new JPanel( );
	    panel.setBounds( 0, TITLE_BAR_HEIGHT, width, width - TITLE_BAR_HEIGHT );
	    panel.setBorder( BorderFactory.createMatteBorder( 0, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, Color.black ) );
	    panel.setBackground( ApplicationColors.DIALOG_BACKGROUND_COLOR );
	    panel.setLayout( null );
	    getContentPane().add( panel );

	    //setVisible( true );
	}

	
} 

