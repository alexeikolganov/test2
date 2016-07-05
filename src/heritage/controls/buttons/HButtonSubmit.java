package heritage.controls.buttons;

import heritage.config.ApplicationColors;

/*
 * Видоизмененные кнопки для приложения (submit)
 */
public class HButtonSubmit extends HButton 
{
	private static final long serialVersionUID = 1L;

	public HButtonSubmit( )
	{
		super( 	ApplicationColors.BUTTON_SUBMIT_FOREGROUND_COLOR, 
				ApplicationColors.BUTTON_SUBMIT_BACKGROUND_COLOR, 
				ApplicationColors.BUTTON_SUBMIT_PRESSED_COLOR, 
				ApplicationColors.BUTTON_SUBMIT_HOVER_COLOR );
	}
	
	public HButtonSubmit( String text )
	{
		super( 	text, 
				ApplicationColors.BUTTON_SUBMIT_FOREGROUND_COLOR, 
				ApplicationColors.BUTTON_SUBMIT_BACKGROUND_COLOR, 
				ApplicationColors.BUTTON_SUBMIT_PRESSED_COLOR, 
				ApplicationColors.BUTTON_SUBMIT_HOVER_COLOR );
	}
}