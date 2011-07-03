package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import control.VolumeManager;

/**
 * Shows a Slider to control the Volume of the music
 * 
 * @author Johannes Putzke
 */
public class VolumeControlGUI extends JPanel implements MouseMotionListener, ChangeListener
{
	private static final long serialVersionUID = -838962312550616576L;
	private VolumeManager vm;
	private JSlider audioSlider;
	
	/**
	 * Creates the horizontal Slider and the listeners for the mousewheel
	 * 
	 * @param vm the Volumemanager that controls the volume in the whole application
	 */
	public VolumeControlGUI(VolumeManager vm)
	{
		super();
		this.vm = vm;
		audioSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
		audioSlider.setMajorTickSpacing(10);
		audioSlider.setPaintTicks(true);

		Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel> ();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 25), new JLabel("25") );
		labelTable.put( new Integer( 50), new JLabel("50") );
		labelTable.put( new Integer( 75), new JLabel("75") );
		labelTable.put( new Integer( 100 ), new JLabel("100") );
		audioSlider.setLabelTable( labelTable );
		audioSlider.setPaintLabels(true);
		add(audioSlider);
		addMouseMotionListener(this);
		audioSlider.addChangeListener(this);
		addMouseWheelListener(new MouseWheelVolumeChangeListener());
		
		//add this volume slider to the volume manager
		vm.addVolumeControl(this);
	}

	/**
	 * Get the current volume of this volume controller
	 * @return the percentage value between 0 and 100
	 */
	public int getVolume()
	{
		return audioSlider.getValue();
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
			audioSlider.setValue(newVolume);
			repaint();
		}
	}

	/**
	 * Looks for a change from a mousewheel and adjust the
	 * volume for a specific volume step 
	 * 
	 * @author Johannes Putzke
	 */
	class MouseWheelVolumeChangeListener implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0)
		{
			//how much does the wheel turned?
			int howMuch = arg0.getWheelRotation();
			//how loud is the sound at the moment?
			int percentage = audioSlider.getValue();
			
			//no decision about how much the wheels turns
			//use for every event just one step
			
			//here forward -> louder
			if(howMuch < 0)
			{
				percentage += 10;
				
				if(percentage > 100)
				{
					percentage = 100;
				}
			}
			//here backwards -> quieter
			if(howMuch > 0)
			{
				percentage -= 10;
				
				if(percentage < 0)
				{
					percentage = 0;
				}
			}
			//change volume local:
			audioSlider.setValue(percentage);
			
//			//change the volume and the slider in the whole application
//			vm.changeVolume(VolumeControlGUI.this,percentage);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		//change the volume and the slider in the whole application
		vm.changeVolume(VolumeControlGUI.this,audioSlider.getValue());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent arg0) { }

	
	
//	//for testing issues 
//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//		//frame.add(new VolumeControlGUI());
//		frame.setSize(100,100);
//		frame.setVisible(true);
//	}
}
