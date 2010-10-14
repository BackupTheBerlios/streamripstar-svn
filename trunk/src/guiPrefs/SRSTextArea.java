package guiPrefs;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SRSTextArea extends JTextArea{
	private static final long serialVersionUID = 2239456758385044407L;

	public SRSTextArea() 
	{
		super();
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.25f));
		this.setOpaque(false);
	}
	
	public SRSTextArea(String text) 
	{
		super(text);
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.25f));
		this.setOpaque(false);
	}
}
