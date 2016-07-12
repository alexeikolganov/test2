package heritage.contact;

import heritage.config.Config;
import heritage.ui.GalleryBlock;
import heritage.ui.block.Block;

import javax.swing.ImageIcon;

/**
 * Детали контакта в системе
 * @author Алексей Колганов
 *
 */
public class Contact 
{
	private static final int BLOCK_WIDTH  			= Block.BLOCK_WIDTH;
	private static final int BLOCK_HEIGHT 			= Block.BLOCK_HEIGHT;
	private static final int BLOCK_MAX_HEIGHT 		= Block.BLOCK_MAX_HEIGHT;
	private static final int MARGIN		 			= Integer.parseInt( Config.getItem( "block_margin" ) );
	
	private final String PHOTO_PATH 		= Config.getItem( "photos_path" ) + "/";
	private final String MAN_NO_AVATAR		= Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_man" );
	private final String WOMAN_NO_AVATAR	= Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_woman" );
	private final int AVATAR_W_H			= Integer.parseInt( Config.getItem( "avatar_w_h" ) ); 
	
	public int id 					= 0;
	public String firstName 		= "";
	public String lastName	 		= "";
	public String maidenName	 	= "";
	public boolean gender			= true; 
	public String dateOfBirth	 	= "";
	public String dateOfDeath	 	= "";
	public String placeOfBirth	 	= "";
	public String placeOfLiving		= "";
	public String placeOfDeath	 	= "";
	public String nationality	 	= "";
	public String status		 	= "";
	public String label		 		= ""; // используется для блоков "Добавить отца/мать"
	public boolean isDead			= false;
	public boolean isPrimary		= false;
	public String notes				= "";
	public String lifeline			= "";
	
	public String avatar			= "";
	
	public double x;
	public double y;
	public double lineY;
	public double lineX;
	public double leftLineX1;
	public double leftLineX2;
	public double rightLineX1;
	public double rightLineX2;
	public double topLineY1;
	public double topLineY2;
	public double bottomLineY1;
	public double bottomLineY2;
	
	public Contact( int id )
	{
		this.id = id;
	}
	
	public Contact( int id, 
					String firstName, 
					String lastName, 
					String maidenName, 
					boolean gender, 
					String nationality, 
					String dateOfBirth, 
					String dateOfDeath,
					String placeOfBirth,
					String placeOfLiving,
					String placeOfDeath,
					boolean isDead,
					String avatar,
					String notes,
					String lifeline
					)
	{
		this( id, firstName, lastName, maidenName, gender, nationality, dateOfBirth, dateOfDeath, placeOfBirth, placeOfLiving, placeOfDeath, isDead, avatar, notes, lifeline, "", false );
	}
	public Contact( int id, 
			String firstName, 
			String lastName, 
			String maidenName, 
			boolean gender, 
			String nationality, 
			String dateOfBirth, 
			String dateOfDeath,
			String placeOfBirth,
			String placeOfLiving,
			String placeOfDeath,
			boolean isDead,
			String avatar,
			String notes,
			String lifeline,
			String status
			)
	{
		this( id, firstName, lastName, maidenName, gender, nationality, dateOfBirth, dateOfDeath, placeOfBirth, placeOfLiving, placeOfDeath, isDead, avatar, notes, lifeline, status, false );
	}
	
	public Contact( int id, 
			String firstName, 
			String lastName, 
			String maidenName, 
			boolean gender, 
			String nationality, 
			String dateOfBirth, 
			String dateOfDeath,
			String placeOfBirth,
			String placeOfLiving,
			String placeOfDeath,
			boolean isDead,
			String avatar,
			String notes,
			String lifeline,
			String status,
			boolean isPrimary
			)
	{
		this.id 			= id;
		this.firstName 		= firstName;
		this.lastName 		= lastName;
		this.maidenName 	= maidenName;
		this.gender			= gender;
		this.nationality 	= nationality;
		this.dateOfBirth 	= dateOfBirth;
		this.dateOfDeath 	= dateOfDeath;
		this.placeOfBirth 	= placeOfBirth;
		this.placeOfLiving 	= placeOfLiving;
		this.placeOfDeath 	= placeOfDeath;		
		this.isDead 		= isDead;
		this.avatar			= avatar;
		this.notes			= notes;
		this.lifeline		= lifeline;
		this.status			= status;
		this.isPrimary		= isPrimary;
	}
	
	public Contact( String label, boolean gender )
	{
		this.id 	  = -1;
		this.label	  = label;
		this.gender	  = gender;
		this.avatar	  = gender ? MAN_NO_AVATAR : WOMAN_NO_AVATAR;
	}
	
	public Contact( int id, String label )
	{
		this.id 	  = id;
		this.label	  = label;
	}
	

	/*
	 * Пол (true - мужской, false - женский)
	 */
	public boolean isMasculine( )// getgender( )
	{
		return gender;
	}
	
	/**
	 * Получаем аватар
	 * Если новый контакт, получаем дефолтную иконку мужчины
	 * Иначе пытаемся найти выбранное фото (изменяем размер фото под размер поля аватара)
	 * Если фото не было загружено, 
	 * выбираем дефолтную картинку в зависимости от пола
	 * @return ImageIcon
	 */
	public ImageIcon getAvatar( )
	{
		String imagePath = this.avatar;
		
		if( this.avatar.isEmpty() )
		{
			imagePath = isMasculine( ) ? MAN_NO_AVATAR : WOMAN_NO_AVATAR;
		}
		else if( !this.avatar.equals( MAN_NO_AVATAR ) && !this.avatar.equals( WOMAN_NO_AVATAR ) )
		{
			imagePath = PHOTO_PATH + this.id + "/" + avatar;
		}
		
		ImageIcon image = new ImageIcon( imagePath );
		return GalleryBlock.getScaledIcon( image, AVATAR_W_H, AVATAR_W_H );
	}
	
	/**
	 * 						   topLineY1
	 * 				_______________|_______________
	 *              |	       topLineY2		  |
	 *              |							  |
	 * leftLineX1 --| leftLineX2      rightLineX1 |-- rightLineX2
	 *              |							  |
	 * 				|________bottomLineY1_________|
	 * 							   |
	 * 						 bottomLineY2
	 * @param x
	 * @param y
	 */
	public void setCoordinates( double x, double y )
	{
		this.x 			 	= x;
		this.y 			 	= y;
		this.lineX 		 	= this.x + BLOCK_WIDTH / 2;
		this.lineY 		 	= this.y + BLOCK_MAX_HEIGHT / 2;
		this.leftLineX1  	= this.x - MARGIN / 2;
		this.leftLineX2  	= this.x;
		this.rightLineX1 	= this.x + BLOCK_WIDTH;
		this.rightLineX2 	= this.x + BLOCK_WIDTH + MARGIN / 2;
		this.topLineY1   	= this.y - MARGIN / 2;
		this.topLineY2   	= this.y;
		this.bottomLineY1   = this.y + BLOCK_HEIGHT;
		this.bottomLineY2   = this.y + BLOCK_MAX_HEIGHT + MARGIN / 2;
	}
	
}
