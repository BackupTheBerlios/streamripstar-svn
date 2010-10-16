package guiPrefs;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SRSLabel extends JLabel{
	private static final long serialVersionUID = 2239456758385044407L;

	public SRSLabel() 
	{
		super();
		setPrefs();
	}
	
	public SRSLabel(String text) 
	{
		super(text);
		setPrefs();
	}
	
	public SRSLabel(ImageIcon icon, int position) 
	{
		super(icon,position);
		setPrefs();
	}
	
	private void setPrefs()
	{
		this.setForeground(Color.WHITE);
	}
}
