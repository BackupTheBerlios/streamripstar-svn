package thread;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/

import gui.Gui_StreamRipStar;

import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Vector;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import misc.SchedulJob;
import misc.Stream;


import control.Control_GetPath;
import control.Control_Threads;


public class Thread_Control_Schedul extends Thread{
	
	//contains all scheduljobs
	private Vector<SchedulJob> schedulVector = new Vector<SchedulJob>(0,1);
	private Gui_StreamRipStar mainGui = null;
	private Boolean stop = false;
	private Boolean updateView = false;
	private Control_Threads controlThread = null;
	private ResourceBundle trans = ResourceBundle.getBundle("translations.StreamRipStar");
	
	public Thread_Control_Schedul(Gui_StreamRipStar mainGui, Control_Threads controlThread) {
		this.mainGui = mainGui;
		this.controlThread = controlThread;
	}
	
	/**
	 * runs forever. It looks every second in the array of scheduljobs
	 * if a new stream can be recorded
	 */
	public void run() {
		//look, that the table is ready for filling
		while (controlThread.askForLoadingScheduls() == false) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err.println("Error while waiting for starting the scheduls : Thread_Control_Scheduls");
			}
		}
		//load all jobs from xml-file into vector
		loadScheduleJobsOnStart();
		//update times for daily,weekly and monthly jobs
		setNextTimeForAllJobs(); 
		
		while(!stop) {
			try {
				//get the current time
				Calendar now = Calendar.getInstance();
				
				//look, what to do for every stream
				for(int i=0; i<schedulVector.capacity(); i++) {
					//get schedulJob for better code
					SchedulJob job = schedulVector.get(i);
					
					//should a stream started? Start if:
					// - is enabled
					// - isn't already started by schedulemanager  
					// - the start time is in the past
					// - the stop time is in the future
					if(job.isJobenabled() && !job.getstatus() && job.getStartTime() < now.getTimeInMillis()
							&& job.getStopTime() > now.getTimeInMillis()) {
						
						//  look for the to recording stream
						Stream stream = mainGui.getControlStream().getStreamByID(
								job.getStreamID());
						
						//	start recording
						mainGui.startRippingUnselected(stream);
						
						//  show a message in systrayicon
						String message = trans.getString("conSched.StreamStarted")+" "+stream.name;
						mainGui.showMessageInTray(message);
						
						//  set the status to recording
						job.setStatus(true);
					}
					
					//should a stream stopped? Stop if:
					// - the stop time is in the past
					// - the stream is started by the schedule manager
					if(job.getStopTime() <= now.getTimeInMillis() && job.getstatus())
					{
						// stop ripping stream
						mainGui.stopRippingUnselected(mainGui.getControlStream()
								.getStreamByID(job.getStreamID()));
						
						// should schedulejob updated?
						calculateNextJobTime(job,false);
						
						//the Gui_schedulmanager should update it't table
						updateView = true;
						
						//save the vector with all schedule jobs
						saveScheduleVector();
					
					}
					
					//should schedulejob deleted? //only if:
					//- record once;
					//- stop time is in the past
					if(job.isOnceJob() && job.getStopTime() <= now.getTimeInMillis()|| 
							(job.isOnceJob() && job.getstatus() && !mainGui.getControlStream().getStreamByID(job.getStreamID()).getStatus()))
					{
						//remove from job vector
						removeJobFromVector(job.getSchedulID());
						
						//show a message in systrayicon
						String message = trans.getString("conSched.StreamStopped")+" "+
									mainGui.getControlStream().getStreamByID(job.getStreamID()).name;
						mainGui.showMessageInTray(message);
						
						//save all scheduljobs in xml file
						saveScheduleVector();
						
						//the Gui_schedulmanager should update it't table
						updateView = true;
						
					} 
					//either:
					//if the recording time is in the past and the job
					//is an daily or weekly job -> update to next time
					//
					//or:
					//the job is a daily or weekly job and is stop. This means
					//the the stream was already started, but the user stopped it
					else if((!job.isOnceJob() && job.getStopTime() <= now.getTimeInMillis()) || 
							(!job.isOnceJob() && !mainGui.getControlStream().getStreamByID(job.getStreamID()).getStatus())) {
						calculateNextJobTime(job,true);
						
						//the Gui_schedulmanager should update it's table
						updateView = true;
						
						//save the vector with all schedule jobs to harddisk
						saveScheduleVector();
					}
				}
					
				
				//good night
				Thread.sleep(3000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stop this Thread
	 */
	public void stopScheduler() {
		stop = true;
	}
	
	/**
	 * add an schedul job to the end of the vector
	 * witch contains all schedul jobs
	 * @param job
	 */
	public void addToSchedulVector(SchedulJob job) {
		schedulVector.add(job);
	}
	
	/**
	 * loads all scheduljobs on start from xml-file
	 * and save it into the vector witch contains
	 * all jobs
	 */
	public void loadScheduleJobsOnStart() {
		String loadPath =  new Control_GetPath().getStreamRipStarPath();
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser;
			parser = factory.createXMLStreamReader( new FileInputStream(loadPath+"/Scheduls.xml" ) );
			while ( parser.hasNext() ) { 
	 
				switch ( parser.getEventType() ) { 
					case XMLStreamConstants.START_DOCUMENT: 
						System.out.println( "Loading file Scheduls.xml" ); 
						break; 
				 
				    case XMLStreamConstants.END_DOCUMENT: 
				    	System.out.println( "End of read settings " ); 
				    	parser.close(); 
				    	break; 
				 
				    case XMLStreamConstants.START_ELEMENT:
						int streamID = 0;
						int schedulID = 0;
						long startTime = 0;
						long stopTime = 0;
						int howOftenID = 0;
						boolean enableJob = true;
						String comment = "";
				    	
				    	for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
				    		if(parser.getAttributeLocalName( i ).equals("lastID")) {
				    			SchedulJob.lastID = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if(parser.getAttributeLocalName( i ).equals("streamID")) {
				    			streamID = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("schedulID")) {
				    			schedulID = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("startTime")) {
				    			startTime = Long.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("stopTime")) {
				    			stopTime = Long.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("howOftenID")) {
				    			howOftenID = Integer.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("enableJob")) {
				    			enableJob = Boolean.valueOf(parser.getAttributeValue(i));
				    		} else if (parser.getAttributeLocalName( i ).equals("")) {
				    			comment = parser.getAttributeValue(i);
				    		}
				    	}
				    	if(schedulID > 0) {
				    		SchedulJob job = new SchedulJob(schedulID,streamID,startTime,
				    				stopTime,howOftenID,enableJob,comment);
				    		schedulVector.add(job);
				    	}
				    	break; 
				 
				    default: 
				    	break; 
				  }
				parser.next(); 
			}

		} catch (FileNotFoundException e) {
			System.err.println("No configuartion file found: Scheduls.xml");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the Vector containing all scheduljobs
	 */
	public Vector<SchedulJob>getScheduleVector() {
		return schedulVector;
	}
	
	/**
	 * looks in the scheduljob vector for in job by an ID
	 * @param id: the ID of the scheduljob in xml-file
	 * @return the scheduljob with the ID in xml-file
	 */
	public SchedulJob getSchedulJobByID(int id) {
		for(int i=0; i<schedulVector.capacity();i++) {
			if(schedulVector.get(i).getSchedulID() == id) {
				return schedulVector.get(i);
			}
		}
		return null;
	}
	
	/**
	 * removes an scheduljob from Vector. 
	 * @param id: The ID in xml-file from the scheduljob
	 */
	public void removeJobFromVector(int id) {
		for(int i=0; i < schedulVector.capacity() ;i++) {
			if(schedulVector.get(i).getSchedulID()==id) {
				schedulVector.remove(i);
				schedulVector.trimToSize();
				System.gc();
				break;
			}
		}
	}
	
	/**
	 * Calculates the next start- and endtime for an daily and weekly
	 * schedul job. 
	 * 
	 * @param job: The schedul job for witch the new
	 * time should calculate.
	 */
	public void calculateNextJobTime(SchedulJob job, Boolean forceUpdate) {
		
		long nowMills = Calendar.getInstance().getTimeInMillis();
		long oldStartTime = job.getStartTime();
		long timeDiff = job.getStopTime()-job.getStartTime();
		boolean older = false;
		boolean newer = false;
		
		//only update if it should record more than once
		if(!job.isOnceJob()) {
			if(job.getStopTime() < nowMills) {
				forceUpdate = true;
			}
			
			//is daily job
			if(job.getJobCount() == 0) {
				if(oldStartTime <= nowMills && forceUpdate) {
					older = false;
				
					while(!(!older && newer)) {
						oldStartTime += 1000*60*60*24; 	// + one day in milliseconds
						older = newer;
						if(oldStartTime <= nowMills) {
							newer = false;
						} else {
							newer = true;
						}
					}
					//set the new start time
					job.setStartTime(oldStartTime);
					//set the new stop time
					job.setStopTime(oldStartTime+timeDiff);
				}
				else {
					//no need for an update
				}
			}
			
			//is weekly job
			else if (job.getJobCount() == 1) {
				if(oldStartTime <= nowMills && forceUpdate) {
					older = false;
				
					while(!(!older && newer)) {
						oldStartTime += 1000*60*60*24*7; 	//one week in milliseconds
						older = newer;
						if(oldStartTime <= nowMills) {
							newer = false;
						} else {
							newer = true;
						}
					}
					//set the new start time
					job.setStartTime(oldStartTime);
					//set the new stop time
					job.setStopTime(oldStartTime+timeDiff);
				}
				else {
					//no need for an update
				}
			}
		}
	}
	
	public void setNextTimeForAllJobs() {
		for(int i=0; i < schedulVector.capacity() ;i++) {
			calculateNextJobTime(schedulVector.get(i), false);
		}
	}
	
	/**
	 * set an new status to the update
	 * @param update: true, if the ui should updated 
	 */
	public void setUpdateView(Boolean update) {
		updateView = update;
	}
	
	/**
	 * 
	 * @return: true, if the table should be updated
	 */
	public Boolean shouldUpdate() {
		return updateView;
	}
	
	/**
	 * looks if a schedul job still exist.
	 *@param ID: the job ID in xml-file and schedulvector
	 *@return: true if the job exist, else
	 */
	public Boolean jobStillExist(int ID) {
		for(int i=0; i < schedulVector.capacity() ;i++) {
			if(schedulVector.get(i).getSchedulID() == ID) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Search in all available schedules for an job with
	 * the stream ID and deletes all off them
	 * @param id: the stream id
	 */
	public void deleteAllJobsFromStream(int id) {
		for(int i=schedulVector.capacity()-1; i >= 0 ;i--) {
			if(schedulVector.get(i).getStreamID() == id) {
				schedulVector.remove(i);
			}
		}
		schedulVector.trimToSize();
		System.gc();
	}
	
	/**
	 * saves all jobs in the vector 
	 */
	public void saveScheduleVector() {
		String savePath =  new Control_GetPath().getStreamRipStarPath();
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance(); 
		
		try {
			XMLEventWriter writer = outputFactory.createXMLEventWriter(
					new FileOutputStream(savePath+"/Scheduls.xml" ) );
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			
			//header for the file
			XMLEvent header = eventFactory.createStartDocument();
			XMLEvent startRootSettings = eventFactory.createStartElement( "", "", "SchdedulJobs" );
			XMLEvent lastID = eventFactory.createAttribute( "lastID",  String.valueOf( SchedulJob.lastID));
			writer.add( header ); 
			writer.add( startRootSettings );
			writer.add( lastID );
			
			for(int i = 0; i < schedulVector.capacity();i++) {
				XMLEvent Job = eventFactory.createStartElement( "", "", "SchedulJob" );
				XMLEvent streamID = eventFactory.createAttribute( "streamID",  String.valueOf( schedulVector.get(i).getStreamID())); 
				XMLEvent schedulID = eventFactory.createAttribute( "schedulID",  String.valueOf( schedulVector.get(i).getSchedulID())); 
				XMLEvent startTime = eventFactory.createAttribute( "startTime",  String.valueOf( schedulVector.get(i).getStartTime())); 
				XMLEvent stopTime = eventFactory.createAttribute( "stopTime",  String.valueOf( schedulVector.get(i).getStopTime())); 
				XMLEvent howOftenID = eventFactory.createAttribute( "howOftenID",  String.valueOf( schedulVector.get(i).getJobCount())); 
				XMLEvent enableJob = eventFactory.createAttribute( "enableJob",  String.valueOf( schedulVector.get(i).isJobenabled())); 
				XMLEvent comment = eventFactory.createAttribute( "comment",  String.valueOf( schedulVector.get(i).getComment()));
				writer.add( Job ); 
				writer.add( streamID ); 
				writer.add( schedulID ); 
				writer.add( startTime ); 
				writer.add( stopTime ); 
				writer.add( howOftenID ); 
				writer.add( enableJob ); 
				writer.add( comment ); 
			}
			
			XMLEvent endRoot = eventFactory.createEndElement( "", "", "Settings" ); 
			XMLEvent endDocument = eventFactory.createEndDocument();

			writer.add( endRoot ); 
			writer.add( endDocument ); 
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} 
	}
}	

