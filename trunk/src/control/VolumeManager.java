package control;

import gui.Gui_StreamRipStar;
import guiPrefs.VolumeControl;

import java.util.Vector;

public class VolumeManager
{
	private Vector<VolumeControl> list = new Vector<VolumeControl>(0,1);
	private Gui_StreamRipStar mainGui;
	
	public VolumeManager(Gui_StreamRipStar mainGui)
	{
		this.mainGui = mainGui;
	}
	
	/**
	 * Sets a new volume
	 * 
	 * @param newVolume: the new volume in percent
	 */
	public void changeVolume(VolumeControl vc,int newVolume)
	{
		if(vc != null)
		{
			for(int i=0; i<list.capacity(); i++)
			{
				//only change the volume, if this slider does not changed the volume
				if(list.get(i) != vc)
				{
					list.get(i).setVolume(newVolume);
				}
				
				//finally set it in the sound system
				mainGui.setVolume(newVolume);
			}
		}
	}
	
	public void addVolumeControl(VolumeControl vc)
	{
		if(vc != null)
		{
			list.add(vc);			
		}
	}
	
	public void deleteVolumeControl(VolumeControl vc)
	{
		if(vc != null)
		{
			list.remove(vc);
			list.trimToSize();
		}
	}
}
