package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcasts.TerminateBroadcast;
import bgu.spl.mics.application.broadcasts.TickBroadcast;

import java.util.Timer;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	long duration;
	long finish;
	long counter;
	long tickTime;
	Timer timer;
	public TimeService(String name, long tickTime,long finish) {
		super(name);
		duration=tickTime;
		this.finish=finish;
		this.tickTime=tickTime;
		counter=0;
		timer=new Timer();
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		int time = 0;
		while (time < finish/tickTime) {
			try {
				Thread.sleep(tickTime);// we give the thread to sleep time=speed
				time = time + 1;// the time that i was have and the current time//fix it and use atomic integer
				sendBroadcast(new TickBroadcast());// the tickBrodcast give it the time
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		sendBroadcast(new TerminateBroadcast());
		terminate();
	}

}
