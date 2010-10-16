package guiPrefs;

import java.awt.Color;
import javax.swing.JCheckBox;

public class SRSCheckBox extends JCheckBox{
	private static final long serialVersionUID = 2239456758385044407L;

	public SRSCheckBox() 
	{
		super();
		setPrefs();
	}
	
	public SRSCheckBox(String text) 
	{
		super(text);
		setPrefs();
	}
	
	public SRSCheckBox(String text, boolean selected) 
	{
		super(text,selected);
		setPrefs();
	}
	
	private void setPrefs()
	{
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.25f));
		this.setOpaque(false);
		this.setBorder(null);
	}
}
