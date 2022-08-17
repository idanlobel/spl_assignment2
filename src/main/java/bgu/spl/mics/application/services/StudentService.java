package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcasts.ConferencePublicationBroadcasts;
import bgu.spl.mics.application.broadcasts.TerminateBroadcast;
import bgu.spl.mics.application.broadcasts.TickBroadcast;
import bgu.spl.mics.application.events.PublishResultsEvent;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.events.TrainModelEvent;
import bgu.spl.mics.application.objects.Student;

import java.util.List;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Student student;
    public StudentService(String name,Student student) {
        super(name);

        this.student=student;
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        messageBus.register(this);

        Callback TerminateBroadcastcallback=(t)->{terminate();};

        subscribeBroadcast(TerminateBroadcast.class, TerminateBroadcastcallback);

        Callback tickbroadcasttick=(ee)->{};
        super.messages.put(TickBroadcast.class, tickbroadcasttick);
        this.subscribeBroadcast(TickBroadcast.class, tickbroadcasttick);

        while(student.check_if_more_models())
        {
            //sendEvent(new TrainModelEvent(student.getcurrentdata()));
            // sendEvent(new TestModelEvent(student.getcurrentdata(),student.getStatus()));
            sendEvent(new TrainModelEvent(student.getcurrentModel()));
            sendEvent(new TestModelEvent(student.getcurrentModel(),student.getStatus()));
            student.increase_index();
            sendEvent(new PublishResultEvent(this.student));

        }


        Callback ConferencePublicationCallback=(ee)->{List listofgoodesult=super.getGoodResults();
            student.increase_papersRead(listofgoodesult.size());

        };
        super.messages.put(ConferencePublicationBroadcast.class, ConferencePublicationCallback);

        subscribeBroadcast(ConferencePublicationBroadcast.class,ConferencePublicationCallback);



    }

    public Student getstudent()
    {
        return this.student;
    }


}