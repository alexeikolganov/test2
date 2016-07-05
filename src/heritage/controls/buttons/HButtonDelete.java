package heritage.controls.buttons;

import heritage.config.ApplicationColors;

/*
 * Видоизмененные кнопки для приложения
 */
public class HButtonDelete extends HButton 
{
	private static final long serialVersionUID = 1L;

	public HButtonDelete( )
	{
		super( 	ApplicationColors.BUTTON_DELETE_FOREGROUND_COLOR, 
				ApplicationColors.BUTTON_DELETE_BACKGROUND_COLOR, 
				ApplicationColors.BUTTON_DELETE_PRESSED_COLOR, 
				ApplicationColors.BUTTON_DELETE_HOVER_COLOR );
	}
	
	public HButtonDelete( String text )
	{
		super( 	text, 
				ApplicationColors.BUTTON_DELETE_FOREGROUND_COLOR, 
				ApplicationColors.BUTTON_DELETE_BACKGROUND_COLOR, 
				ApplicationColors.BUTTON_DELETE_PRESSED_COLOR, 
				ApplicationColors.BUTTON_DELETE_HOVER_COLOR );
	}
}
