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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.HToggleButton;
import heritage.controls.buttons.HButtonDelete;
import heritage.controls.buttons.HButtonSubmit;
import heritage.controls.inputs.HCheckBox;
import heritage.controls.inputs.HComboBox;
import heritage.controls.inputs.HTextArea;
import heritage.controls.inputs.HTextField;

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
	
	String[] months = { "-" + Config.getItem( "default_month" ) + "-", "������", "�������", "����", "������", "���", "����", "����", "������", "��������", "�������", "������", "�������" };
	
	// ����� ������
	private JLabel firstName;
	private JLabel surname;
	private JLabel maidenName;
	private JLabel placeOfLiving;
	
	// �������� ������
	private JPanel mainPanel;
	private HTextField firstNameField;
	private HTextField surnameField;
	private HTextField maidenNameField;
	private HTextField placeOfLivingField;
	private HTextField nationalityField;
	
	private HTextField placeOfBirthField;
	private HTextField dayOfBirthField;
	private HTextField yearOfBirthField;
	private HComboBox monthOfBirth;
	
	private HTextField placeOfDeathField;
	private HTextField dayOfDeathField;
	private HTextField yearOfDeathField;
	private HComboBox monthOfDeath;
	
	private HToggleButton gender;
	private HCheckBox dead;
	private JLabel genderLabel;
	
	private HTextArea lifeTimeField;
	private HTextArea notesField;
	
	private Contact contact;
	private Contact relatedContact;
	
	private static Logger log = Logger.getLogger( ModalEdit.class.getName() );
	
	public ModalEdit( String title, Contact contact ) 
	{
		super( title, contact );
		this.contact = contact;

		getRightSection( ).add( buildMainSection() );
		buildLeftSectionFields( );
		
		getDialog().setVisible( true );
		
	}
	
	public ModalEdit( String title, Contact contact, Contact relatedContact ) 
	{
		super( title, contact );
		this.contact = contact;
		this.relatedContact = relatedContact;

		getRightSection( ).add( buildMainSection() );
		buildLeftSectionFields( );
		
		getDialog().setVisible( true );	
	}
	
	/**
	 * ������� �������� ������
	 * @return �������� ������
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
	 * ������� ���� � �������� ������
	 */
	private void buildFields( )
	{
		int panelWidth 			= MODAL_WIDTH - LEFT_SECTION_WIDTH - BORDER_THICKNESS * 2;
		
		int labelWidth 			= 100;
		int yMainPos			= 8;
		int margin 				= 8;
		
		int maidenNameWidth 	= 180;
		
		int genderMainLabelWidth 	= 45; // ���
		int genderLabelWidth		= 20; // � / �
		int togglerWidth		= 64;
		
		int dayYearWidth 		= 60;
		int monthWidth 			= 100;
		
		int deadLabelWidth 		= 68;
		
		int avatarWH			= 32;
		
		// ���
		buildNameField( margin, yMainPos, FIELD_WIDTH - margin - avatarWH, FIELD_HEIGHT );
		
		// ������
		buildFileChooser( FIELD_WIDTH - avatarWH + margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// �������
		buildSurnameField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
				
		// ��� (+ ������� �������, ���� ��� �������)	
		buildGender( margin, yMainPos, genderMainLabelWidth, genderLabelWidth, togglerWidth );	
		buildMaidenNameField( genderMainLabelWidth + margin + genderLabelWidth + togglerWidth + margin, yMainPos, maidenNameWidth, FIELD_HEIGHT );				
		yMainPos += FIELD_HEIGHT + margin;
		
		// ��������������
		buildNationalityField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// ����� ����������
		buildplaceOfLivingField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// ����� ��������
		buildPlaceOfBirthField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// ���� ��������
		buildDateOfBirth( margin, yMainPos, labelWidth, dayYearWidth, monthWidth );
		yMainPos += FIELD_HEIGHT + margin;
		
		// ����(��)?
		buildIsDeadCheckbox( margin, yMainPos, deadLabelWidth );
		
		// ���� ������
		buildDateOfDeath( margin, yMainPos, deadLabelWidth, dayYearWidth, monthWidth );
		yMainPos += FIELD_HEIGHT + margin;
		
		// ����� ������
		buildPlaceOfDeath( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// �������������
		buildLifeTimeField( margin, yMainPos );
		yMainPos += TEXTAREA_HEIGHT + margin;
		
		// �������
		buildNotesField( margin, yMainPos );
		yMainPos += TEXTAREA_HEIGHT + margin;
		
		// ������
		buildButtons( panelWidth, yMainPos );
	}
	
	/**
	 * ������� ������ � ���/������� ������������ � ����� ������
	 */
	private void buildLeftSectionFields( )
	{
		int yLeftPos 	 = 10;
		int avatarWidth  = 128;
		int margin 		 = 10;
		
		JPanel leftPanel = getInfoSection();
		// ���
		leftPanel.add( buildName( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );
		yLeftPos += TEXT_FONT_SIZE + margin + margin;
		
		// �������
		leftPanel.add( buildSurname( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );	
		yLeftPos += TEXT_FONT_SIZE + margin + margin;
		
		// ������� �������
		leftPanel.add( buildMaidenName( margin, yLeftPos, avatarWidth, TEXT_FONT_SIZE + margin ) );	
		yLeftPos += TEXT_FONT_SIZE + margin + margin;
		
		// ����� ����������
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
	
	// ====================== �������� ����� ==============================
	
	/**
	 * ������� ��� � ����� ������
	 * @return ���
	 */
	private JLabel buildName( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		firstName = buildLabel( x, y, w, h, firstNameField, Config.getItem( "default_name" ), font );
		return firstName;
	}
	
	/**
	 * ������� ������� � ����� ������
	 * @return �������
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
	 * ������� ���� "���"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
	 * ������� ���� "�������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
	 * ������� ���� "����� ����������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
	 * ������� ������������� "���"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 * @param labelMainWidth - ������ label
	 * @param labelWidth - ������ "�/�"
	 * @param togglerWidth - ������ �������������
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
	 * ������� ���� "������� �������", ���� ��� = �
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 * @param w - ������
	 * @param h - ������
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
	 * ������� ���� "����� ��������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
	 * ������� ���� "���� ��������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 * @param labelWidth - ������ label
	 * @param dayYearWidth - ������ ����� "����" � "���"
	 * @param monthWidth - ������ ���� "�����"
	 */
	private void buildDateOfBirth( int x, int y, int labelWidth, int dayYearWidth, int monthWidth )
	{
		mainPanel.add( ModalComponents.buildLabel( x * 2, y, labelWidth, FIELD_HEIGHT, Config.getItem( "default_date_birth" ) ), true );
		if( contact.dateOfBirth.isEmpty() )
		{
			dayOfBirthField = new HTextField( Config.getItem( "default_day" ) );	
			monthOfBirth = new HComboBox( months, 0 );
			yearOfBirthField = new HTextField( Config.getItem( "default_year" ) );
		}
		else
		{
			String[] date = contact.dateOfBirth.split( "/" );
			dayOfBirthField = new HTextField( Config.getItem( "default_day" ), date[0] );	
			monthOfBirth = new HComboBox( months, Integer.parseInt( date[1] ) );
			yearOfBirthField = new HTextField( Config.getItem( "default_year" ), date[2] );
		}
			
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x, y, dayYearWidth, FIELD_HEIGHT, dayOfBirthField, true ) );
		mainPanel.add( ModalComponents.buildComboBox( labelWidth + x + dayYearWidth, y, monthWidth, FIELD_HEIGHT, monthOfBirth, false ) );	
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + dayYearWidth + monthWidth, y, dayYearWidth, FIELD_HEIGHT, yearOfBirthField, false ) );
	}
	
	/**
	 * ������� ������������� "����(��?)"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 * @param labelWidth - ������ label
	 */
	private void buildIsDeadCheckbox( int x, int y, int labelWidth )
	{
		mainPanel.add( ModalComponents.buildLabel( x * 2, y, labelWidth, FIELD_HEIGHT, Config.getItem( "default_dead" ) ) );
		
		dead = ModalComponents.buildCheckBox( labelWidth + x, y, FIELD_HEIGHT, FIELD_HEIGHT );
		dead.setSelected( contact.isDead );
		dead.addItemListener(new ItemListener() 
		{
		    public void itemStateChanged( ItemEvent ev ) 
		    {
		    	HCheckBox checkbox = (HCheckBox)ev.getSource();
		    	dayOfDeathField.setVisible( checkbox.isSelected() );
		    	monthOfDeath.setVisible( checkbox.isSelected() );
		    	yearOfDeathField.setVisible( checkbox.isSelected() );
		    	placeOfDeathField.setVisible( checkbox.isSelected() );
		    }
		});
		mainPanel.add( dead );
	}
	
	/**
	 * ������� ���� "���� ������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 * @param labelWidth - ������ label
	 * @param dayYearWidth - ������ ����� "����" � "���"
	 * @param monthWidth - ������ ���� "�����"
	 */
	private void buildDateOfDeath( int x, int y, int labelWidth, int dayYearWidth, int monthWidth )
	{
		if( contact.dateOfDeath.isEmpty() )
		{
			dayOfDeathField = new HTextField( Config.getItem( "default_day" ) );	
			monthOfDeath = new HComboBox( months, 0 );
			yearOfDeathField = new HTextField( Config.getItem( "default_year" ) );
		}
		else
		{
			String[] date = contact.dateOfDeath.split( "/" );
			dayOfDeathField = new HTextField( Config.getItem( "default_day" ), date[0] );	
			monthOfDeath = new HComboBox( months, Integer.parseInt( date[1] ) );
			yearOfDeathField = new HTextField( Config.getItem( "default_year" ), date[2] );
		}
		dayOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + FIELD_HEIGHT, y, dayYearWidth, FIELD_HEIGHT, dayOfDeathField, true ) );

		monthOfDeath.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildComboBox( labelWidth + x + dayYearWidth + FIELD_HEIGHT, y, monthWidth, FIELD_HEIGHT, monthOfDeath, false ) );
		
		yearOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + dayYearWidth + monthWidth + FIELD_HEIGHT, y, dayYearWidth, FIELD_HEIGHT, yearOfDeathField, false ) );
	}
	
	/**
	 * ������� ���� "����� ������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
	 * ������� ���� "�������������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
	 * ������� ���� "�������������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
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
					System.out.println( "insert" );
				}
				else
				{
					System.out.println( "update" );
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
					Object[] options = { "��", "���" };
					int dialogResult = JOptionPane.showOptionDialog(null ,
												"�������, �� �������� ��������� ���������?",
												"��������",
											    JOptionPane.YES_NO_OPTION,
											    JOptionPane.QUESTION_MESSAGE,
											    null,     //do not use a custom Icon
											    options,  //the titles of buttons
											    options[0]); //default button title
					
					if( dialogResult == JOptionPane.YES_OPTION )
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
	 * �������, ���������� �� ��������
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
		
		String dob = dayOfBirthField.getText().replaceAll( Config.getItem( "default_day" ), "" ) + "/" + monthOfBirth.getSelectedIndex() + "/" + yearOfBirthField.getText().replaceAll( Config.getItem( "default_year" ), "" );
		System.out.println( "8. " + ( !dob.equals( "/0/" ) && !contact.dateOfBirth.equals( dob ) ) );
		if( !dob.equals( "/0/" ) && !contact.dateOfBirth.equals( dob ) )
		{
			return true;
		}

		String dod = dayOfDeathField.getText().replaceAll( Config.getItem( "default_day" ), "" ) + "/" + monthOfDeath.getSelectedIndex() + "/" + yearOfDeathField.getText().replaceAll( Config.getItem( "default_year" ), "" );
		System.out.println( "9. " + ( !dod.equals( "/0/" ) && !contact.dateOfDeath.equals( dob ) ) );
		if( !dod.equals( "/0/" ) && !contact.dateOfDeath.equals( dod ) )
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
		return false;
	}
}
