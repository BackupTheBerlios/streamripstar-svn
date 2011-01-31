package guiPrefs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class VolumeControl extends JLabel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = -6739267281511679094L;
	private BasicStroke inUseStroke = new BasicStroke(1);
	private int percentage = 50;
	private final int FAKTOR = 3;
	private final int PADDING = 5;
	//
	public VolumeControl()
	{
		super();
//		this.setMinimumSize(new Dimension(100, 2));
//		this.setPreferredSize(new Dimension(200, 2));
		setText(" ");
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		setPreferredSize(new Dimension(getSize().height*FAKTOR,getSize().height ));
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(inUseStroke);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		//draw the inner ring to show the area where you can change the volume
        Polygon p2 = new Polygon();
        p2.addPoint( PADDING , getSize().height-PADDING);
        p2.addPoint( ((getSize().height*FAKTOR) * percentage/100)-PADDING , getSize().height-PADDING );
        p2.addPoint( ((getSize().height*FAKTOR) * percentage/100)-PADDING , (getSize().height-PADDING)*(100-percentage)/100+PADDING);
        g2.setColor(Color.BLUE);
        g2.fillPolygon(p2);
        
		//draw the outer ring to show the area where you can change the volume
        Polygon p = new Polygon();
        p.addPoint( PADDING , getSize().height-PADDING );
        p.addPoint( getSize().height*FAKTOR-PADDING, getSize().height-PADDING);
        p.addPoint( getSize().height*FAKTOR-PADDING, PADDING);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(p);

	}
	
	/**
	 * Set the new value for the audio slider
	 * @param newPercentage the new value between 0 and 100
	 */
	public void setPercentage(int newPercentage)
	{
		if(newPercentage >= 0 && newPercentage <= 100)
		{
			percentage = newPercentage;
		}
	}

	/**
	 * Get the current volume of this volume controler
	 * @return the percentage value between 0 and 100
	 */
	public int getVolume()
	{
		return percentage;
	}
	
	/**
	 * Set the new value for the audio slider. This does exactly the same
	 * as the setPercentage methode
	 * @param newVolume the new value between 0 and 100
	 */
	public void setVolume(int newVolume)
	{
		if(newVolume >= 0 && newVolume <= 100)
		{
			percentage = newVolume;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) { }
	@Override
	public void mouseMoved(MouseEvent arg0) { }
	@Override
	public void mouseReleased(MouseEvent arg0) { }
	
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		e.consume();
		int posX = e.getPoint().x;
		
		if(posX >= 0) 
		{
			percentage = 100*(posX+PADDING+1)/getSize().width;
			
			if(percentage > 100)
			{
				percentage = 100;
			}
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		e.consume();
		
		int posX = e.getPoint().x;
		
		if(posX >= 0) 
		{
			percentage = 100*(posX+PADDING+1)/getSize().width;
			
			if(percentage > 100)
			{
				percentage = 100;
			}
			repaint();
		}
	}
}
