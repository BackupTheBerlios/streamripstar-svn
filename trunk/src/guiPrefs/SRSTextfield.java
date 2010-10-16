package guiPrefs;

import java.awt.Color;
import javax.swing.JTextField;

public class SRSTextfield extends JTextField{
	private static final long serialVersionUID = 2239456758385044407L;

	public SRSTextfield() 
	{
		super();
		setPrefs();
	}
	
	public SRSTextfield(String text) 
	{
		super(text);
		setPrefs();
	}
	
	private void setPrefs()
	{
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.25f));
		this.setOpaque(false);
		this.setBorder(null);
		this.setForeground(Color.WHITE);
	}
}
