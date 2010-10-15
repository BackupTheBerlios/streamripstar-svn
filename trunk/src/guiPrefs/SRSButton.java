package guiPrefs;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

public class SRSButton extends JButton implements MouseListener{
	private static final long serialVersionUID = 2239456758385044407L;
	
	private Color startColor;
	private Color endColor;
	
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
	}
	
	private void setPrefs()
	{
		startColor = new Color(178,253,83,255);
		endColor = new Color(120,196,25,255);
		this.setRolloverEnabled(false);
		this.setContentAreaFilled(false);
		addMouseListener(this);
	}

	@Override
	public void paint(Graphics g)
	{
		
		Graphics2D g2 = (Graphics2D) g;
		
		GradientPaint vl = new GradientPaint(
				getWidth(),0, startColor,
				getWidth(),getHeight(),endColor);
		g2.setPaint(vl);
		g2.fill(new RoundRectangle2D.Double(0, 0, this.getWidth(),this.getHeight(), 0, 0));
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		startColor = new Color(178,253,83,255);
		endColor = new Color(120,196,25,255);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		startColor = new Color(178,253,83,180);
		endColor = new Color(120,196,25,180);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		startColor = new Color(178,253,83,255);
		endColor = new Color(120,196,25,255);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		endColor = new Color(178,253,83,255);
		startColor = new Color(120,196,25,255);
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		startColor = new Color(178,253,83,255);
		endColor = new Color(120,196,25,255);
	}
}
