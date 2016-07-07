package heritage.controls;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.buttons.HButton;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class HOptionPane extends HDialog
{

	private static final long serialVersionUID 	= 5466259953203242190L;
	private static int WIDTH  			= 300;
	private static int HEIGHT 			= 160;
	private final int TITLE_BAR_HEIGHT 			= Integer.parseInt( Config.getItem( "title_bar_height" ) );
	private final int BORDER_THICKNESS			= Integer.parseInt( Config.getItem( "border_thickness" ) );
	private final int TEXT_FONT_SIZE			= 16;//Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final String TEXT_FONT_NAME			= Config.getItem( "app_font_name" );
	
	private JPanel boxPanel;
	private JPanel messagePanel;
	private JPanel buttonPanel;
	
	private HButton ok;
	private HButton cancel;
	
	public Status status;
	
	public enum OptionType
	{
		OK,
		OK_CANCEL,
		YES_NO
	}
	
	public enum Status
	{
		OK,
		CANCEL
	}
	
	public HOptionPane( JFrame parent, String title, String message ) 
	{
		this( parent, title, message, OptionType.OK, WIDTH, HEIGHT );
	}
	
	public HOptionPane( JFrame parent, String title, String message, OptionType type ) 
	{
		this( parent, title, message, type, WIDTH, HEIGHT );
	}
	
	public HOptionPane( JFrame parent, String title, String message, OptionType type, int width, int height ) 
	{
		super( parent, title, width, height );	
		createBoxPanel( width, height, message );
		createButtons( type );
		setVisible( true );	
	}
	
	private void createButtons( OptionType type )
	{
		switch( type )
		{
			case OK_CANCEL:
				ok = new HButton( "OK" );
				ok.addActionListener( new ActionListener() 
				{
					public void actionPerformed( ActionEvent ev ) 		
					{
						dispose();
						status = Status.OK;
					}
					
				});
				buttonPanel.add( ok );
				cancel = new HButton( "Cancel" );
				cancel.addActionListener( new ActionListener() 
				{
					public void actionPerformed( ActionEvent ev ) 		
					{
						dispose();
						status = Status.CANCEL;
					}
					
				});
				buttonPanel.add( cancel );
				break;
			case YES_NO:
				ok = new HButton( "Yes" );
				ok.addActionListener( new ActionListener() 
				{
					public void actionPerformed( ActionEvent ev ) 		
					{
						dispose();
						status = Status.OK;
					}
					
				});
				buttonPanel.add( ok );
				cancel = new HButton( "No" );
				cancel.addActionListener( new ActionListener() 
				{
					public void actionPerformed( ActionEvent ev ) 		
					{
						dispose();
						status = Status.CANCEL;
					}
					
				});
				buttonPanel.add( cancel );
				break;	
			case OK:
			default:
				ok = new HButton( "OK" );
				ok.addActionListener( new ActionListener() 
				{
					public void actionPerformed( ActionEvent ev ) 		
					{
						dispose();
						status = Status.OK;
					}
					
				});

				buttonPanel.add( ok );
				break;
		}
	}
	
	private void createBoxPanel( int width, int height, String message )
	{
		// box container
		boxPanel = new JPanel();
		boxPanel.setLayout( new BoxLayout(boxPanel, BoxLayout.Y_AXIS) );
		boxPanel.setBounds( BORDER_THICKNESS, TITLE_BAR_HEIGHT, width - BORDER_THICKNESS*2, height - TITLE_BAR_HEIGHT - BORDER_THICKNESS );
		boxPanel.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		getPanel().add( boxPanel );
		
		// a container for messages
		messagePanel = new JPanel();
		messagePanel.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		boxPanel.add( messagePanel );
		
		// the message withinh a textarea which allows to wrap the text
		JTextArea textArea = new JTextArea( message );
	    textArea.setWrapStyleWord( true );
	    textArea.setLineWrap( true );
	    textArea.setOpaque( false );
	    textArea.setEditable( false );
	    textArea.setFocusable( false );
	    textArea.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
	    textArea.setPreferredSize( new Dimension( width - BORDER_THICKNESS*2, height - TITLE_BAR_HEIGHT * 2 - 12 ));
	    textArea.setMinimumSize( new Dimension( width - BORDER_THICKNESS*2, height - TITLE_BAR_HEIGHT * 2 - 12 ));
	    textArea.setBorder( new EmptyBorder( 4, 16, 4, 4 ) );
	    
		messagePanel.add( textArea );
		
		// a container for buttons
		buttonPanel = new JPanel();
		buttonPanel.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		buttonPanel.setMinimumSize( new Dimension(width - BORDER_THICKNESS*2, TITLE_BAR_HEIGHT ));
		buttonPanel.setMaximumSize( new Dimension(width - BORDER_THICKNESS*2, TITLE_BAR_HEIGHT ));
		boxPanel.add( buttonPanel );
	}

}
