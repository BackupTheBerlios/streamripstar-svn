package guiPrefs;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class SRSPanel extends JPanel 
{
	private static final long serialVersionUID = 401809601955122714L;

	private Image backgroundImage;
	
	public SRSPanel(Image bgImage) 
	{
		super();
		this.backgroundImage = bgImage;
		//backgroundImage = new ImageIcon((URL)getClass().getResource("/Icons/aboutDialog/hintergrund.png")).getImage();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(backgroundImage, 0, 0, null);
	}

}
