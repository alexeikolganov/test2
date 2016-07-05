package heritage.controls.filechooser;

import heritage.config.Config;
import heritage.controls.HTitleBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.FileChooserUI;

/**
 * Кастомный диалог для выбора файла
 * @author Home
 *
 */
public class HFileChooser extends JFileChooser
{
	private static final long serialVersionUID = -5818929824610406061L;
	private final int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );

	public HFileChooser( )
	{
		super();
		
		setUI( new HFileChooserUI( this ) );
		setBorder( BorderFactory.createLineBorder( Color.black, BORDER_THICKNESS ) );
		ImagePreviewPanel preview = new ImagePreviewPanel();
		setAccessory( preview );
		addPropertyChangeListener( preview );
	}	
	
	@Override
	/**
	 * Переписываем "родной" createDialog для того, чтобы получить возможность использовать 
	 * 	setUndecorated(true);
	 * который недоступен, если просто попытаться в использовать его в конструкторе
	 * (возникает exception, связанный с тем, что после pack() в createDialog нельзя уже ничего делать) 
	 */
	protected JDialog createDialog(Component parent) throws HeadlessException {
        
		int width = 704;
		
		FileChooserUI ui = getUI();
        String title = ui.getDialogTitle(this);
        putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY,
                          title);

        JDialog dialog;
        dialog = new JDialog((Dialog)null, title, true);
        //dialog.setComponentOrientation(this.getComponentOrientation());
 
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
       
        HTitleBar titleBar = new HTitleBar( Config.getItem( "file_chooser_title" ), width, dialog, true, false );
        contentPane.add( titleBar, BorderLayout.NORTH );

        if (JDialog.isDefaultLookAndFeelDecorated()) 
        {
            boolean supportsWindowDecorations =
            UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
            }
        }
        dialog.getRootPane().setDefaultButton(ui.getDefaultButton(this));
        dialog.setUndecorated(true);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }
}