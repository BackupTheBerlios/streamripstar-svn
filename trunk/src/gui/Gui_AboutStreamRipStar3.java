package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import guiPrefs.SRSButton;
import guiPrefs.SRSPanel;
import guiPrefs.SRSTextArea;
import guiPrefs.SRSTextfield;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import control.Control_Stream;

/**
 * Shows an Dialog with all information about StreamRipStar. Here you can find the
 * link to StreamRipStars website, the version and revision.
 * 
 * @author Johannes Putzke
 *
 */
public class Gui_AboutStreamRipStar3 extends JDialog
{
	private static final long serialVersionUID = 1L;
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	private Control_Stream controlStreams = null;
	
	private JLabel programming = new JLabel("Programming :");
	private SRSTextfield programmerName = new SRSTextfield("Johannes Putzke"); 
	private JLabel IconsAndGrap = new JLabel("Icons and Graphics :");
	private SRSTextArea iconAndGrapName = new SRSTextArea("Christian Putzke (Edmolf) - Icons Mainwindow," +
			"	\n\tLogo and other graphics\nOxygen Icon Projekt - Streambrowser, Updatedialog");
	private JLabel license = new JLabel("License :");
	private SRSTextfield licenceName = new SRSTextfield("GPLV3");
	private JLabel version = new JLabel("Version :");
	private SRSTextfield versionName = new SRSTextfield(misc.StreamRipStar.releaseVersion);
	private JLabel revision = new JLabel("Revision :");
	private SRSTextfield revisionnumber = new SRSTextfield(misc.StreamRipStar.releaseRevision+"");
	private JLabel streamRipStarSourceforgeSite = new JLabel("StreamRipStars Projektpage :");
	private SRSTextfield streamRipStarSourceforgeAddress = new SRSTextfield("http://developer.berlios.de/projects/streamripstar/");
	private JLabel streamRipStarHomepage = new JLabel("StreamRipStars Wiki :");
	private SRSTextfield streamRipStarHomepageAddress = new SRSTextfield("http://sourceforge.net/apps/mediawiki/stripper");
	private JLabel edmolfHomepage = new JLabel("EdMolfs Homepage");
	private SRSTextfield edmolfHomepageAddress = new SRSTextfield("http://www.edmolf.net");
	private JLabel streamripperHomepage = new JLabel("Streamrippers Homepage");
	private SRSTextfield streamripperHomepageAddress = new SRSTextfield("http://developer.berlios.de/projects/streamripstar/");
	private JLabel externPrograms = new JLabel("Extern Programms");
	private SRSTextArea externProgramsTA = new SRSTextArea("gstreamer-java (GPL)");
	
	private JLabel copyRight = new JLabel("Copyright @ Johannes Putzke");
	
	private JLabel BannerLabel = new JLabel(new ImageIcon((URL) getClass()
			.getResource("/Icons/aboutDialog/logo.png")),JLabel.CENTER);
	private SRSButton OKButton = new SRSButton("OK");

	/**
	 * Create the About-Dialog by give the controlStream object. The controlstream
	 * is necessary to open the links in a webbrowser.
	 * 
	 * @param controlStreams The used controlstream, where you can start the webbrowser
	 * @param mainWindow The parent window for this dialog 
	 */
	public Gui_AboutStreamRipStar3(Control_Stream controlStreams, JFrame mainWindow) {
		super(mainWindow);
		this.controlStreams = controlStreams;
		ImageIcon tmpIcon = new ImageIcon((URL)getClass().getResource("/Icons/aboutDialog/hintergrund.png"));
		SRSPanel panel = new SRSPanel(tmpIcon.getImage());
		add(panel);
		panel.setLayout(new GridBagLayout());
		
        //set size of window
    	Dimension frameDim = new Dimension(tmpIcon.getIconWidth(),tmpIcon.getIconHeight());
    	setSize(frameDim);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
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
		c.gridy = 11;
		c.gridx = 0;
		panel.add(externPrograms,c);
		c.gridx = 1;
		c.gridwidth = 1;
		panel.add(externProgramsTA,c);
		c.gridy = 12;
		c.gridx = 1;
		c.gridwidth = 2;
		panel.add(copyRight,c);	
		c.gridx = 0;
		c.gridy = 13;
		c.weighty = 0.5;
		panel.add(new JLabel(""),c);
		c.insets = new Insets( 10, 10, 10, 10);
		c.weighty = 0.0;
		c.gridy = 14;
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
		
    	setLanguage();

        //get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //calculates the app. values
        int x = (screenDim.width - frameDim.width)/2;
        int y = (screenDim.height - frameDim.height)/2;
        
        //set location
        
        setLocation(x, y);
    	makeEditable(false);
    	setVisible(true);
	}
	
	/**
	 * make all textfields editable or not
	 * 
	 * @param edit
	 */
	public void makeEditable(Boolean edit) {
		programmerName.setEditable(edit);
		iconAndGrapName.setEditable(edit);
		licenceName.setEditable(edit);
		revisionnumber.setEditable(edit);
		versionName.setEditable(edit);
		streamRipStarSourceforgeAddress.setEditable(edit);
		streamRipStarHomepageAddress.setEditable(edit);
		edmolfHomepageAddress.setEditable(edit);
		streamripperHomepageAddress.setEditable(edit);
		externProgramsTA.setEditable(edit);
	}
	
	
	public void setLanguage() {
		try {
    		setTitle(trans.getString("about"));
    		programming.setText(trans.getString("programming"));
    		IconsAndGrap.setText(trans.getString("IconsAndGrap"));
    		license.setText(trans.getString("license"));
    		revision.setText(trans.getString("revision"));
    		version.setText(trans.getString("version"));
    		streamRipStarSourceforgeSite.setText(trans.getString("streamRipStarSourceforgeSite"));
    		streamRipStarHomepage.setText(trans.getString("streamRipStarHomepage"));
    		externPrograms.setText(trans.getString("about.externPrograms"));
    	}
		catch ( MissingResourceException e ) { 
		      System.err.println( e ); 
	    }
	}
	
	
	/**
	 * 
	 * @author Johannes Putzke
	 */
    public class OKListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
    
    /**
     * Opens the website, when someone clicked in the website 
     */
	public class OpenWebSiteListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			controlStreams.startWebBrowser(((SRSTextfield)e.getSource()).getText());
		}
	}
}
