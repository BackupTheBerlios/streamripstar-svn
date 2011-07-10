package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import control.Control_GetPath;
import control.SRSOutput;

/**
 * Shows an Dialog where the log entries are visible. It loads the messages from file
 * and print it to the textarea, where the user can copy it.
 * 
 * @author Johannes Putzke
 *
 */
public class ViewLog extends JDialog
{
	private static final long serialVersionUID = 1L;
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	private JButton OKButton = new JButton("OK");
	private JButton deleteButton = new JButton("Delete File");
	private JPanel buttonPanel = new JPanel();
	private JTextArea logField = new JTextArea();
	private JScrollPane logSP = new JScrollPane(logField);

	/**
	 * Create the About-Dialog by give the controlStream object. The controlstream
	 * is necessary to open the links in a webbrowser.
	 * 
	 * @param mainWindow The parent window for this dialog 
	 */
	public ViewLog(JFrame mainWindow) 
	{
		super(mainWindow);
		
		setLayout(new BorderLayout());
		add(logSP, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		OKButton.addActionListener(new OKListener());
		deleteButton.addActionListener(new DeleteListener());
		
		buttonPanel.add(OKButton);
		buttonPanel.add(deleteButton);
		
    	setLanguage();
    	
    	logField.setEditable(false);
    	loadLogFromFile();
    	
        //set size of window
    	Dimension frameDim = new Dimension(600,600);
    	setSize(frameDim);
    	
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
	 * Updates the language for this window
	 */
	public void setLanguage() 
	{
		try 
		{
    		setTitle(trans.getString("ViewLog.title"));
    		OKButton.setText(trans.getString("ViewLog.okButton"));
    		deleteButton.setText(trans.getString("ViewLog.deleteButton"));
 
    	}
		catch ( MissingResourceException e )
		{ 
		      SRSOutput.getInstance().logE( e.getMessage() ); 
	    }
	}
	
	/**
	 * Loads the 
	 */
	private void loadLogFromFile()
	{
		BufferedReader reader = null;
		String txt = "";
		logField.setText(""); //make it empty
		
		try
		{
			String path = new Control_GetPath().getStreamRipStarPath() + "/output.log";
			reader = new BufferedReader(new FileReader(path));
			while((txt = reader.readLine()) != null)
			{
				logField.append(txt+"\n");
			}
		}
		catch (FileNotFoundException e)
		{
			SRSOutput.getInstance().log("No logfile found");
			
			JOptionPane.showConfirmDialog(ViewLog.this, trans.getString("ViewLog.FileNotFound"));
		}
		catch (IOException e)
		{
			SRSOutput.getInstance().logE("IOException when reading the error log file\n"+e.getMessage());
		}
		finally
		{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Close this dialog
	 * @author Johannes Putzke
	 */
    public class OKListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
    
	/**
	 * 
	 * @author Johannes Putzke
	 */
    public class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SRSOutput.getInstance().deleteLogFile();
            loadLogFromFile(); //update the view
        }
    }
}
