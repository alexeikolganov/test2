package heritage.ui.modal;

import java.awt.Color;
import java.awt.Dimension;
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

import heritage.config.Config;
import heritage.controls.HDialog;
import heritage.controls.HToggleButton;
import heritage.controls.buttons.HButton;
import heritage.controls.buttons.HButtonDelete;
import heritage.controls.buttons.HButtonSubmit;
import heritage.controls.buttons.HMenuButton;
import heritage.controls.filechooser.HFileChooser;
import heritage.controls.inputs.HCheckBox;
import heritage.controls.inputs.HComboBox;
import heritage.controls.inputs.HTextArea;
import heritage.controls.inputs.HTextField;
import heritage.ui.TopPanel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class _Modal 
{
	private final int MODAL_WIDTH  			= Integer.parseInt( Config.getItem( "modal_width" ) );
	private final int MODAL_HEIGHT 			= Integer.parseInt( Config.getItem( "modal_height" ) );
	private final int FIELD_WIDTH  			= 320; // Integer.parseInt( Config.getItem( "field_width" ) ); // 240
	private final int FIELD_HEIGHT 			= Integer.parseInt( Config.getItem( "field_height" ) );
	private final int TEXTAREA_HEIGHT 		= Integer.parseInt( Config.getItem( "textarea_height" ) );
	private final int SYS_PANEL_HEIGHT		= Integer.parseInt( Config.getItem( "sys_panel_height" ) );
	private final int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );
	
	private final int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
		
	private final int LEFT_SECTION_WIDTH 	= 10 + 128 + 10;
	
	private final String TEXT_FONT_NAME		= Config.getItem( "app_font_name" );
	private final int TEXT_FONT_SIZE		= Integer.parseInt( Config.getItem( "text_font_fize" ) );
	private final String MAN_NO_AVATAR		= "icons/man_128.png";
	private final String WOMAN_NO_AVATAR	= "icons/woman_128.png";
	
	private final Color LEFT_SECTION_COLOR_MAN 		= new Color( 129, 218, 245 );
	private final Color LEFT_SECTION_COLOR_WOMAN 	= new Color( 245, 169, 188 );
	private final Color MAIN_SECTION_COLOR 			= Color.white;
	
	String[] months = { "-" + Config.getItem( "default_month" ) + "-", "������", "�������", "����", "������", "���", "����", "����", "������", "��������", "�������", "������", "�������" };
	
	private JPanel panel;
	
	// ����� ������
	private JPanel leftPanel;
	private JLabel avatarContainer;
	private JLabel accountName;
	private JLabel accountSurname;
	private JLabel accountMaidenName;
	
	private JLabel accountPlaceOfLiving;
	
	// �������� ������
	private JPanel mainPanel;
	private HTextField accountNameField;
	private HTextField accountSurnameField;
	private HTextField accountMaidenNameField;
	private HTextField accountPlaceOfLivingField;
	private HTextField accountNationalityField;
	
	private HTextField accountPlaceOfBirthField;
	private HTextField accountDayOfBirthField;
	private HTextField accountYearOfBirthField;
	private HComboBox accountMonthOfBirth;
	
	private HTextField accountPlaceOfDeathField;
	private HTextField accountDayOfDeathField;
	private HTextField accountYearOfDeathField;
	private HComboBox accountMonthOfDeath;
	
	private HToggleButton sex;
	private HCheckBox dead;
	private JLabel sexLabel;
	
	private HTextArea lifeTimeField;
	private HTextArea notesField;
			
	private static Logger log = Logger.getLogger(TopPanel.class.getName());
	
	public _Modal( String title )
	{
		HDialog dialog = new HDialog( new JFrame(), title, MODAL_WIDTH, MODAL_HEIGHT );	
		
		panel = (JPanel) dialog.getContentPane().getComponents()[1];
		
		panel.add( buildMainSection() );
		panel.add( buildLeftSection() );
						
		dialog.setVisible( true );
	}
	
	private JPanel buildLeftSection( )
	{		
		leftPanel = new JPanel( );
		leftPanel.setBounds( BORDER_THICKNESS, SYS_PANEL_HEIGHT, LEFT_SECTION_WIDTH, MODAL_HEIGHT - SYS_PANEL_HEIGHT - BORDER_THICKNESS );
		leftPanel.setBackground( LEFT_SECTION_COLOR_MAN );
		leftPanel.setLayout( null );
		
		buildLeftSectionFields( );
		
		return leftPanel;
	}
		
	/**
	 * ������� ������ � ���/������� ������������ � ����� ������
	 */
	private void buildLeftSectionFields( )
	{
		int yLeftPos 	 = 10;
		int avatarWidth  = 128;
		int avatarHeight = 128;
		int margin 		 = 10;
		
		// ������
		leftPanel.add( buildAvatar( margin, yLeftPos, avatarWidth, avatarHeight ) );	
		yLeftPos += avatarHeight + margin;
		
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
	
	/**
	 * ������� �������� ������
	 * @return �������� ������
	 */
	private JPanel buildMainSection( )
	{
		int width = MODAL_WIDTH - LEFT_SECTION_WIDTH - BORDER_THICKNESS * 2;

		mainPanel = new JPanel( );
		mainPanel.setBounds( LEFT_SECTION_WIDTH + BORDER_THICKNESS, SYS_PANEL_HEIGHT - BORDER_THICKNESS, width, MODAL_HEIGHT - SYS_PANEL_HEIGHT );
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
		
		int sexMainLabelWidth 	= 45; // ���
		int sexLabelWidth		= 20; // � / �
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
		buildSex( margin, yMainPos, sexMainLabelWidth, sexLabelWidth, togglerWidth );	
		buildMaidenNameField( sexMainLabelWidth + margin + sexLabelWidth + togglerWidth + margin, yMainPos, maidenNameWidth, FIELD_HEIGHT );				
		yMainPos += FIELD_HEIGHT + margin;
		
		// ��������������
		buildNationalityField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// ����� ����������
		buildaccountPlaceOfLivingField( margin, yMainPos );
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
	
	
	
	// ====================== ����� ����� ==============================
	
	/**
	 * ������� ������
	 * @return - ������, ���������� ������
	 */
	private JLabel buildAvatar( int x, int y, int w, int h )
	{
		ImageIcon image = new ImageIcon( !sex.isSelected() ? MAN_NO_AVATAR : WOMAN_NO_AVATAR );
		avatarContainer = new JLabel( "", image, JLabel.CENTER );
		avatarContainer.setBounds( x, y, w, h );
		
		return avatarContainer;
	}
		
	/**
	 * ������� ��� � ����� ������
	 * @return ���
	 */
	private JLabel buildName( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		accountName = buildLabel( x, y, w, h, accountNameField, Config.getItem( "default_name" ), font );
		return accountName;
	}
	
	/**
	 * ������� ������� � ����� ������
	 * @return �������
	 */
	private JLabel buildSurname( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		accountSurname = buildLabel( x, y, w, h, accountSurnameField, Config.getItem( "default_surname" ), font );
		return accountSurname;
	}
	
	private JLabel buildMaidenName( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		accountMaidenName = buildLabel( x, y, w, h, accountMaidenNameField, Config.getItem( "default_maiden_name" ), font );
		return accountMaidenName;
	}
	
	private JLabel buildPlaceOfLiving( int x, int y, int w, int h )
	{		
		Font font = new Font( TEXT_FONT_NAME, Font.ITALIC, TITLE_FONT_SIZE - 2 );
		accountPlaceOfLiving = buildLabel( x, y, w, h, accountPlaceOfLivingField, Config.getItem( "default_place_of_living" ), font );
		return accountPlaceOfLiving;
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
	 * ������� ���� "���"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildNameField( int x, int y, int w, int h )
	{
		accountNameField = new HTextField( Config.getItem( "default_name" ) );	
		accountNameField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                accountName.setText( textField.getText() );
            }
        });	
		mainPanel.add( ModalComponents.buildTextField( x, y, w, h, accountNameField, true ) );	
	}
	
	/**
	 * ������� ���� "�������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildSurnameField( int x, int y )
	{
		accountSurnameField = new HTextField( Config.getItem( "default_surname" ) );	
		accountSurnameField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                accountSurname.setText( textField.getText() );
            }
        });
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountSurnameField, true ) );
	}
	
	/**
	 * ������� ���� "����� ����������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildaccountPlaceOfLivingField( int x, int y )
	{
		accountPlaceOfLivingField = new HTextField( Config.getItem( "default_place_of_living" ) );	
		accountPlaceOfLivingField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                accountPlaceOfLiving.setText( textField.getText() );
            }
        });
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountPlaceOfLivingField, true ) );
	}
	
	private void buildNationalityField( int x, int y )
	{
		accountNationalityField = new HTextField( Config.getItem( "default_nationality" ) );	
		/*accountNationalityField.addKeyListener(new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                accountNationalityField.setText( textField.getText() );
            }
        });*/
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountNationalityField, true ) );
	}
	
	/**
	 * ������� ������������� "���"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 * @param labelMainWidth - ������ label
	 * @param labelWidth - ������ "�/�"
	 * @param togglerWidth - ������ �������������
	 */
	private void buildSex( int x, int y, int labelMainWidth, int labelWidth, int togglerWidth )
	{
		mainPanel.add( ModalComponents.buildLabel( x * 2, y, labelMainWidth, FIELD_HEIGHT, Config.getItem( "default_sex" ) ) );
		sexLabel = ModalComponents.buildLabel( labelMainWidth + x, y, labelWidth, FIELD_HEIGHT, Config.getItem( "default_masc" ) );
		mainPanel.add( sexLabel );
		sex = new HToggleButton();
		sex.setBounds( labelMainWidth + x + labelWidth, y, togglerWidth, FIELD_HEIGHT );
		sex.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				HToggleButton toggler = (HToggleButton)ev.getSource();
				
				String icon = "";
				if( toggler.isSelected() )
				{
					leftPanel.setBackground( LEFT_SECTION_COLOR_MAN );
					icon = MAN_NO_AVATAR;
					sexLabel.setText( Config.getItem( "default_masc" ) );			
				}
				else
				{
					leftPanel.setBackground( LEFT_SECTION_COLOR_WOMAN );
					icon = WOMAN_NO_AVATAR;
					sexLabel.setText( Config.getItem( "default_fem" ) );
				}
				
				accountMaidenName.setText( getMaidenName() );
				
				accountMaidenNameField.setVisible( !toggler.isSelected() );
				
				try 
				{
					Image img = ImageIO.read( new FileInputStream(icon) );
					avatarContainer.setIcon( new ImageIcon( img ) );
				} 
				catch( IOException ex ) 
				{
					log.log( Level.SEVERE, "Failed to find '" + icon + "': ", ex );
				}	
			}
			
		});				
		mainPanel.add( sex );
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
		accountMaidenNameField = new HTextField( Config.getItem( "default_maiden_name" ) );
		accountMaidenNameField.addKeyListener( new KeyAdapter() 
		{
            public void keyReleased(KeyEvent e) 
            {
                HTextField textField = (HTextField) e.getSource();
                accountMaidenName.setText( "(" + textField.getText() + ")" );
            }
        });	
		accountMaidenNameField.setVisible( sex.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( x, y, w, h, accountMaidenNameField, true ) );
	}
	
	/**
	 * ������� ���� "����� ��������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildPlaceOfBirthField( int x, int y )
	{
		accountPlaceOfBirthField = new HTextField( Config.getItem( "default_place_birth" ) );	
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountPlaceOfBirthField, true ) );
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
		
		accountDayOfBirthField = new HTextField( Config.getItem( "default_day" ) );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x, y, dayYearWidth, FIELD_HEIGHT, accountDayOfBirthField, true ) );

		accountMonthOfBirth = new HComboBox( months, 0 );
		mainPanel.add( ModalComponents.buildComboBox( labelWidth + x + dayYearWidth, y, monthWidth, FIELD_HEIGHT, accountMonthOfBirth, false ) );
		
		accountYearOfBirthField = new HTextField( Config.getItem( "default_year" ) );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + dayYearWidth + monthWidth, y, dayYearWidth, FIELD_HEIGHT, accountYearOfBirthField, false ) );
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
		dead.addItemListener(new ItemListener() 
		{
		    public void itemStateChanged( ItemEvent ev ) 
		    {
		    	HCheckBox checkbox = (HCheckBox)ev.getSource();
		    	accountDayOfDeathField.setVisible( checkbox.isSelected() );
		    	accountMonthOfDeath.setVisible( checkbox.isSelected() );
		    	accountYearOfDeathField.setVisible( checkbox.isSelected() );
		    	accountPlaceOfDeathField.setVisible( checkbox.isSelected() );
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
		accountDayOfDeathField = new HTextField( Config.getItem( "default_day" ) );
		accountDayOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + FIELD_HEIGHT, y, dayYearWidth, FIELD_HEIGHT, accountDayOfDeathField, true ) );

		accountMonthOfDeath = new HComboBox( months, 0 );
		accountMonthOfDeath.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildComboBox( labelWidth + x + dayYearWidth + FIELD_HEIGHT, y, monthWidth, FIELD_HEIGHT, accountMonthOfDeath, false ) );
		
		accountYearOfDeathField = new HTextField( Config.getItem( "default_year" ) );
		accountYearOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + dayYearWidth + monthWidth + FIELD_HEIGHT, y, dayYearWidth, FIELD_HEIGHT, accountYearOfDeathField, false ) );
	}
	
	/**
	 * ������� ���� "����� ������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildPlaceOfDeath( int x, int y )
	{
		accountPlaceOfDeathField = new HTextField( Config.getItem( "default_place_death" ) );
		accountPlaceOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountPlaceOfDeathField, true ) );
	}
	
	/**
	 * ������� ���� "�������������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildLifeTimeField( int x, int y )
	{
		lifeTimeField = new HTextArea( Config.getItem( "default_lifetime" ) );	
		mainPanel.add( ModalComponents.buildTextArea( x, y, FIELD_WIDTH, TEXTAREA_HEIGHT, lifeTimeField, true ) );	
	}
	
	/**
	 * ������� ���� "�������������"
	 * @param x - ���������� X
	 * @param y - ���������� Y
	 */
	private void buildNotesField( int x, int y )
	{
		notesField = new HTextArea( Config.getItem( "default_notes" ) );	
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
		mainPanel.add( apply );
		
		xPos -=  buttonW + margin;
		
		HButtonDelete cancel = new HButtonDelete( Config.getItem( "label_cancel" ) );
		cancel.setBounds( xPos, y, buttonW, buttonH );
		mainPanel.add( cancel );
		
		xPos -=  buttonW + margin;
						
		HButtonSubmit submit = new HButtonSubmit( Config.getItem( "label_save" ) );
		submit.setBounds( xPos, y, buttonW, buttonH );
		mainPanel.add( submit );
	}
	
	
	private String getMaidenName( )
	{
		String name = "";
		if( !sex.isSelected() )
		{
			name = ( !accountMaidenNameField.getText().equals( Config.getItem( "default_maiden_name" ) )) ? "(" + accountMaidenNameField.getText() + ")" : "";
		}
		return name;
	}
}
