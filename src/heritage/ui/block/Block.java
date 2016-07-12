package heritage.ui.block;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.HOptionPane;
import heritage.controls.buttons.HMenuButton;
import heritage.listener.ClickListener;
import heritage.relationship.Relation;
import heritage.relationship.RelationshipPanel;
import heritage.ui.modal.ModalEdit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Ѕлоковое отображение человека
 * @author Home
 *
 */
public class Block extends JPanel
{
	private static final long serialVersionUID 		= -137686843148695848L;
	
	private static Color blockColor;
	private static boolean personIsDead  			= false;
	private static boolean personIsSelected			= false;
	
	public final static int BORDER_THICKNESS		= 4;
	public static final int BLOCK_WIDTH  			= Integer.parseInt( Config.getItem( "block_width" ) );
	public static final int BLOCK_HEIGHT 			= Integer.parseInt( Config.getItem( "block_height" ) );
	public static final int BLOCK_BUTTON_SIZE  		= Integer.parseInt( Config.getItem( "block_button_size" ) );
	public static final int BLOCK_MAX_HEIGHT 		= BLOCK_HEIGHT + BLOCK_BUTTON_SIZE + BORDER_THICKNESS * 2;//Integer.parseInt( Config.getItem( "block_max_height" ) );

		
	private final static int TEXT_FONT_SIZE	 		= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final static int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
	private final static String TEXT_FONT_NAME 		= Config.getItem( "app_font_name" );
	
