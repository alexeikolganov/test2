package heritage.ui.modal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import heritage.config.Config;
import heritage.contact.Contact;
import heritage.contact.ContactRelationship;
import heritage.controls.HOptionPane;
import heritage.controls.HToggleButton;
import heritage.controls.buttons.HButtonDelete;
import heritage.controls.buttons.HButtonSubmit;
import heritage.controls.inputs.DatePicker;
import heritage.controls.inputs.HCheckBox;
import heritage.controls.inputs.HTextArea;
import heritage.controls.inputs.HTextField;
import heritage.relationship.Relation;

public class ModalEdit extends Modal
{	
	private final int MODAL_WIDTH  			= Integer.parseInt( Config.getItem( "modal_width" ) );
	private final int MODAL_HEIGHT 			= Integer.parseInt( Config.getItem( "modal_height" ) );
	private final int FIELD_WIDTH  			= 320; // Integer.parseInt( Config.getItem( "field_width" ) ); // 240
	private final int FIELD_HEIGHT 			= Integer.parseInt( Config.getItem( "field_height" ) );
	private final int TEXTAREA_HEIGHT 		= Integer.parseInt( Config.getItem( "textarea_height" ) );
	private final int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );
	
	private final int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
		
	private final int LEFT_SECTION_WIDTH 	= 10 + 128 + 10;
	
	private final String TEXT_FONT_NAME		= Config.getItem( "app_font_name" );
	private final int TEXT_FONT_SIZE		= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final String MAN_NO_AVATAR		= "icons/man_128.png";
	private final String WOMAN_NO_AVATAR	= "icons/woman_128.png";
	
	private final Color LEFT_SECTION_COLOR_MAN 		= new Color( 129, 218, 245 );
	private final Color LEFT_SECTION_COLOR_WOMAN 	= new Color( 245, 169, 188 );
	private final Color MAIN_SECTION_COLOR 			= Color.white;
	
	String[] months = { "-" + Config.getItem( "default_month" ) + "-", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
	
	// левая секция
	private JLabel firstName;
	private JLabel surname;
	private JLabel maidenName;
	private JLabel placeOfLiving;
	
	// основная секция
	private JPanel mainPanel;
	private HTextField firstNameField;
	private HTextField surnameField;
	private HTextField maidenNameField;
	private HTextField placeOfLivingField;
	private HTextField nationalityField;
	
	private HTextField placeOfBirthField;
	private DatePicker dateOfBirth;
	
	private HTextField placeOfDeathField;
	private DatePicker dateOfDeath;
	
	private HToggleButton gender;
	private HCheckBox dead;
	private JLabel genderLabel;
	
	private HTextArea lifeTimeField;
	private HTextArea notesField;
	
	private Contact contact;
	private ContactRelationship reln;
	
	private static Logger log = Logger.getLogger( ModalEdit.class.getName() );
	
	public ModalEdit( String title, Contact contact ) 
	{
		super( title, contact );
		this.contact = contact;

		getRightSection( ).add( buildMainSection() );
		buildLeftSectionFields( );
		
		getDialog().setVisible( true );
		
	}
	
	public ModalEdit( String title, Contact contact, ContactRelationship reln ) 
	{
		super( title, contact );
		this.contact = contact;
		this.reln 	 = reln;

		getRightSection( ).add( buildMainSection() );
		buildLeftSectionFields( );
		
		getDialog().setVisible( true );	
	}
	
	/**
	 * Создаем основную секцию
	 * @return основная панель
	 */
	private JPanel buildMainSection( )
	{
		mainPanel = new JPanel( );
		mainPanel.setBounds( 0, 0, MODAL_WIDTH, MODAL_HEIGHT );
		mainPanel.setBackground( MAIN_SECTION_COLOR );
		mainPanel.setLayout( null );
		
		buildFields( );
		
		return mainPanel;
	}

	/**
	 * Создаем поля в основной секции
	 */
	private void buildFields( )
	{
		int panelWidth 			= MODAL_WIDTH - LEFT_SECTION_WIDTH - BORDER_THICKNESS * 2;
		
		int labelWidth 			= 100;
		int yMainPos			= 8;
		int margin 				= 8;
		
		int maidenNameWidth 	= 180;
		
		int genderMainLabelWidth 	= 45; // Пол
		int genderLabelWidth		= 20; // М / Ж
		int togglerWidth		= 64;
		
		int dayYearWidth 		= 60;
		int monthWidth 			= 100;
		
		int deadLabelWidth 		= 68;
		
		int avatarWH			= 32;
		
		// Имя
		buildNameField( margin, yMainPos, FIELD_WIDTH - margin - avatarWH, FIELD_HEIGHT );
		
		// Аватар
		buildFileChooser( FIELD_WIDTH - avatarWH + margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Фамилия
		buildSurnameField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
				
		// Пол (+ девичья фамилия, если пол женский)	
		buildGender( margin, yMainPos, genderMainLabelWidth, genderLabelWidth, togglerWidth );	
		buildMaidenNameField( genderMainLabelWidth + margin + genderLabelWidth + togglerWidth + margin, yMainPos, maidenNameWidth, FIELD_HEIGHT );				
		yMainPos += FIELD_HEIGHT + margin;
		
		// Национальность
		buildNationalityField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Место жительства
		buildplaceOfLivingField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Место Рождения
		buildPlaceOfBirthField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Дата Рождения
		buildDateOfBirth( margin, yMainPos, labelWidth, dayYearWidth, monthWidth );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Умер(ла)?
		buildIsDeadCheckbox( margin, yMainPos, deadLabelWidth );
		
		// Дата Смерти
		buildDateOfDeath( margin, yMainPos, deadLabelWidth, dayYearWidth, monthWidth );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Место смерти
		buildPlaceOfDeath( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Жизнеописание
		buildLifeTimeField( margin, yMainPos );
		yMainPos += TEXTAREA_HEIGHT + margin;
		
		// Заметки
		buildNotesField( margin, yMainPos );
		yMainPos += TEXTAREA_HEIGHT + margin;
		
		// Кнопки
		buildButtons( panelWidth, yMainPos );
	}
	
	/**
	 * Создаем аватар и имя/фамилия пользователя в левой секции
	 */
	private void buildLeftSectionFields( )
	{
		int yLeftPos 	 = 10;
		int avatarWidth  = 128;
		int margin 		 = 10;
		
		JPanel leftPanel = getInfoSection();
		// имя
		leftPanel.add( buildName( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );
		yLeftPos += TEXT_FONT_SIZE + margin + margin;
		
		// фамилия
		leftPanel.add( buildSurname( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );	
		yLeftPos += TEXT_FONT_SIZE + margin + margin;
		
		// девичья фамилия
		leftPanel.add( buildMaidenName( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );	
		yLeftPos += TEXT_FONT_SIZE + margin + margin;
		
		// место жительства
		leftPanel.add( buildPlaceOfLiving( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );	
		yLeftPos += TEXT_FONT_SIZE + margin + margin;

	}
	private JLabel buildPlaceOfLiving( int x, int y, int w, int h )
	{		
		Font font = new Font( TEXT_FONT_NAME, Font.ITALIC, TITLE_FONT_SIZE - 2 );
		placeOfLiving = buildLabel( x, y, w, h, placeOfLivingField, Config.getItem( "default_place_of_living" ), font );
		return placeOfLiving;
	}
	
	private JLabel buildLabel( int x, int y, int w, int h, HTextField mainSectionField, String defaultText, Font font )
	{
		String value = ( !mainSectionField.getText().equals( defaultText )) ? mainSectionField.getText() : "";
		
		JLabel label = new JLabel( value );
		label.setBounds( x, y, w, h );
		label.setFont( font );
		
		return label;		
	}
	
	// ====================== ОСНОВНАЯ ЧАСТЬ ==============================
	
	/**
	 * Создаем имя в левой секции
	 * @return имя
	 */
	private JLabel buildName( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		firstName = buildLabel( x, y, w, h, firstNameField, Config.getItem( "default_name" ), font );
		return firstName;
	}
	
	/**
	 * Создаем фамилию в левой секции
	 * @return фамилию
	 */
	private JLabel buildSurname( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		surname = buildLabel( x, y, w, h, surnameField, Config.getItem( "default_surname" ), font );
		return surname;
	}
	
	private JLabel buildMaidenName( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		maidenName = buildLabel( x, y, w, h, maidenNameField, Config.getItem( "default_maiden_name" ), font );
		return maidenName;
	}
	
	/**
	 * Создаем поле "Имя"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildNameField( int x, int y, int w, int h )
	{		
		if( contact.firstName.isEmpty() )
		{
			firstNameField = new HTextField( Config.getItem( "default_name" ) );
		}
		else
		{
			firstNameField = new HTextField( Config.getItem( "default_name" ), contact.firstName );
		}
		firstNameField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                firstName.setText( textField.getText() );
            }
        });	
		mainPanel.add( ModalComponents.buildTextField( x, y, w, h, firstNameField, true ) );	
	}
	
	/**
	 * Создаем поле "Фамилия"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildSurnameField( int x, int y )
	{
		if( contact.lastName.isEmpty() )
		{
			surnameField = new HTextField( Config.getItem( "default_surname" ) );
		}
		else
		{
			surnameField = new HTextField( Config.getItem( "default_surname" ), contact.lastName );
		}
		surnameField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                surname.setText( textField.getText() );
            }
        });
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, surnameField, true ) );
	}
	
	/**
	 * Создаем поле "Место жительства"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildplaceOfLivingField( int x, int y )
	{
		if( contact.placeOfLiving.isEmpty() )
		{
			placeOfLivingField = new HTextField( Config.getItem( "default_place_of_living" ) );	
		}
		else
		{
			placeOfLivingField = new HTextField( Config.getItem( "default_place_of_living" ), contact.placeOfLiving );
		}
		placeOfLivingField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                placeOfLiving.setText( textField.getText() );
            }
        });
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, placeOfLivingField, true ) );
	}
	
	private void buildNationalityField( int x, int y )
	{
		if( contact.nationality.isEmpty() )
		{
			nationalityField = new HTextField( Config.getItem( "default_nationality" ) );	
		}
		else
		{
			nationalityField = new HTextField( Config.getItem( "default_nationality" ), contact.nationality );
		}
		/*nationalityField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                nationalityField.setText( textField.getText() );
            }
        });*/
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, nationalityField, true ) );
	}
	
	/**
	 * Создаем переключатель "Пол"
	 * @param x - координата X
	 * @param y - координата Y
	 * @param labelMainWidth - ширина label
	 * @param labelWidth - ширина "М/Ж"
	 * @param togglerWidth - ширина переключателя
	 */
	private void buildGender( int x, int y, int labelMainWidth, int labelWidth, int togglerWidth )
	{
		JLabel lbl = ModalComponents.buildLabel( x * 2, y, labelMainWidth, FIELD_HEIGHT, Config.getItem( "default_gender" ) );
		mainPanel.add( lbl );
		genderLabel = ModalComponents.buildLabel( labelMainWidth + x, y, labelWidth, FIELD_HEIGHT, Config.getItem( (contact.isMasculine()) ? "default_masc" : "default_fem" ) );

		mainPanel.add( genderLabel );
		gender = new HToggleButton();
		gender.setSelected( !contact.isMasculine() );
		gender.setBounds( labelMainWidth + x + labelWidth, y, togglerWidth, FIELD_HEIGHT );
		gender.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				HToggleButton toggler = (HToggleButton)ev.getSource();
				
				String icon = "";
				if( toggler.isSelected() )
				{
					leftPanel.setBackground( LEFT_SECTION_COLOR_MAN );
					icon = MAN_NO_AVATAR;
					genderLabel.setText( Config.getItem( "default_masc" ) );			
				}
				else
				{
					leftPanel.setBackground( LEFT_SECTION_COLOR_WOMAN );
					icon = WOMAN_NO_AVATAR;
					genderLabel.setText( Config.getItem( "default_fem" ) );
				}
				
				maidenName.setText( getMaidenName() );
				
				maidenNameField.setVisible( !toggler.isSelected() );
				
				try 
				{
					Image img = ImageIO.read( new FileInputStream(icon) );
					getAvatar().setIcon( new ImageIcon( img ) );
				} 
				catch( IOException ex ) 
				{
					log.log( Level.SEVERE, "Failed to find '" + icon + "': ", ex );
				}	
			}
			
		});				
		mainPanel.add( gender );
	}
		
	/**
	 * Создаем поле "Девичья фамилия", если Пол = Ж
	 * @param x - координата X
	 * @param y - координата Y
	 * @param w - ширина
	 * @param h - высота
	 */
	private void buildMaidenNameField( int x, int y, int w, int h )
	{
		if( contact.maidenName.isEmpty() )
		{
			maidenNameField = new HTextField( Config.getItem( "default_maiden_name" ) );	
		}
		else
		{
			maidenNameField = new HTextField( Config.getItem( "default_maiden_name" ), contact.maidenName );
		}
		maidenNameField.addKeyListener( new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                maidenName.setText( "(" + textField.getText() + ")" );
            }
        });	
		maidenNameField.setVisible( gender.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( x, y, w, h, maidenNameField, true ) );
	}
	
	/**
	 * Создаем поле "Место Рождения"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildPlaceOfBirthField( int x, int y )
	{
		if( contact.placeOfBirth.isEmpty() )
		{
			placeOfBirthField = new HTextField( Config.getItem( "default_place_birth" ) );	
		}
		else
		{
			placeOfBirthField = new HTextField( Config.getItem( "default_place_birth" ), contact.placeOfBirth );
		}
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, placeOfBirthField, true ) );
	}
	
	/**
	 * Создаем поля "Дата Рождения"
	 * @param x - координата X
	 * @param y - координата Y
	 * @param labelWidth - ширина label
	 * @param dayYearWidth - ширина полей "День" и "Год"
	 * @param monthWidth - ширина поля "Месяц"
	 */
	private void buildDateOfBirth( int x, int y, int labelWidth, int dayYearWidth, int monthWidth )
	{
		dateOfBirth = new DatePicker( Config.getItem( "default_date_birth" ), contact.dateOfBirth );		
		mainPanel.add( ModalComponents.buildDatePicker( x, y, FIELD_WIDTH, FIELD_HEIGHT, dateOfBirth, true ) );
	}
	
	/**
	 * Создаем переключатель "Умер(ла?)"
	 * @param x - координата X
	 * @param y - координата Y
	 * @param labelWidth - ширина label
	 */
	private void buildIsDeadCheckbox( int x, int y, int labelWidth )
	{
		mainPanel.add( ModalComponents.buildLabel( x * 2, y, labelWidth, FIELD_HEIGHT, Config.getItem( "default_dead" ) ) );
		
		dead = ModalComponents.buildCheckBox( labelWidth + x, y, FIELD_HEIGHT, FIELD_HEIGHT );
		System.out.println(contact.isDead);
		dead.setSelected( contact.isDead );
		dead.addItemListener(new ItemListener() 
		{
		    public void itemStateChanged( ItemEvent ev ) 
		    {
		    	HCheckBox checkbox = (HCheckBox)ev.getSource();
		    	dateOfDeath.setVisible( checkbox.isSelected() );
		    	placeOfDeathField.setVisible( checkbox.isSelected() );
		    }
		});
		mainPanel.add( dead );
	}
	
	/**
	 * Создаем поля "Дата Смерти"
	 * @param x - координата X
	 * @param y - координата Y
	 * @param labelWidth - ширина label
	 * @param dayYearWidth - ширина полей "День" и "Год"
	 * @param monthWidth - ширина поля "Месяц"
	 */
	private void buildDateOfDeath( int x, int y, int labelWidth, int dayYearWidth, int monthWidth )
	{
		dateOfDeath = new DatePicker( Config.getItem( "default_date_death" ), contact.dateOfDeath );		
		dateOfDeath.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildDatePicker( labelWidth + x + FIELD_HEIGHT, y, FIELD_WIDTH - (labelWidth + FIELD_HEIGHT), FIELD_HEIGHT, dateOfDeath, true ) );
	}
	
	/**
	 * Создаем поле "Место Смерти"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildPlaceOfDeath( int x, int y )
	{
		if( contact.placeOfBirth.isEmpty() )
		{
			placeOfDeathField = new HTextField( Config.getItem( "default_place_death" ) );	
		}
		else
		{
			placeOfDeathField = new HTextField( Config.getItem( "default_place_death" ), contact.placeOfDeath );
		}
		placeOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, placeOfDeathField, true ) );
	}
	
	/**
	 * Создаем поле "Жизнеописание"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildLifeTimeField( int x, int y )
	{
		if( contact.lifeline.isEmpty() )
		{
			lifeTimeField = new HTextArea( Config.getItem( "default_lifetime" ) );	
		}
		else
		{
			lifeTimeField = new HTextArea( Config.getItem( "default_lifetime" ), contact.lifeline );
		}
		mainPanel.add( ModalComponents.buildTextArea( x, y, FIELD_WIDTH, TEXTAREA_HEIGHT, lifeTimeField, true ) );	
	}
	
	/**
	 * Создаем поле "Жизнеописание"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildNotesField( int x, int y )
	{
		if( contact.notes.isEmpty() )
		{
			notesField = new HTextArea( Config.getItem( "default_notes" ) );	
		}
		else
		{
			notesField = new HTextArea( Config.getItem( "default_notes" ), contact.notes );
		}
		mainPanel.add( ModalComponents.buildTextArea( x, y, FIELD_WIDTH, TEXTAREA_HEIGHT, notesField, true ) );	
	}
	
	private void buildFileChooser( int x, int y )
	{
		mainPanel.add( ModalComponents.buildFileChooser( x, y, "add_acontact_32.png" ) );	
	}
	
	private void buildButtons( int w, int y )
	{
		int buttonW = 80;
		int buttonH = 24;
		int margin  = 10;
		int marginRight = 20;
		
		int xPos = w - buttonW - marginRight;
		
		HButtonSubmit apply = new HButtonSubmit( Config.getItem( "label_apply" ) );
		apply.setBounds( xPos, y, buttonW, buttonH );
		apply.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				if( contact.id == -1 )
				{
					populateContactDetails( );				
					Relation.insertContact( contact );				
				}
				else
				{
					populateContactDetails( );				
					Relation.updateContact( contact );	
				}
			}
			
		});
		mainPanel.add( apply );
		
		xPos -=  buttonW + margin;
		
		HButtonDelete cancel = new HButtonDelete( Config.getItem( "label_cancel" ) );
		cancel.setBounds( xPos, y, buttonW, buttonH );
		cancel.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				if( fieldsChanged() )
				{
					HOptionPane pane = new HOptionPane( null, "Warning", "Are you sure you want to close the window? All the changes will be lost", HOptionPane.OptionType.OK_CANCEL );
					if(pane.status == HOptionPane.Status.OK )
					{
						dialog.dispose();
					}
				}
				else
				{
					dialog.dispose();
				}
			}
			
		});
		mainPanel.add( cancel );
		
		xPos -= buttonW + margin;
						
		HButtonSubmit submit = new HButtonSubmit( Config.getItem( "label_save" ) );
		submit.setBounds( xPos, y, buttonW, buttonH );
		mainPanel.add( submit );
	}
	
	
	private String getMaidenName( )
	{
		String name = "";
		if( !gender.isSelected() )
		{
			name = ( !maidenNameField.getText().equals( Config.getItem( "default_maiden_name" ) )) ? "(" + maidenNameField.getText() + ")" : "";
		}
		return name;
	}
		
	/**
	 * Сверяем, изменились ли значения
	 * @return
	 */
	private boolean fieldsChanged( )
	{
		System.out.println( "1. " + !contact.firstName.equals( firstNameField.getText().replaceAll( Config.getItem( "default_name" ), "" ) ) );
		if( !contact.firstName.equals( firstNameField.getText().replaceAll( Config.getItem( "default_name" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "2. " + !contact.lastName.equals( surnameField.getText().replaceAll( Config.getItem( "default_surname" ), "" ) ) );
		if( !contact.lastName.equals( surnameField.getText().replaceAll( Config.getItem( "default_surname" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "3. " + !contact.maidenName.equals( maidenNameField.getText().replaceAll( Config.getItem( "default_maiden_name" ), "" ) ) );
		if( !contact.maidenName.equals( maidenNameField.getText().replaceAll( Config.getItem( "default_maiden_name" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "4. " + !contact.placeOfLiving.equals( placeOfLivingField.getText().replaceAll( Config.getItem( "default_place_of_living" ), "" ) ) );
		if( !contact.placeOfLiving.equals( placeOfLivingField.getText().replaceAll( Config.getItem( "default_place_of_living" ), "" ) ) )
		{
			return true;
		}
		System.out.println("5. " + !contact.placeOfBirth.equals( placeOfBirthField.getText().replaceAll( Config.getItem( "default_place_birth" ), "" ) ) );
		if( !contact.placeOfBirth.equals( placeOfBirthField.getText().replaceAll( Config.getItem( "default_place_birth" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "6. " + !contact.placeOfDeath.equals( placeOfDeathField.getText().replaceAll( Config.getItem( "default_place_death" ), "" ) ) );
		if( !contact.placeOfDeath.equals( placeOfDeathField.getText().replaceAll( Config.getItem( "default_place_death" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "7. " + !contact.nationality.equals( nationalityField.getText().replaceAll( Config.getItem( "default_nationality" ), "" ) ) );
		if( !contact.nationality.equals( nationalityField.getText().replaceAll( Config.getItem( "default_nationality" ), "" ) ) )
		{
			return true;
		}
		
		System.out.println( "8. " + !contact.dateOfBirth.equals( dateOfBirth.getText().replaceAll( Config.getItem( "default_date_birth" ), "" ) ) );
		if( !contact.dateOfBirth.equals( dateOfBirth.getText().replaceAll( Config.getItem( "default_date_birth" ), "" ) ) )
		{
			return true;
		}

		System.out.println( "9. " + !contact.dateOfDeath.equals( dateOfDeath.getText().replaceAll( Config.getItem( "default_date_death" ), "" ) ) );
		if( !contact.dateOfDeath.equals( dateOfDeath.getText().replaceAll( Config.getItem( "default_date_death" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "10. " + ( contact.gender == gender.isSelected()));
		if( contact.gender == gender.isSelected() )
		{
			return true;
		}
		System.out.println( "11. " + (contact.isDead != dead.isSelected()));
		if( contact.isDead != dead.isSelected() )
		{
			return true;
		}
		System.out.println( "12. " + !contact.lifeline.equals( lifeTimeField.getText().replaceAll( Config.getItem( "default_lifetime" ), "" ) ) );
		if( !contact.lifeline.equals( lifeTimeField.getText().replaceAll( Config.getItem( "default_lifetime" ), "" ) ) )
		{
			return true;
		}
		System.out.println( "13. " + !contact.notes.equals( notesField.getText().replaceAll( Config.getItem( "default_notes" ), "" ) ) );
		if( !contact.notes.equals( notesField.getText().replaceAll( Config.getItem( "default_notes" ), "" ) ) )
		{
			return true;
		}
		return false;
	}
	
	private void populateContactDetails( )
	{
		contact.firstName 		= firstNameField.getText().replaceAll( Config.getItem( "default_name" ), "" );
		contact.lastName  		= surnameField.getText().replaceAll( Config.getItem( "default_surname" ), "" );
		contact.gender    		= !gender.isSelected();
		contact.maidenName 		= maidenNameField.getText().replaceAll( Config.getItem( "default_maiden_name" ), "" );
		contact.nationality 	= nationalityField.getText().replaceAll( Config.getItem( "default_nationality" ), "" );
		contact.placeOfLiving 	= placeOfLivingField.getText().replaceAll( Config.getItem( "default_place_of_living" ), "" );
		contact.placeOfBirth 	= placeOfBirthField.getText().replaceAll( Config.getItem( "default_place_birth" ), "" );
		contact.dateOfBirth 	= dateOfBirth.getText().replaceAll( Config.getItem( "default_date_birth" ), "" );
		contact.dateOfDeath 	= dateOfDeath.getText().replaceAll( Config.getItem( "default_name" ), "" );
		contact.placeOfDeath 	= placeOfDeathField.getText().replaceAll( Config.getItem( "default_place_death" ), "" );
		contact.isDead 			= dead.isSelected();
		contact.lifeline 		= lifeTimeField.getText().replaceAll( Config.getItem( "default_lifetime" ), "" );
		contact.notes 			= notesField.getText().replaceAll( Config.getItem( "default_notes" ), "" );
		
		contact.avatar			= "";
	}
}
