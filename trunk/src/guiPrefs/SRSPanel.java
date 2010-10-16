package guiPrefs;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class SRSPanel extends JPanel 
{
	private static final long serialVersionUID = 401809601955122714L;

	private Image backgroundImage;		//the background image, if one is used
	private Color startColor = new Color(35,151,162,255);
	private Color endColor = new Color(55,197,255,255);
	private boolean imageIsUsed = false;
	
	public SRSPanel() 
	{
		super();
		setPref();
	}
	
	public SRSPanel(Image bgImage) 
	{
		super();
		backgroundImage = bgImage;
		imageIsUsed = true;
		setPref();
	}
	
	public void setPref()
	{
		setBackground(endColor);
	}

	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		if (imageIsUsed) 
		{
			g.drawImage(backgroundImage, 0, 0, null);
		}
		else
		{
			Graphics2D g2 = (Graphics2D) g;
			GradientPaint vl = new GradientPaint(
					getWidth(),0, startColor,
					getWidth(),getHeight(),endColor);
			g2.setPaint(vl);
			g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(),getHeight(), 0, 0));
		}
	}
}