	private final static Font LARGE_FONT 			= new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE );
	private final static Font MIDDLE_FONT 			= new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE );
	private final static Font MIDDLE_ITALIC_FONT 	= new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE );
	private final static String DECEASED_ICON		= "icons/deceased.png";
	
	private final static Color BORDER_COLOR			= new Color( 150, 150, 150 );
	
	private Timer timer;
	
	private Contact contact;
	
	private static Logger log = Logger.getLogger( Block.class.getName() );
		
	/**
	 * ќсновной метод, сто€щий блок
	 * @param contact - объект "контакт"
	 * @param isSelected - выбран ли этот контакт
	 * @return - JPanel блок
	 */
	public Block( final Contact contact, boolean isSelected )
	{ 
		this.contact 	 = contact;
		blockColor 		 = ( contact.isMasculine() ) ? ApplicationColors.MAN_COLOR : ApplicationColors.WOMAN_COLOR;
		personIsDead 	 = contact.isDead;
		personIsSelected = isSelected;
		
		setLocation( (int)contact.x, (int)contact.y );
		setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT ) );
		setLayout( null );
		setBackground( blockColor );
		
		if( personIsSelected )
		{
			setBorder( BorderFactory.createLineBorder( BORDER_COLOR, BORDER_THICKNESS ) );
		}				
		add( setAvatar( ) );
		add( setDetails( ) );
		add( setExtendedDetails() );
		
		addMouseListener( new ClickListener()  
		{  	
			public void mouseEntered( MouseEvent e )
            {
				final Block block = (Block)e.getComponent();
		    	timer = new Timer( 10, new ActionListener() 
		        {
					public void actionPerformed( ActionEvent e ) 
					{
						// expand
						block.setSize( BLOCK_WIDTH , block.getHeight() + 5 );
		                if( block.getHeight() >= BLOCK_MAX_HEIGHT ) 
		                {
		                	((Timer) e.getSource()).stop( );    
		                	block.setSize( BLOCK_WIDTH , BLOCK_MAX_HEIGHT );
		                }
					}
		         });
				 timer.setInitialDelay( 250 );
				 timer.start();
            }
			
			public void mouseExited( MouseEvent e )
            {
				final Block block = (Block)e.getComponent();
		        timer = new Timer( 1, new ActionListener() 
				{
					public void actionPerformed( ActionEvent e ) 
					{
						// collapse
						block.setSize( BLOCK_WIDTH , block.getHeight() - 20 );
						block.getParent().getParent().revalidate();
						block.getParent().getParent().repaint();
				        if( block.getHeight() <= BLOCK_HEIGHT ) 
				        {
				        	((Timer) e.getSource()).stop( );
				        	block.setSize( BLOCK_WIDTH , BLOCK_HEIGHT );
				        } 
					}
				 });
		         timer.setInitialDelay( 2000 );
				 timer.start();			 
            }
			
            public void doubleClick( MouseEvent e )
            {
            	new ModalEdit( contact.firstName + " " + contact.lastName, contact );
            }	
            
            public void singleClick( MouseEvent e )
            {
				timer.stop();
				RelationshipPanel.setSelectedContactId( contact.id );
				RelationshipPanel.drawTree( );
            }	
		});
	}
	
	/**
	 * ƒобавл€ем аватар
	 * ≈сли человека нет в живых, добавл€м траурную ленту на аватаре
	 * @param avatar - название файла
	 */
	private JLayeredPane setAvatar( )
	{
		int avatarMargin 		= 4;
		int avatarDoubleMargin 	= BLOCK_HEIGHT - avatarMargin * 2;
		int avatarLayerId 		= 2;
		int avatarDeadLayerId 	= 3;
		
		JLayeredPane avatarPane = new JLayeredPane();
		avatarPane.setBounds( avatarMargin, avatarMargin, avatarDoubleMargin, avatarDoubleMargin );
		
		ImageIcon icon = new ImageIcon( ( (new ImageIcon( contact.avatar ) ).getImage() ).getScaledInstance( avatarDoubleMargin, 
																									avatarDoubleMargin, 
																									java.awt.Image.SCALE_SMOOTH ) );
		JLabel lbl = new JLabel( icon );
		lbl.setBounds( 0, 0, avatarDoubleMargin, avatarDoubleMargin );
		avatarPane.add( lbl, avatarLayerId, 0 );
		
		if( personIsDead )
		{
			ImageIcon deceasedIcon = new ImageIcon( ( (new ImageIcon( DECEASED_ICON ) ).getImage() ).getScaledInstance( avatarDoubleMargin, 
																														avatarDoubleMargin, 
																														java.awt.Image.SCALE_SMOOTH ));
			JLabel deceasedLbl = new JLabel( deceasedIcon );
			deceasedLbl.setBounds( 0, 0, avatarDoubleMargin, avatarDoubleMargin );
			avatarPane.add( deceasedLbl, avatarDeadLayerId, 0 );
		}
		
		return avatarPane;
	}
	
	/**
	 * ѕишем детали
	 * @param details - массив деталей пользовател€ ( аватар, им€, фамили€, место жительства, дата рождени€, родственный статус относительно выбранного контакта )
	 */
	private JPanel setDetails( )
	{
		int textWidth 	= BLOCK_WIDTH - BLOCK_HEIGHT - BORDER_THICKNESS;
		int yPos 		= 1;
		int margin 		= 0;
		
		JPanel detailPanel = new JPanel( );
		detailPanel.setBounds( BLOCK_HEIGHT, BORDER_THICKNESS, textWidth, BLOCK_HEIGHT - BORDER_THICKNESS * 2 );
		detailPanel.setBackground( blockColor );
		detailPanel.setLayout( null );

		int rowHeight = (int) ( BLOCK_HEIGHT - BORDER_THICKNESS * 2 ) / 4;
				
		// им€
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.firstName, LARGE_FONT ) );
		yPos += rowHeight;
		
		// фамили€
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.lastName, LARGE_FONT ) ); 
		yPos += rowHeight;
		
		// место жительства
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.placeOfLiving, MIDDLE_FONT ) ); 
		yPos += rowHeight;
		
		// дата рождени€
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.dateOfBirth, MIDDLE_FONT ) );
		
		// статус
		if( !personIsSelected )
		{
			JLabel lbl = createLabel( textWidth / 2 , yPos, textWidth / 2, rowHeight, contact.status, MIDDLE_ITALIC_FONT );
			lbl.setHorizontalAlignment( SwingConstants.RIGHT );
			detailPanel.add( lbl );
		}		
		return detailPanel;
	}
	
	private JPanel setExtendedDetails()
	{
		JPanel extendedDetails = new JPanel();
		extendedDetails.setBounds( BORDER_THICKNESS, BLOCK_HEIGHT, BLOCK_WIDTH - BORDER_THICKNESS * 2, BLOCK_MAX_HEIGHT - BLOCK_HEIGHT - BORDER_THICKNESS );
		extendedDetails.setLayout( null );
		extendedDetails.setBackground( ApplicationColors.BUTTON_MENU_BACKGROUND_COLOR );
		
		int x = BLOCK_WIDTH - BORDER_THICKNESS * 2 - ( BLOCK_BUTTON_SIZE + 4 ) * 5;
		
		// read-only details
		HMenuButton viewButton = new HMenuButton( );
		viewButton.setBounds( x, 0, BLOCK_BUTTON_SIZE + 4, BLOCK_BUTTON_SIZE + 4 );
		try 
		{
			Image img = ImageIO.read( new FileInputStream("icons/info.png") );
			Image newimg = img.getScaledInstance( BLOCK_BUTTON_SIZE, BLOCK_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			viewButton.setIcon( new ImageIcon(newimg) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find 'info.png': ", ex );
		}
		extendedDetails.add( viewButton );
		
		x += BLOCK_BUTTON_SIZE + 4;
		
		// edit details
		HMenuButton editButton = new HMenuButton( );
		editButton.setBounds( x, 0, BLOCK_BUTTON_SIZE + 4, BLOCK_BUTTON_SIZE + 4 );
		try 
		{
			Image img = ImageIO.read( new FileInputStream("icons/edit.png") );
			Image newimg = img.getScaledInstance( BLOCK_BUTTON_SIZE, BLOCK_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			editButton.setIcon( new ImageIcon(newimg) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find 'edit.png': ", ex );
		}
		editButton.addMouseListener( new ClickListener()  
		{  				
            public void singleClick( MouseEvent e )
            {
            	new ModalEdit( contact.firstName + " " + contact.lastName, contact );
            }			
		});
		extendedDetails.add( editButton );
		
		x += BLOCK_BUTTON_SIZE + 4;
		
		// delete contact
		HMenuButton deleteButton = new HMenuButton( );
		deleteButton.setBounds( x, 0, BLOCK_BUTTON_SIZE + 4, BLOCK_BUTTON_SIZE + 4 );
		try 
		{
			Image img = ImageIO.read( new FileInputStream("icons/delete.png") );
			Image newimg = img.getScaledInstance( BLOCK_BUTTON_SIZE, BLOCK_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			deleteButton.setIcon( new ImageIcon(newimg) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find 'delete.png': ", ex );
		}
		deleteButton.addMouseListener( new ClickListener()  
		{  				
            public void singleClick( MouseEvent e )
            {
            	HOptionPane pane = new HOptionPane( null, Config.getItem( "warning" ), Config.getItem( "warning_delete_contact" ), HOptionPane.OptionType.OK_CANCEL );
				if( pane.status == HOptionPane.Status.OK )
				{
					Relation.deleteContact( contact );
					RelationshipPanel.drawTree( );
				}
            }			
		});
		extendedDetails.add( deleteButton );
		
		x += BLOCK_BUTTON_SIZE + 4;
		
		// unlink contact
		HMenuButton unlinkButton = new HMenuButton( );
		unlinkButton.setBounds( x, 0, BLOCK_BUTTON_SIZE + 4, BLOCK_BUTTON_SIZE + 4 );
		try 
		{
			Image img = ImageIO.read( new FileInputStream("icons/unlink.png") );
			Image newimg = img.getScaledInstance( BLOCK_BUTTON_SIZE, BLOCK_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			unlinkButton.setIcon( new ImageIcon(newimg) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find 'unlink.png': ", ex );
		}
		unlinkButton.addMouseListener( new ClickListener()  
		{  				
			public void singleClick( MouseEvent e )
            {
            	System.out.println(timer.isRunning());
            	timer.stop();
            	System.out.println(timer.isRunning());
				HOptionPane pane = new HOptionPane( null, Config.getItem( "warning" ), Config.getItem( "warning_delete_relns" ), HOptionPane.OptionType.OK_CANCEL, 300, 160 );
				if( pane.status == HOptionPane.Status.OK )
				{				
					Relation.deleteRelationship( contact );
					RelationshipPanel.drawTree( );			
				}
				timer.restart();
            }			
		});
		extendedDetails.add( unlinkButton );
		
		x += BLOCK_BUTTON_SIZE + 4;
		
		HMenuButton pinButton = new HMenuButton( );
		pinButton.setBounds( x, 0, BLOCK_BUTTON_SIZE + 4, BLOCK_BUTTON_SIZE + 4 );
		try 
		{
			Image img = ImageIO.read( new FileInputStream("icons/pin.png") );
			Image newimg = img.getScaledInstance( BLOCK_BUTTON_SIZE, BLOCK_BUTTON_SIZE,  java.awt.Image.SCALE_SMOOTH ) ;
			pinButton.setIcon( new ImageIcon(newimg) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find 'pin.png': ", ex );
		}
		pinButton.setEnabled( !contact.isPrimary );
		if( pinButton.isEnabled() )
		{
			pinButton.addMouseListener( new ClickListener()  
			{  				
	            public void singleClick( MouseEvent e )
	            {
					timer.stop();
					RelationshipPanel.setSelectedContactId( contact.id );
	            	Relation.setPrimaryContact( contact );	
					RelationshipPanel.drawTree( );
	            }			
			});
		}
		extendedDetails.add( pinButton );
		
		return extendedDetails;
	}
	
	/**
	 * —оздаем текст
	 * @param x - координата X
	 * @param y - координата Y
	 * @param w - ширина
	 * @param h - высота
	 * @param text - текст
	 * @param font - шрифт
	 * @return JLabel
	 */
	private static JLabel createLabel( int x, int y, int w, int h, String text, Font font )
	{
		JLabel field = new JLabel( text );
		field.setBounds( x, y, w, h );
		field.setFont( font );
		return field;
	}
}
