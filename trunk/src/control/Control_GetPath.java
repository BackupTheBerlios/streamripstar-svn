package control;
/* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/**
 * a collection of methods to find out the correct paths on the systems
 * @author Schmoffel
 *
 */
public class Control_GetPath {
	private String path = null;
	private String home = System.getProperty("user.home");
	private String opsys = System.getProperty("os.name");
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	/**
	 * empty constructor
	 */
	public Control_GetPath() {
		
	}
	
	/**
	 * looks for the home path and return the path
	 * as a string.
	 * e.g: "/home/test/.StreamRipStar/"
	 * @return The home path as a String
	 */
	public String getStreamRipStarPath() {
		//look first in the exe directory
		File savePath = new File("StreamRipStar");
		
		//look first for the save path in the executable path
		//if it don't exist, look for the path in the home
		//directory
		if(!savePath.exists()) {
			//set home path for Linux
			if(opsys.startsWith("Linux")) {
				savePath = new File( home+"/.StreamRipStar");
				if(savePath.exists() && savePath.isDirectory()) {
					path =  home+"/.StreamRipStar";
					return path;
				} else {
					askWhereToSave();
					SRSOutput.getInstance().log("Created new save path in : " + path);
					return path;
				}
			}
			
			//set home path for Windows
			else if (opsys.startsWith("Windows")) {
				savePath = new File( home+"\\.StreamRipStar");
				if( savePath.exists() && savePath.isDirectory() ) {
					path = home+"\\.StreamRipStar";
					return path;
				} else {
					askWhereToSave();
					SRSOutput.getInstance().log("Created new save path in : " + path);
					return path;
				}
			}
			
			//else set an default value
			else {
				String sep = System.getProperty("file.separator");
				savePath = new File( home+sep+".StreamRipStar");
				if( savePath.exists() && savePath.isDirectory() ) {
					path = home+sep+".StreamRipStar";
					SRSOutput.getInstance().log("Save Path exist in: " + path);
					return path;
				} else {
					askWhereToSave();
					SRSOutput.getInstance().log("Created new save path in : " + path);
					return path;
				}
			}
		} else {
			if(savePath.isDirectory()) {
				return(savePath.getPath());
			}
			else {
				askWhereToSave();
				SRSOutput.getInstance().log("Created new save path in : " + path);
				return path;
			}
		}
	}
	
	/**
	 * set the savePath
	 * @param newPath
	 */
	public void setPath(String newPath) {
		path = newPath;
	}
	
	/**
	 * shows a Popup witch ask the user where to save
	 * the config file of StreamRipStar
	 * @return
	 */
	public void askWhereToSave() {
		
		String[] options = {trans.getString("GetPath.home"),trans.getString("GetPath.portable")};
		
		int choose = JOptionPane.showOptionDialog(null,trans.getString("GetPath.WhereSaveQuestion"),
				trans.getString("GetPath.WhereSaveTitle"),JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,
				options,options[0]);
		if(choose == 1) {
			path = "StreamRipStar";
		} else {
			//create the OS specific directories 
			if (opsys.startsWith("Windows")) {
				path = home+"\\.StreamRipStar";
			} else if(opsys.startsWith("Linux")) {
				path =  home+"/.StreamRipStar";
			} else {
				String sep = System.getProperty("file.separator");
				path =  home+sep+".StreamRipStar";
			}
		}
		//finally create the directory for the config file
		new File(path).mkdir();
	}
}
