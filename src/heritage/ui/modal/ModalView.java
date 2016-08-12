package heritage.ui.modal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.HDialog;
import heritage.controls.HScrollBar;

public class ModalView
{
	private final static String TEXT_FONT_NAME		= Config.getItem( "app_font_name" );
	private final static int TEXT_FONT_SIZE			= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final static int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
	private final static int FIELD_HEIGHT			= 24;//Integer.parseInt( Config.getItem( "field_height" ) );
	
	private final int MODAL_WIDTH  			= Integer.parseInt( Config.getItem( "modal_view_width" ) );
	private final int MODAL_HEIGHT 			= Integer.parseInt( Config.getItem( "modal_view_height" ) );
	private final int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );
	
	private final int TOP_SECTION_HEIGHT    = (int) (MODAL_HEIGHT * 0.6);
	private final int BOTTOM_SECTION_HEIGHT = MODAL_HEIGHT - TOP_SECTION_HEIGHT - BORDER_THICKNESS * 2;
	
	private final int MAIN_DETAILS_WIDTH	= (int) ( ( MODAL_WIDTH - BORDER_THICKNESS * 2 ) * 0.4 ) - BORDER_THICKNESS * 2;
	
	private final int MARGIN = BORDER_THICKNESS * 4;
	
	protected HDialog dialog;
	
	private JPanel contentPanel;
	private JPanel topSection;
	private JPanel bottomSection;
	private JPanel mainDetailsSection;
	private JPanel lifeLineSection;
	private JPanel notesSection;
	
	private Contact contact;
	
	private static Logger log = Logger.getLogger(ModalView.class.getName());	
	
	public ModalView( String title, Contact contact ) 
	{ 
		this.contact = contact;
		
		dialog = new HDialog( new JFrame(), title, MODAL_WIDTH, MODAL_HEIGHT );		
		contentPanel = dialog.getPanel();
		contentPanel.setBackground( ApplicationColors.DIALOG_BACKGROUND_COLOR );
		contentPanel.setLayout( null );

		createTopPanel();
		createBottomPanel( );
		createMainDetailsPanel( );
		createLifeLinePanel( );
		createNotesPanel( );
				
		dialog.setVisible( true );	
	}
	
	private void createTopPanel( )
	{
		topSection = new JPanel();
		topSection.setBackground( contact.isMasculine() ? ApplicationColors.MAN_COLOR : ApplicationColors.WOMAN_COLOR );
		//topSection.setBounds( BORDER_THICKNESS, BORDER_THICKNESS, MODAL_WIDTH - BORDER_THICKNESS * 2, TOP_SECTION_HEIGHT );
		topSection.setSize( new Dimension( MODAL_WIDTH - BORDER_THICKNESS * 2, TOP_SECTION_HEIGHT ) );
		topSection.setPreferredSize( new Dimension( MODAL_WIDTH - BORDER_THICKNESS * 2, TOP_SECTION_HEIGHT ) );
		topSection.setMinimumSize( new Dimension( MODAL_WIDTH - BORDER_THICKNESS * 2, TOP_SECTION_HEIGHT ) );
		topSection.setMaximumSize( new Dimension( MODAL_WIDTH - BORDER_THICKNESS * 2, TOP_SECTION_HEIGHT ) );
		contentPanel.add( topSection );
		
		JLabel avatar = new JLabel( "", JLabel.CENTER );
		//avatar.setBounds( ( MODAL_WIDTH - TOP_SECTION_HEIGHT + MARGIN * 4 ) / 2, MARGIN * 5, TOP_SECTION_HEIGHT - MARGIN * 6, TOP_SECTION_HEIGHT - MARGIN * 6 );
		
		Image img;
		try 
		{
			img = ImageIO.read( new FileInputStream( contact.avatar ) );
			Image newimg = img.getScaledInstance( TOP_SECTION_HEIGHT - MARGIN * 2, TOP_SECTION_HEIGHT - MARGIN *2,  java.awt.Image.SCALE_SMOOTH ) ;
			avatar.setIcon( new ImageIcon(newimg) );
		} 
		catch( FileNotFoundException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find '" + contact.avatar + "': ", ex );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find '" + contact.avatar + "': ", ex );
		}
		
		topSection.add( avatar );
	}
	
	
	private void createBottomPanel( )
	{
		bottomSection = new JPanel();
		bottomSection.setLayout( null );
		bottomSection.setBackground( ApplicationColors.DIALOG_BACKGROUND_COLOR );
		bottomSection.setBounds( BORDER_THICKNESS, TOP_SECTION_HEIGHT + BORDER_THICKNESS, MODAL_WIDTH - BORDER_THICKNESS * 2, BOTTOM_SECTION_HEIGHT );
		contentPanel.add( bottomSection );
	}
	
	private void createMainDetailsPanel( )
	{	
		mainDetailsSection = new JPanel();
		mainDetailsSection.setLayout( new BoxLayout(mainDetailsSection, BoxLayout.PAGE_AXIS ) );
		mainDetailsSection.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		mainDetailsSection.setBounds( MARGIN, MARGIN, MAIN_DETAILS_WIDTH, BOTTOM_SECTION_HEIGHT - MARGIN * 2 );
		bottomSection.add( mainDetailsSection );
		
		JLabel firstName = new JLabel( contact.firstName );
		firstName.setFont( new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE ) );
		firstName.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
		firstName.setPreferredSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		firstName.setSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		firstName.setMinimumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		firstName.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		mainDetailsSection.add( firstName );
		
		String fullLastName = ( !contact.isMasculine() && !contact.maidenName.isEmpty() ) ? contact.lastName + " (" + contact.maidenName + ")" : contact.lastName;
		JLabel lastName = new JLabel( fullLastName );
		lastName.setFont( new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE ) );
		lastName.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
		lastName.setPreferredSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		lastName.setSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		lastName.setMinimumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		lastName.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		mainDetailsSection.add( lastName );
		
		JLabel placeOfLiving = new JLabel( contact.placeOfLiving );
		placeOfLiving.setFont( new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE ) );
		placeOfLiving.setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
		placeOfLiving.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
		placeOfLiving.setPreferredSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		placeOfLiving.setSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		placeOfLiving.setMinimumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		placeOfLiving.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
		placeOfLiving.setHorizontalAlignment( SwingConstants.RIGHT );
		mainDetailsSection.add( placeOfLiving );
		
		if( !contact.dateOfBirth.isEmpty() || !contact.placeOfBirth.isEmpty() )
		{
			JLabel birthLabel = new JLabel( Config.getItem( ( contact.isMasculine() ) ? "born_m" : "born_f" ) );
			birthLabel.setFont( new Font( TEXT_FONT_NAME, Font.BOLD, TEXT_FONT_SIZE ) );
			birthLabel.setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
			birthLabel.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
			birthLabel.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
			mainDetailsSection.add( birthLabel );
			
			String fullBirthData = "";
			
			if( !contact.dateOfBirth.isEmpty() )
			{
				fullBirthData = contact.dateOfBirth;
				if( !contact.placeOfBirth.isEmpty() )
				{
					fullBirthData += ", " + contact.placeOfBirth;
				}
			}
			else
			{
				fullBirthData = contact.placeOfBirth;
			}
			
			JLabel birthDataLabel = new JLabel( fullBirthData );
			birthDataLabel.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
			birthDataLabel.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
			birthDataLabel.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
			mainDetailsSection.add( birthDataLabel );
		}
		
		if( contact.isDead && ( !contact.dateOfDeath.isEmpty() || !contact.placeOfDeath.isEmpty() ) )
		{
			JLabel birthLabel = new JLabel( Config.getItem( ( contact.isMasculine() ) ? "died_m" : "died_f" ) );
			birthLabel.setFont( new Font( TEXT_FONT_NAME, Font.BOLD, TEXT_FONT_SIZE ) );
			birthLabel.setForeground( ApplicationColors.FIELD_FOREGROUND_UNSELECTED_COLOR );
			birthLabel.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
			birthLabel.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
			mainDetailsSection.add( birthLabel );
			
			String fullDeathData = "";
			
			if( !contact.dateOfDeath.isEmpty() )
			{
				fullDeathData = contact.dateOfDeath;
				if( !contact.placeOfDeath.isEmpty() )
				{
					fullDeathData += ", " + contact.placeOfDeath;
				}
			}
			else
			{
				fullDeathData = contact.placeOfDeath;
			}
			
			JLabel birthDataLabel = new JLabel( fullDeathData );
			birthDataLabel.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
			birthDataLabel.setBorder( new EmptyBorder( 0, 8, 0, 8 ) );
			birthDataLabel.setMaximumSize( new Dimension( MAIN_DETAILS_WIDTH, FIELD_HEIGHT ) );
			mainDetailsSection.add( birthDataLabel );
		}
	}
	
	private void createLifeLinePanel( )
	{
		int width = MODAL_WIDTH - MAIN_DETAILS_WIDTH - MARGIN * 3;
		int height = BOTTOM_SECTION_HEIGHT / 2 - MARGIN;
		
		lifeLineSection = new JPanel();
		lifeLineSection.setLayout( null );
		lifeLineSection.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		lifeLineSection.setBounds( MARGIN * 2 + MAIN_DETAILS_WIDTH, MARGIN, width, height );
		bottomSection.add( lifeLineSection );
		
		JTextArea lifeline = new JTextArea( ( !contact.lifeline.isEmpty() ) ? contact.lifeline : Config.getItem( "default_lifetime" ) );
		lifeline.setWrapStyleWord( true );
		lifeline.setLineWrap( true );
		lifeline.setOpaque( false );
		lifeline.setEditable( false );
		lifeline.setFocusable( false );
		lifeline.setFont( new Font( TEXT_FONT_NAME, ( !contact.lifeline.isEmpty() ) ? Font.PLAIN : Font.ITALIC, TEXT_FONT_SIZE ) );
		lifeline.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		if( contact.lifeline.isEmpty() )
		{
			lifeline.setBorder( new EmptyBorder( 8, 8, 8, 8 ) );
		}
		
		JScrollPane scroll = new JScrollPane( lifeline );
	    scroll.setVerticalScrollBar( new HScrollBar( JScrollBar.VERTICAL ) );
	    scroll.setHorizontalScrollBar( new HScrollBar( JScrollBar.HORIZONTAL ) );
	    scroll.getViewport().setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
	    scroll.setBorder( null );
	    scroll.setBounds( 0, 0, width, height );
		
		lifeLineSection.add( scroll );
	}
	
	private void createNotesPanel( )
	{
		int width = MODAL_WIDTH - MAIN_DETAILS_WIDTH - MARGIN * 3;
		int height = BOTTOM_SECTION_HEIGHT / 2 - MARGIN * 2;
		
		notesSection = new JPanel();
		notesSection.setLayout( null );
		notesSection.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		notesSection.setBounds( MARGIN * 2 + MAIN_DETAILS_WIDTH, BOTTOM_SECTION_HEIGHT / 2 + MARGIN, width, height );
		bottomSection.add( notesSection );
		
		JTextArea notes = new JTextArea( ( !contact.notes.isEmpty() ) ? contact.notes : Config.getItem( "default_notes" ) );
		notes.setWrapStyleWord( true );
		notes.setLineWrap( true );
		notes.setOpaque( false );
		notes.setEditable( false );
		notes.setFocusable( false );
		notes.setFont( new Font( TEXT_FONT_NAME, ( !contact.notes.isEmpty() ) ? Font.PLAIN : Font.ITALIC, TEXT_FONT_SIZE ) );
		notes.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		if( contact.notes.isEmpty() )
		{
			notes.setBorder( new EmptyBorder( 8, 8, 8, 8 ) );
		}
		
		JScrollPane scroll = new JScrollPane( notes );
	    scroll.setVerticalScrollBar( new HScrollBar( JScrollBar.VERTICAL ) );
	    scroll.setHorizontalScrollBar( new HScrollBar( JScrollBar.HORIZONTAL ) );
	    scroll.getViewport().setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
	    scroll.setBorder( null );
	    scroll.setBounds( 0, 0, width, height );
		
	    notesSection.add( scroll );
	}

}
