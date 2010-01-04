package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import control.Control_Stream;

public class Gui_AboutStreamRipStar extends JFrame
{
	private static final long serialVersionUID = 1L;

	private ResourceBundle trans = null;
	private Control_Stream controlStreams = null;
	
	private JLabel programming = new JLabel("Programming :");
	private JTextField programmerName = new JTextField("Johannes Putzke"); 
	private JLabel IconsAndGrap = new JLabel("Icons and Graphics :");
	private JTextField iconAndGrapName = new JTextField("Christian Putzke (EdMolf)");
	private JLabel license = new JLabel("License :");
	private JTextField licenceName = new JTextField("GPLV3");
	private JLabel version = new JLabel("Version :");
	private JTextField versionName = new JTextField("StreamRipStar 0.5.6");
	private JLabel revision = new JLabel("Revision :");
	private JTextField revisionnumber = new JTextField("626");
	private JLabel streamRipStarSourceforgeSite = new JLabel("StreamRipStars Projektpage :");
	private JTextField streamRipStarSourceforgeAddress = new JTextField("http://sourceforge.net/projects/stripper");
	private JLabel streamRipStarHomepage = new JLabel("StreamRipStars Wiki :");
	private JTextField streamRipStarHomepageAddress = new JTextField("http://sourceforge.net/apps/mediawiki/stripper");
	private JLabel edmolfHomepage = new JLabel("EdMolfs Homepage");
	private JTextField edmolfHomepageAddress = new JTextField("http://www.edmolf.net");
	private JLabel streamripperHomepage = new JLabel("Streamrippers Homepage");
	private JTextField streamripperHomepageAddress = new JTextField("http://streamripper.sourceforge.net");
	
	private JLabel copyRight = new JLabel("Copyright @ Johannes Putzke");
	
	private JLabel BannerLabel = new JLabel(new ImageIcon((URL) getClass()
			.getResource("/Icons/banner.png")),JLabel.CENTER);
	private JButton OKButton = new JButton("OK");

	
	public Gui_AboutStreamRipStar(ResourceBundle trans,Control_Stream controlStreams)
	{
		this.trans = trans;
		this.controlStreams = controlStreams;
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weighty = 0.0;
		panel.add(BannerLabel,c);
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel(""),c);
		c.weighty = 0.0;
		c.insets = new Insets( 10, 10, 2, 10);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(programming,c);
		c.gridx = 1;
		panel.add(programmerName,c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(IconsAndGrap,c);
		c.gridx = 1;
		panel.add(iconAndGrapName,c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(license,c);
		c.gridx = 1;
		panel.add(licenceName,c);
		c.gridx = 0;
		c.gridy = 5;
		panel.add(version,c);
		c.gridx = 1;
		panel.add(versionName,c);
		c.gridx = 0;
		c.gridy = 6;
		panel.add(revision,c);
		c.gridx = 1;
		panel.add(revisionnumber,c);
		c.gridx = 0;
		c.gridy = 7;
		panel.add(streamRipStarSourceforgeSite,c);
		c.gridx = 1;
		panel.add(streamRipStarSourceforgeAddress,c);
		c.gridx = 0;
		c.gridy = 8;
		panel.add(streamRipStarHomepage,c);
		c.gridx = 1;
		panel.add(streamRipStarHomepageAddress,c);
		c.gridx = 0;
		c.gridy = 9;
		panel.add(streamripperHomepage,c);
		c.gridx = 1;
		panel.add(streamripperHomepageAddress,c);
		c.gridx = 0;
		c.gridy = 10;
		panel.add(edmolfHomepage,c);
		c.gridx = 1;
		panel.add(edmolfHomepageAddress,c);
		c.gridwidth = 2;
		c.gridy = 11;
		c.gridx = 0;
		panel.add(copyRight,c);	
		c.gridy = 12;
		c.weighty = 0.5;
		panel.add(new JLabel(""),c);
		c.insets = new Insets( 10, 10, 10, 10);
		c.weighty = 0.0;
		c.gridy = 13;
		panel.add(OKButton,c);
		this.setResizable(false);
		
		 //escape for exit
	    KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
		getRootPane().registerKeyboardAction(new OKListener(), escStroke,
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		OKButton.addActionListener(new OKListener());
		
		streamRipStarSourceforgeAddress.addMouseListener(new OpenWebSiteListener());
		streamRipStarHomepageAddress.addMouseListener(new OpenWebSiteListener());
		edmolfHomepageAddress.addMouseListener(new OpenWebSiteListener());
		streamripperHomepageAddress.addMouseListener(new OpenWebSiteListener());
		
        //set size of window
    	pack();
    	Dimension frameDim = getSize();
    	
        //get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //calculates the app. values
        int x = (screenDim.width - frameDim.width)/2;
        int y = (screenDim.height - frameDim.height)/2;
        
        //set location
        
        setLocation(x, y);
    	setLanguage();
    	makeEditable(false);
    	setVisible(true);
	}
	
	public void makeEditable(Boolean edit)
	{
		programmerName.setEditable(edit);
		iconAndGrapName.setEditable(edit);
		licenceName.setEditable(edit);
		revisionnumber.setEditable(edit);
		versionName.setEditable(edit);
		streamRipStarSourceforgeAddress.setEditable(edit);
		streamRipStarHomepageAddress.setEditable(edit);
		edmolfHomepageAddress.setEditable(edit);
		streamripperHomepageAddress.setEditable(edit);
	}
	
	
	public void setLanguage()
	{
		try
    	{
    		setTitle(trans.getString("about"));
    		programming.setText(trans.getString("programming"));
    		IconsAndGrap.setText(trans.getString("IconsAndGrap"));
    		license.setText(trans.getString("license"));
    		revision.setText(trans.getString("revision"));
    		version.setText(trans.getString("version"));
    		streamRipStarSourceforgeSite.setText(trans.getString("streamRipStarSourceforgeSite"));
    		streamRipStarHomepage.setText(trans.getString("streamRipStarHomepage"));
    	}
		catch ( MissingResourceException e )
		{ 
		      System.err.println( e ); 
	    }
	}
	
	
	//Listeners
    public class OKListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
    
	public class OpenWebSiteListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
				controlStreams.startWebBrowser(((JTextField)e.getSource()).getText());
		}
		
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
}
