package heritage.ui.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.contact.ContactRelationship;
import heritage.controls.HScrollBar;
import heritage.controls.buttons.HMenuButton;
import heritage.listener.ClickListener;
import heritage.relationship.Relation;
import heritage.relationship.RelationshipPanel;
import heritage.ui.UnlinkedPanel;
import heritage.ui.modal.ModalEdit;

/**
 * Блок "Добавить <тип_контакта>"
 * @author Home
 *
 */
public class AddContactBlock extends JLayeredPane
{
	private static final long serialVersionUID = 2946765925019144703L;
	private static final int BLOCK_WIDTH  			= Block.BLOCK_WIDTH;
	private static final int BLOCK_HEIGHT 			= Block.BLOCK_HEIGHT;

	private final static int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
	private final static String TEXT_FONT_NAME 		= Config.getItem( "app_font_name" );
	
	private final static Font LARGE_FONT 			= new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE );
	
	private Timer timer;
	
	private JPanel mainPanel;
	private JPanel menuPanel;
	private JPanel listPanel;
	private ContactRelationship[] reln;

	/**
	 * Конструктор, состоящий из:
	 * 1. Основной панели, содержащей текст "Добавить <тип_контакта>"
	 * 2. Выезжающего меню при наведении на блок, в свою очередь состоящего из:
	 * 2.а. Меню "Добавить новый контакт"
	 * 2.б. Меню "Выбрать существующий контакт"
	 * @param contact
	 */
	public AddContactBlock( final Contact contact, final ContactRelationship[] reln )
	{
		this.reln 	 = reln;
		
		// устанавливаем основные параметры
		setLocation( (int)contact.x, (int)contact.y );
		setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT ) );
		setBorder( BorderFactory.createDashedBorder( null ) );
		addMouseListener( createClickListener() );
		
		// 1. Основная панель, содержащая текст "Добавить <тип_контакта>"
		mainPanel = new JPanel();
		mainPanel.setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT ) );
		mainPanel.setOpaque( false );
		add( mainPanel, 1, 1 );
		
		JLabel addLbl = new JLabel( contact.label );
		addLbl.setHorizontalAlignment( SwingConstants.CENTER );
		addLbl.setFont( LARGE_FONT );
		mainPanel.add( addLbl );
		
		// 2. Выезжающее меню при наведении на блок
		menuPanel = new JPanel();
		menuPanel.setSize( new Dimension( BLOCK_WIDTH, 0 ) );
		menuPanel.setLayout( null );
		add( menuPanel, 2, 1 );
		
		listPanel = new JPanel();
		listPanel.setLocation( (int)contact.leftLineX2, (int)contact.bottomLineY1 );
		listPanel.setSize( BLOCK_WIDTH, 0 );
		listPanel.setLayout( null );
		RelationshipPanel.innerPanel.add( listPanel, 10, 10 );
		
		final JPanel innerPanel = new JPanel( );
		innerPanel.setBackground( ApplicationColors.UNLINKED_PANEL_BACKGROUND_COLOR );
		innerPanel.setLayout( new BoxLayout( innerPanel, BoxLayout.PAGE_AXIS ) );
		//innerPanel.setBorder( BorderFactory.createMatteBorder( 0, 0, 0, 2, ApplicationColors.FIELD_BORDER_FOCUSED ) );

		JScrollPane scrollFrame = new JScrollPane( innerPanel );
        scrollFrame.setBorder( null );
        scrollFrame.setPreferredSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT * 3 ) );
        scrollFrame.setVerticalScrollBar( new HScrollBar( JScrollBar.VERTICAL ) );
        scrollFrame.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scrollFrame.setBounds( 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT * 3 );
        listPanel.add( scrollFrame );
		
		// 2.а. Меню "Добавить новый контакт"
		final String BUTTON_LABEL = Config.getItem( "new_contact_label" );
		HMenuButton newContactButton = new HMenuButton( BUTTON_LABEL, "icons/plus.png" );
		newContactButton.setBounds( 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT / 2 );		
		newContactButton.addMouseListener( new ClickListener()  
		{  	
			public void mouseEntered( MouseEvent e )
            {
				 timer.stop();
            }
			
			public void mouseExited( MouseEvent e )
            {
				 timer.restart();			 
            }
            public void singleClick( MouseEvent e )
            {
            	timer.stop();
				Contact temp = new Contact( "", contact.gender );
				new ModalEdit( BUTTON_LABEL, temp, reln );	
            }	
		});
		menuPanel.add( newContactButton );
		
		// 2.б. Меню "Выбрать существующий контакт"
		HMenuButton existingContactButton = new HMenuButton( Config.getItem( "existing_contact" ), "icons/link.png" );
		existingContactButton.setBounds( 0, BLOCK_HEIGHT / 2, BLOCK_WIDTH, BLOCK_HEIGHT / 2 );		
		existingContactButton.addMouseListener( new ClickListener()  
		{  	
			public void mouseEntered( MouseEvent e )
            {
				 timer.stop();
            }
			
			public void mouseExited( MouseEvent e )
            {
				 timer.restart();
				
            }
            public void singleClick( MouseEvent e )
            {
				if( listPanel.getSize().height == 0 )
				{
					RelationshipPanel.innerPanel.repaint();
					RelationshipPanel.innerPanel.revalidate();
							
			        int i=0;
			        List<Contact> contacts = Relation.getUnlinkedContacts();
					for( Contact listedContact : contacts )
					{
						// if a parent being added, we need to list just correct genders:
						// women for mother
						// men for father
						if( reln[0].lookupLevel == -1 && listedContact.gender != contact.gender )
						{
							continue;
						}
						Block block = new Block( listedContact, false );
						block.setBorder( BorderFactory.createMatteBorder( 0, 0, 2, 0, ApplicationColors.FIELD_BORDER_FOCUSED ) );
						block.removeMouseListener( block.click );
						block.addMouseListener( createListClickListener( listedContact ) );
						
						innerPanel.add( block );
						i++;					
						// adjust list height if there are less than 3 results
						listPanel.setSize( BLOCK_WIDTH, BLOCK_HEIGHT * ( ( i<3 ) ? i : 3 ) );
					}
				}
				else
				{
					listPanel.setSize( BLOCK_WIDTH, 0 );
				}
			}	
		});
		menuPanel.add( existingContactButton );
	}	
	
	public ClickListener createListClickListener( final Contact listedContact )
	{
		return new ClickListener()  
		{  	
			public void mouseEntered( MouseEvent e )
            {
				Color background = ( listedContact.isMasculine()) ? ApplicationColors.MAN_COLOR_HOVER : ApplicationColors.WOMAN_COLOR_HOVER;
				final Block block = (Block)e.getComponent();
		    	block.setBackground( background );
            }
			
			public void mouseExited( MouseEvent e )
            {
				Color background = (listedContact.isMasculine()) ? ApplicationColors.MAN_COLOR : ApplicationColors.WOMAN_COLOR;
				final Block block = (Block)e.getComponent();
		    	block.setBackground( background );	
            }
			            
            public void singleClick( MouseEvent e )
            {
            	for( ContactRelationship relationship : reln )
        		{
        			relationship.subjectContactId = listedContact.id;
        			Relation.insertRelationship( relationship );
        		}
            	RelationshipPanel.drawTree( );
				UnlinkedPanel.drawUnlinkedContacts( );
            }	
		};
	}
	
	private ClickListener createClickListener()
	{
		return new ClickListener()  
		{  	
			public void mouseEntered( MouseEvent e )
            {
				final AddContactBlock block = (AddContactBlock)e.getComponent();
				timer = new Timer( 10, new ActionListener() 
		        {
					public void actionPerformed( ActionEvent e ) 
					{
						// expand
						block.menuPanel.setSize( BLOCK_WIDTH , block.menuPanel.getHeight() + 5 );
		                if( block.menuPanel.getHeight() >= BLOCK_HEIGHT ) 
		                {
		                	((Timer) e.getSource()).stop( );    
		                	block.menuPanel.setSize( BLOCK_WIDTH, BLOCK_HEIGHT );
		                }
					}
		         });
				 timer.setInitialDelay( 250 );
				 timer.start();
            }
			
			public void mouseExited( MouseEvent e )
            {
				final AddContactBlock block = (AddContactBlock)e.getComponent();
		        timer = new Timer( 1, new ActionListener() 
				{
					public void actionPerformed( ActionEvent e ) 
					{
						// collapse
						block.menuPanel.setSize( BLOCK_WIDTH , block.menuPanel.getHeight() - 20 );
		                if( block.menuPanel.getHeight() <= 0 ) 
		                {
		                	((Timer) e.getSource()).stop( );    
		                	block.menuPanel.setSize( BLOCK_WIDTH, 0 );
		                }
		                if( listPanel.getSize().height > 0 )
						{
			                listPanel.setSize( BLOCK_WIDTH, 0 );
							RelationshipPanel.innerPanel.repaint();
							RelationshipPanel.innerPanel.revalidate();
						}
					}
				 });
		         timer.setInitialDelay( 2000 );
				 timer.start();			 
            }
		};
	}
	
}
