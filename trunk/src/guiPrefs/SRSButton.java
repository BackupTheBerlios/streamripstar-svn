package guiPrefs;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SRSButton extends JButton implements MouseListener{
	private static final long serialVersionUID = 2239456758385044407L;
	
	private Color startColor = new Color(178,253,83,255);
	private Color endColor = new Color(120,196,25,255);
	private Color startColorInUse = startColor;
	private Color endColorInUse = endColor;
	
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

	public SRSButton(String text, ImageIcon icon) 
	{
		super(text,icon);
		setPrefs();
	}
	
	public SRSButton(ImageIcon icon) 
	{
		super(icon);
		setPrefs();
	}
	
	private void setPrefs()
	{
//		setRolloverEnabled(false);
		setContentAreaFilled(false);
		addMouseListener(this);
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		GradientPaint vl = new GradientPaint(
				getWidth(),0, startColorInUse,
				getWidth(),getHeight(),endColorInUse);
		g2.setPaint(vl);
		g2.fill(new RoundRectangle2D.Double(0, 0, this.getWidth(),this.getHeight(), 0, 0));
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		startColorInUse = startColor;
		endColorInUse = endColor;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		startColorInUse = endColor;
		endColorInUse = startColor;
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		startColorInUse = startColor;
		endColorInUse = endColor;
	}
}
