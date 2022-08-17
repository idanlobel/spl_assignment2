package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcasts.TickBroadcast;
import bgu.spl.mics.application.events.DataPreProcessEvent;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.events.TrainModelEvent;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.broadcasts.TerminateBroadcast;
/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    private GPU gpu;
    private long timepassed;


    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
        this.timepassed=0;
    }

    @Override
    protected void initialize() {

        messageBus.register(this);

        Callback TerminateBroadcastcallback=(t)->{terminate();};

        subscribeBroadcast(TerminateBrodcast.class, TerminateBroadcastcallback);
        Callback tickbroadcasttick=(ee)->{};
        super.messages.put(TickBroadcast.class, tickbroadcasttick);
        this.subscribeBroadcast(TickBroadcast.class, tickbroadcasttick);


        Callback trainModelCallback =(ee)-> {
            gpu.updateModel((Model)super.getFirstMessage());
            gpu.makebatch();
            gpu.send_to_cluster();
            Future future=sendEvent(new DataPreProcessEvent(gpu.get_current_DataBatch()));
            while(future.get()==null)
            {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            while(super.get_broadcasts().isEmpty())
            {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ changed here
            if(!super.get_broadcasts().peek().equals(TerminateBrodcast.class))
            {
                super.get_broadcasts().remove();
                long required_ticks=0;
                if(this.gpu.get_type()==GPU.Type.GTX1080)
                {
                    required_ticks=4;
                }
                if(this.gpu.get_type()==GPU.Type.RTX2080)
                {
                    required_ticks=2;
                }
                if(this.gpu.get_type()==GPU.Type.RTX3090)
                {
                    required_ticks=1;
                }
                if(this.timepassed<required_ticks)
                {
                    this.timepassed=this.timepassed+1;
                }
                else
                {
                    this.timepassed=0;
                    this.gpu.Train();
                    if(this.gpu.get_trainedModel()!=null)
                    {
                        super.complete((Event<TrainModelEvent>)super.getFirstMessage(), this.gpu.get_trainedModel());
                    }


                }
            }
            else
            {
                terminate();
            }
        };
        super.messages.put(TrainModelEvent.class, trainModelCallback);
        subscribeEvent(TrainModelEvent.class, trainModelCallback);


        Callback testModelCallback=(ee)->{
            gpu.updateModel((Model) getFirstMessage());
            complete((Model) getFirstMessage(),gpu.testModel());
        };
        super.messages.put(TestModelEvent.class,testModelCallback);
        subscribeEvent(TestModelEvent.class,testModelCallback );

    }
}