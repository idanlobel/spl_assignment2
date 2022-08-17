package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcasts.PublishConfrenceBroadcast;
import bgu.spl.mics.application.broadcasts.TerminateBroadcast;
import bgu.spl.mics.application.broadcasts.TickBroadcast;
import bgu.spl.mics.application.events.PublishResultsEvent;
import bgu.spl.mics.application.objects.ConfrenceInformation;

import java.util.LinkedList;
import java.util.List;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */public class ConferenceService extends MicroService {
    private ConfrenceInformation confrenceInformation;
    private List<StudentService> studentsWhoWantResults;
    private int clock;
    public ConferenceService(String name,ConfrenceInformation confrenceInformation) {
        super(name);
        this.confrenceInformation=confrenceInformation;
        clock=0;
        studentsWhoWantResults=new LinkedList<>();
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        messageBus.register(this);
        Callback TerminateBroadcastcallback=(t)->{terminate();};

        subscribeBroadcast(TerminateBroadcast.class, TerminateBroadcastcallback);






        Callback tickCallback=(e)->{
            clock=clock+this.getTickTime();
            if(confrenceInformation.getDate()>=clock){
                for(Message m:getMessagesHashMap().get(this)){
                    if(m.getClass()==PublishResultsEvent.class){
                        Callback callback=messages.get(m);
                        callback.call(m);
                    }
                }
            }
            sendBroadcast(new PublishConfrenceBroadcast(getGoodResults()));
            messageBus.unregister(this);
            System.out.println("conference publish");
            // terminate();
        };

        subscribeBroadcast(TickBroadcast.class,tickCallback);
        messages.put(TickBroadcast.class,tickCallback);








        Callback publishResultsCallback=(ee)->{
            if(confrenceInformation.getDate()>=clock) {
//                for(StudentService s:studentsWhoWantResults){
//                    List<TrainModelEvent> list=getGoodResultsByStudent(s);
//                    s.getstudent().increase_publication(super.getGoodResultsByStudent(s).size());
//                    for(TrainModelEvent m:list)
//                    {
//                        s.getstudent().Update_trainedModels((TrainModelEvent) m);
//
//                    }
//                }
            }


        };

        subscribeEvent(PublishResultsEvent.class,publishResultsCallback);
        messages.put(PublishResultsEvent.class,publishResultsCallback);




    }
}