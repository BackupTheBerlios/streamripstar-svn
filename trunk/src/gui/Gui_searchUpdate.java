package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.*;

/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johanes Putzke*/
/* eMail: die_eule@gmx.net*/  

/**
 * The GUI where the user can look for informations about new
 * releases for StreamRipStar
 */
public class Gui_searchUpdate extends JDialog{
	private static final long serialVersionUID = 4165135034469872266L;
	private Control_Stream controlStream;
	private SearchUpdate searchUpdate = new SearchUpdate(this);
	private ImageIcon loadingIcon = new ImageIcon((URL)getClass().getResource("/Icons/update_loading.png"));
	private ImageIcon okIcon = new ImageIcon((URL)getClass().getResource("/Icons/update_ok.png"));
	private ImageIcon availableIcon = new ImageIcon((URL)getClass().getResource("/Icons/update_available.png"));
	private ImageIcon failIcon = new ImageIcon((URL)getClass().getResource("/Icons/update_fail.png"));
	private JPanel panel = new JPanel();
	private JButton okButton = new JButton("Beenden");
	private JLabel iconLabel = new JLabel(loadingIcon);
	private JLabel infoLabel = new JLabel("Searching for new Version. Please wait...");
	
	public Gui_searchUpdate(Control_Stream controlStream) {
		this.controlStream = controlStream;
		searchUpdate.start();
		panel.setLayout(new GridBagLayout());
		
		//set Constrains defaults
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 5, 5, 5, 5);
		c.gridwidth = 3;
		
		c.gridy = 0;
		c.gridx = 0;
		panel.add(iconLabel,c);
		c.gridy = 1;
		c.insets = new Insets( 10, 5, 15, 5);
		panel.add(infoLabel,c);
		c.gridy = 2;
		panel.add(okButton,c);
		
		okButton.addActionListener(new AbortListener());
		add(panel);
		
		//set size of window
		//pack together
		pack();
		//get new dimension of the window
        Dimension frameDim = getSize();
    	
        //get resolution
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        
        //calculates the app. values
        int x = (screenDim.width - frameDim.width)/2;
        int y = (screenDim.height - frameDim.height)/2;
        
        //set location
        setLocation(x, y);
		setVisible(true);
	}
	
	/**
	 * Set the Dialog with the information, the the user knows, that this
	 * version of StreamRipStar is the newst
	 */
	public void setAllOk() {
		iconLabel.setIcon(okIcon);
		infoLabel.setText("You have already the latest version of StreamRipStar");
		
		//show the new size
		pack();
	}
	
	/**
	 * Show the user, that it has a newer version, that the sever knows
	 */
	public void setNewVersion() {
		iconLabel.setIcon(okIcon);
		infoLabel.setText("Gratulation. You have a NEWER version of StreamRipStar than available");
		
		//show the new size
		pack();
	}
	
	/**
	 * Show all information about the new release.  
	 * @param revision The new revision (integer value)
	 * @param version The name of the version (e.g. StreamRipStar 0.6 Alpha 1)
	 * @param url The url to the download of this version
	 */
	public void setNewVersionAvailable(String revision, String version, String url) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 5, 5, 5, 5);
		c.gridwidth = 3;
		
		iconLabel.setIcon(availableIcon);
		infoLabel.setText("Found a new Version of StreamRipStar");
		
		c.gridwidth = 1;
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new JLabel("Version :"),c);
		c.gridx = 1;
		panel.add(new JLabel(version),c);	
		c.gridx = 0;
		c.gridy = 3;
		panel.add(new JLabel("Revision :"),c);
		c.gridx = 1;
		panel.add(new JLabel(revision),c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(new JLabel("Download :"),c);
		c.gridx = 1;
		panel.add(new JLabel(url),c);
		
		c.insets = new Insets( 15, 5, 5, 5);
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 5;
		panel.add(okButton,c);
		
		//show the new size
		pack();
	}
	
	/**
	 * If something did wrong (e.g. no connection to the internet),
	 * show it to the user
	 */
	public void setFailedToFetchInformation() {
		iconLabel.setIcon(failIcon);
		infoLabel.setText("Fail to load information about a new release");
		
		//show the new size
		pack();
	}
	
	/**
	 * Stop this dialog and show the mainwindow
	 * 
	 * @author Johannes Putzke
	 */
	class AbortListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
