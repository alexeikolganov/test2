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
	
	String[] months = { "-" + Config.getItem( "default_month" ) + "-", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
	
	private JPanel panel;
	
	// левая секция
	private JPanel leftPanel;
	private JLabel avatarContainer;
	private JLabel accountName;
	private JLabel accountSurname;
	private JLabel accountMaidenName;
	
	private JLabel accountPlaceOfLiving;
	
	// основная секция
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
	 * Создаем аватар и имя/фамилия пользователя в левой секции
	 */
	private void buildLeftSectionFields( )
	{
		int yLeftPos 	 = 10;
		int avatarWidth  = 128;
		int avatarHeight = 128;
		int margin 		 = 10;
		
		// аватар
		leftPanel.add( buildAvatar( margin, yLeftPos, avatarWidth, avatarHeight ) );	
		yLeftPos += avatarHeight + margin;
		
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
	
	/**
	 * Создаем основную секцию
	 * @return основная панель
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
	 * Создаем поля в основной секции
	 */
	private void buildFields( )
	{
		int panelWidth 			= MODAL_WIDTH - LEFT_SECTION_WIDTH - BORDER_THICKNESS * 2;
		
		int labelWidth 			= 100;
		int yMainPos			= 8;
		int margin 				= 8;
		
		int maidenNameWidth 	= 180;
		
		int sexMainLabelWidth 	= 45; // Пол
		int sexLabelWidth		= 20; // М / Ж
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
		buildSex( margin, yMainPos, sexMainLabelWidth, sexLabelWidth, togglerWidth );	
		buildMaidenNameField( sexMainLabelWidth + margin + sexLabelWidth + togglerWidth + margin, yMainPos, maidenNameWidth, FIELD_HEIGHT );				
		yMainPos += FIELD_HEIGHT + margin;
		
		// Национальность
		buildNationalityField( margin, yMainPos );
		yMainPos += FIELD_HEIGHT + margin;
		
		// Место жительства
		buildaccountPlaceOfLivingField( margin, yMainPos );
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
	
	
	
	// ====================== ЛЕВАЯ ЧАСТЬ ==============================
	
	/**
	 * Создаем аватар
	 * @return - панель, содержащая аватар
	 */
	private JLabel buildAvatar( int x, int y, int w, int h )
	{
		ImageIcon image = new ImageIcon( !sex.isSelected() ? MAN_NO_AVATAR : WOMAN_NO_AVATAR );
		avatarContainer = new JLabel( "", image, JLabel.CENTER );
		avatarContainer.setBounds( x, y, w, h );
		
		return avatarContainer;
	}
		
	/**
	 * Создаем имя в левой секции
	 * @return имя
	 */
	private JLabel buildName( int x, int y, int w, int h )
	{
		Font font = new Font( TEXT_FONT_NAME, Font.PLAIN, TITLE_FONT_SIZE );
		accountName = buildLabel( x, y, w, h, accountNameField, Config.getItem( "default_name" ), font );
		return accountName;
	}
	
	/**
	 * Создаем фамилию в левой секции
	 * @return фамилию
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
	
	// ====================== ОСНОВНАЯ ЧАСТЬ ==============================
	
	/**
	 * Создаем поле "Имя"
	 * @param x - координата X
	 * @param y - координата Y
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
	 * Создаем поле "Фамилия"
	 * @param x - координата X
	 * @param y - координата Y
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
	 * Создаем поле "Место жительства"
	 * @param x - координата X
	 * @param y - координата Y
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
	 * Создаем переключатель "Пол"
	 * @param x - координата X
	 * @param y - координата Y
	 * @param labelMainWidth - ширина label
	 * @param labelWidth - ширина "М/Ж"
	 * @param togglerWidth - ширина переключателя
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
	 * Создаем поле "Девичья фамилия", если Пол = Ж
	 * @param x - координата X
	 * @param y - координата Y
	 * @param w - ширина
	 * @param h - высота
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
	 * Создаем поле "Место Рождения"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildPlaceOfBirthField( int x, int y )
	{
		accountPlaceOfBirthField = new HTextField( Config.getItem( "default_place_birth" ) );	
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountPlaceOfBirthField, true ) );
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
		mainPanel.add( ModalComponents.buildLabel( x * 2, y, labelWidth, FIELD_HEIGHT, Config.getItem( "default_date_birth" ) ), true );
		
		accountDayOfBirthField = new HTextField( Config.getItem( "default_day" ) );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x, y, dayYearWidth, FIELD_HEIGHT, accountDayOfBirthField, true ) );

		accountMonthOfBirth = new HComboBox( months, 0 );
		mainPanel.add( ModalComponents.buildComboBox( labelWidth + x + dayYearWidth, y, monthWidth, FIELD_HEIGHT, accountMonthOfBirth, false ) );
		
		accountYearOfBirthField = new HTextField( Config.getItem( "default_year" ) );
		mainPanel.add( ModalComponents.buildTextField( labelWidth + x + dayYearWidth + monthWidth, y, dayYearWidth, FIELD_HEIGHT, accountYearOfBirthField, false ) );
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
	 * Создаем поля "Дата Смерти"
	 * @param x - координата X
	 * @param y - координата Y
	 * @param labelWidth - ширина label
	 * @param dayYearWidth - ширина полей "День" и "Год"
	 * @param monthWidth - ширина поля "Месяц"
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
	 * Создаем поле "Место Смерти"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildPlaceOfDeath( int x, int y )
	{
		accountPlaceOfDeathField = new HTextField( Config.getItem( "default_place_death" ) );
		accountPlaceOfDeathField.setVisible( dead.isSelected() );
		mainPanel.add( ModalComponents.buildTextField( x, y, FIELD_WIDTH, FIELD_HEIGHT, accountPlaceOfDeathField, true ) );
	}
	
	/**
	 * Создаем поле "Жизнеописание"
	 * @param x - координата X
	 * @param y - координата Y
	 */
	private void buildLifeTimeField( int x, int y )
	{
		lifeTimeField = new HTextArea( Config.getItem( "default_lifetime" ) );	
		mainPanel.add( ModalComponents.buildTextArea( x, y, FIELD_WIDTH, TEXTAREA_HEIGHT, lifeTimeField, true ) );	
	}
	
	/**
	 * Создаем поле "Жизнеописание"
	 * @param x - координата X
	 * @param y - координата Y
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
