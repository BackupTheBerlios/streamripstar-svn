package control;
 /* This program is licensed under the terms of the GPLV3 or newer*/
/* Written by Johanes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.io.File;

public class Control_TestStartFirst {
	
//	The Class looks for the HOME on the spec. OS
//	If a known OS exist, it looks for the folder StreamRipStar
//	The folder will contain the settings- and Streamfiles
//	If the folder an files doesn't exist -> create them
//	
//	On Linux the folder will be hidden
//	
//	If no know OS is available the settings will be saved
//	in the working-directory
	
	public Control_TestStartFirst() {
	}

	public String[][] searchPrograms() {
		//Look for the OS and give the found programs back
		String opsys = System.getProperty("os.name");
		if(opsys.startsWith("Linux"))
			return searchProgrammsLinux();
		if(opsys.startsWith("Windows"))
			return searchProgrammsWindows();
		return null;
	}
	
	public String[][] searchProgrammsLinux() {
//	
//		These method searches for some standard programs
//		with are usually installed on a linux system.
//		The return String[] includes:
//		[0] - FileBrowser
//		[1] - WebBrowser
//		[2] - mp3/ogg Player
//		[3] - streamripper
//	
		String[][] programNames = {
/*[0]*/		{"thunar","konqueror","nautilus","dolphin"},
/*[1]*/		{"firefox","firefox-bin","iceweasel","opera","epiphany","galeon"},
/*[2]*/		{"audacious","qmmp","beep-media-player","xmms","amarok","mplayer"},
/*[3]*/		{"streamripper"}};
		String[] binPath = getPathBinLinux();
		
		//catch an empty array for wrong installed java systems
		if(binPath == null) {
			SRSOutput.getInstance().logE("No executable bin path found on the linux system");
			return null;
		}
		String[][] foundPrograms = new String[programNames.length][6];
		
		for(int i=0;i<programNames.length;i++) {
			int k=0;
			for(int l=0;l<programNames[i].length;l++) {
				for(int j=0;j<binPath.length;j++) {
					File temp = new File(binPath[j]+"/"+programNames[i][l]);
					if(temp.exists()) {
						foundPrograms[i][k]=binPath[j]+"/"+programNames[i][l];
						k++;
					}
				}
			}
		}
		return foundPrograms;
	}
	

	public String[] getPathBinLinux() {
//	
//		This method looks for the path where all executable program 
//		are linked on a Linux system. 
//	
		String pathBin = System.getenv("PATH");
		int countDoublePoint = 0;
		int i = 0;
		while(i<pathBin.length()) {
			if ((pathBin.charAt(i)) == ':')
				countDoublePoint++;
			i++;
		}
		i=0;
		String[] pathArray = new String[countDoublePoint+1];
		
//		Set default values to an empty String
		for(int g=0 ; g<countDoublePoint+1 ; g++) {
			pathArray[g]="";
		}
		countDoublePoint=0;
		while(i<pathBin.length()) {
			if((pathBin.charAt(i)) == ':') {
				countDoublePoint++;
			} else {
				pathArray[countDoublePoint] += pathBin.substring(i, i+1);
			}
			i++;		
		}
		return pathArray;
	}
	
	public String[] getPathBinWindows() {
		String[] pathArray;
		String binPath = System.getenv("PATH");
		int countCommata = 0;
		int i = 0;
		while(i<binPath.length()) {
			if((binPath.charAt(i))== ';')
				countCommata++;
			i++;
		}
		i=0;
		pathArray = new String[countCommata+2];
		
		//set default values to ""
		for(int j=0; j< countCommata+2;j++)
			pathArray[j]="";
		int array = countCommata+2;
		countCommata = 0;
		
		while(i<binPath.length()) {
			if((binPath.charAt(i))== ';')
				countCommata++;
			else
				pathArray[countCommata] += binPath.substring(i, i+1);
			i++;
		}
		//Programs are often not in PATH
		
		pathArray[array-1] = System.getenv("ProgramFiles");
		
		return pathArray;
	}
	
	public String[][] searchProgrammsWindows()
	{
		String[] exePath = getPathBinWindows();

		
		//List of some well known programs
		String[][] programNames = {
				{"\\zabkat\\xplorer2","\\explorer.exe"},
				{"\\Mozilla Firefox\\firefox.exe","\\Opera\\Opera.exe",},
				{"\\Winamp\\winamp.exe","\\Windows Media Player\\wmplayer.exe","VideoLAN\\VLC\\vlc.exe"},
				{"\\Streamripper\\streamripper.exe"}};
		
		String foundPrograms[][]= new String[programNames.length][4];
		
		for(int i=0; i<programNames.length;i++)
		{
			int k=0;
			for(int l=0;l<programNames[i].length;l++)
			{
				for(int j=0;j<exePath.length;j++)
				{
					File temp = new File(exePath[j]+programNames[i][l]);
					if(temp.exists())
					{
						foundPrograms[i][k] = exePath[j]+programNames[i][l];
						k++;
					}
				}
			}
		}
		return foundPrograms;
	}

}
