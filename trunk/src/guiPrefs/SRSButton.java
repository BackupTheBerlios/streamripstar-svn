package guiPrefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SRSButton extends JButton{
	private static final long serialVersionUID = 2239456758385044407L;
	private Image backgroundImage;			//the default image
	
	public SRSButton() 
	{
		super();
		setPrefs();
	}
	
	public SRSButton(String text) 
	{
		super(text);
		setPrefs();
	}
	
	public SRSButton(String text, Image bgImage) 
	{
		super(text);
		setPrefs();
		backgroundImage = bgImage;
	}
	
	private void setPrefs()
	{
		backgroundImage = new ImageIcon((URL)getClass().getResource("/Icons/aboutDialog/button.png")).getImage();
//		this.setBorder(null);
	}
	
	private void maxSizeAfterNewImage()
	{

	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(backgroundImage, 0, 0, null);
	}
}
