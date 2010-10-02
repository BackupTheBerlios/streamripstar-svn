package gui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class IconButton extends JButton 
{
	private static final long serialVersionUID = -4009411873391033570L;
	
	public IconButton()
	{
		super();
		setDefaults();
	}
	
	public IconButton(ImageIcon icon) 
	{
		super(icon);
		setDefaults();
	}
	
	public IconButton(String text,ImageIcon icon) 
	{
		super(text,icon);
		setDefaults();
	}
	
	/**
	 * Set the useful default settings for an icon button
	 */
	public void setDefaults()
	{
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setBorderPainted(false);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

}
