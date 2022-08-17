package bgu.spl.mics;

import bgu.spl.mics.application.objects.ConfrenceInformation;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	/**@INV:microServiceQueueHashMap.size>=0
	 */
	private ConcurrentHashMap<MicroService,Queue<Class<? extends Event>>> subscribeEvents;
	private ConcurrentHashMap<MicroService,Queue<Class<? extends Broadcast>>> subscribeBroadCasts;
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> messagesHashMap;
	private ConcurrentHashMap<Event,Future> eventFutureHashMap;
	private static MessageBusImpl instance=new MessageBusImpl();
	private int tickTime;
	private ConfrenceInformation confrenceInformation;
	private MessageBusImpl() {
		subscribeEvents=new ConcurrentHashMap<>();
		subscribeBroadCasts=new ConcurrentHashMap<>();
		messagesHashMap=new ConcurrentHashMap<>();
		eventFutureHashMap=new ConcurrentHashMap<>();
	}
	public static MessageBusImpl getInstance(){
		return instance;
	}
	/**
	@PRE:subscribeEvents.get(m).contains(type)==false
	@POST:subscribeEvents.get(m).contains(type)==true
	subscribeEvents.get(m).at(subscribeEvents.get(m).size()-1)==type
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub
		synchronized (subscribeEvents){
			if(subscribeEvents.get(m)==null){
				Queue<Class<? extends Event>> queue=new ArrayDeque<>();
				subscribeEvents.remove(m);
				subscribeEvents.put(m,queue);
			}
		}
		synchronized (subscribeEvents.get(m)) {
		if(!subscribeEvents.get(m).contains(type)) {
				subscribeEvents.get(m).offer(type);
			}
		}
	}
	/**
	@PRE:subscribeBroadcats.get(m).contains(type)==false
	@POST:subscribeBroadcats.get(m).contains(type)==true&&
	subscribeBroadcats.get(m).at(subscribeBroadcats.get(m).size()-1)==type
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized  (subscribeBroadCasts) {
			if (subscribeBroadCasts.get(m) == null) {
				Queue<Class<? extends Broadcast>> queue = new ArrayDeque<>();
				subscribeBroadCasts.remove(m);
				subscribeBroadCasts.put(m, queue);
			}
			if (!subscribeBroadCasts.get(m).contains(type)) {
				subscribeBroadCasts.get(m).add(type);
			}
		}
	}
	/**
	@PRE:messagesHashMap.get(microService).contains(e)=true&&
	eventFutureHashMap.get(e).get()==null
	@POST:eventFutureHashMap.get(e).get()=result
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub
//		for (MicroService microService: messagesHashMap.keySet()){
//			if(messagesHashMap.get(microService).contains(e)) {
//				microService.complete(e, result);
//				Future<T> future=eventFutureHashMap.get(e);
//				future.resolve(result);
//			}
//		}
		synchronized (eventFutureHashMap.get(e)) {
			while (eventFutureHashMap.get(e) == null) {//while the future of the event is set to null, so the event is not completed
				try {
					eventFutureHashMap.get(e).wait();
				}
				catch (InterruptedException exception){
					exception.printStackTrace();
				}
			}
		}
		eventFutureHashMap.get(e).resolve(result);
		notifyAll();
	}
	/**@PRE:for (MicroService microService: subscribeBroadcast.keySet())
			if(subscribeBroadcast.get(microService).contains(b.getClass()))
					messagesHashMap.get(microService).contains(b)==false
		@POST:
		for (MicroService microService: subscribeBroadcast.keySet())
			if(subscribeBroadcast.get(microService).contains(b.getClass()))
					messagesHashMap.get(microService).contains(b)==true
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		if(subscribeBroadCasts.get(b)==null||subscribeBroadCasts.get(b).isEmpty())
			return;
		for (MicroService microService: subscribeBroadCasts.keySet()) {
			synchronized (subscribeBroadCasts.get(microService)) {
				if (subscribeBroadCasts.get(microService).contains(b.getClass()))
					synchronized (messagesHashMap.get(microService)) {
						messagesHashMap.get(microService).offer(b);
						notifyAll();
					}
			}
		}
	}
	/**@PRE: for (MicroService microService: subscribeEvents.keySet())
			if(subscribeEvents.get(microService).contains(e.getClass()))
					messagesHashMap.get(microService).contains(e)==false
		@POST:(for (MicroService microService: subscribeEvents.keySet())
			if(subscribeEvents.get(microService).contains(e.getClass()))
					messagesHashMap.get(microService).contains(e)==true) &&  eventFutureHashMap.get(e)==@return
	 */
	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		Class eventType=e.getClass();
			if(subscribeEvents.get(eventType)==null)
				return null;
			if(subscribeEvents.get(eventType).isEmpty()) {
				Future<T> future=new Future<>();
				synchronized (eventFutureHashMap) {
					eventFutureHashMap.put(e, future);
					notifyAll();
				}
				//implement sending the event to the appropriate gpu
				return future;
			}

			for (MicroService m : subscribeEvents.keySet()) {
				synchronized ((subscribeEvents.get(m))) {
					if (subscribeEvents.get(m).peek() == e.getClass()) {
						synchronized (messagesHashMap.get(m)) {
							messagesHashMap.get(m).offer(e);
						}
						subscribeEvents.get(m).offer(subscribeEvents.get(m).poll());
					}
				}
			}
			Future<T> future=new Future<>();
			synchronized (eventFutureHashMap){
				eventFutureHashMap.put(e,future);
				eventFutureHashMap.notifyAll();
			}
			return future;
	}
	/**@PRE:messagesHashMap.keySet().contains(m)==false && m!=null
	@POST:messageQueue==null&messagesHashMap.keySet().contains(m)==true
	 */
	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub
		BlockingQueue<Message> messageQueue=new LinkedBlockingQueue<>();
		synchronized (messagesHashMap) {
			messagesHashMap.put(m, messageQueue);
		}
		Queue<Class<?extends Event>> eventsQueue=new ArrayDeque<>();
		synchronized (subscribeEvents){
			subscribeEvents.put(m,eventsQueue);
		}
		Queue<Class<?extends Broadcast>> broadcastsQueue=new ArrayDeque<>();
		synchronized (subscribeBroadCasts){
			subscribeBroadCasts.put(m,broadcastsQueue);
		}
	}
	/**@PRE:messagesHashMap.keySet().contains(m)==true && m!=null
	@POST:messagesHashMap.keySet().contains(m)==false
	 */
	@Override
	public void unregister(MicroService m){
		// TODO Auto-generated method stub
		synchronized (messagesHashMap) {
			try {
				messagesHashMap.remove(m);
			}
			catch (Exception e) {
				if (!messagesHashMap.containsKey(m))
					System.out.println(e.getMessage());
				else
					System.out.println("Error in unregister microservice");
			}
		}
		synchronized (subscribeEvents) {
			try {
				subscribeEvents.remove(m);
			}
			catch (Exception e) {
				if (!subscribeEvents.containsKey(m))
					System.out.println(e.getMessage());
				else
					System.out.println("Error in unregister microservice");
			}
		}
		synchronized (subscribeBroadCasts) {
			try {
				subscribeBroadCasts.remove(m);
			}
			catch (Exception e) {
				if (!subscribeBroadCasts.containsKey(m))
					System.out.println(e.getMessage());
				else
					System.out.println("Error in unregister microservice");
			}
		}
	}
	/**
	 *
		@PRE:messagesHashMap.keyset().contains(m)==true&&messagesHashMap.get(m).isEmpty()==false
		@POST:messagesHashMap.get(m).peek()==@return
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException,IllegalStateException{
		// TODO Auto-generated method stub
		Message messageToReturn=null;
		if(messagesHashMap.get(m)==null)
			throw new IllegalStateException(m.getName()+" is not registered");
		return messagesHashMap.get(m).poll();
	}
	/**public HashMap<MicroService, Queue<Class<? extends Message>>> getMicroServiceQueueHashMap() {
		return messagesHashMap;
	}*/
	public  ConcurrentHashMap<Event,Future> getEventFutureHashMape(){
		return eventFutureHashMap;
	}
	public ConcurrentHashMap<MicroService,Queue<Class<? extends Broadcast>>> getSubscribeBroadCasts(){
		return subscribeBroadCasts;
	}
	public ConcurrentHashMap<MicroService,Queue<Class<? extends Event>>> getSubscribeEvents(){
		return subscribeEvents;
	}
	public ConcurrentHashMap<MicroService,BlockingQueue<Message>> getMessagesHashMap(){
		return messagesHashMap;
	}
	public int getTickTime(){
		return tickTime;
	}
	public void setTickTime(int tickTime){
		this.tickTime=tickTime;
	}

}
