package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcasts.TerminateBroadcast;
import bgu.spl.mics.application.broadcasts.TickBroadcast;
import bgu.spl.mics.application.events.DataPreProcessEvent;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */


public class CPUService extends MicroService {

    private CPU cpu;
    private long timepassed;
    public CPUService(String name,CPU cpu) {
        super(name);
        this.cpu=cpu;
        this.timepassed=0;
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        super.messageBus.register(this);
        subscribeBroadcast(TerminateBroadcast.class, (t) -> terminate());
        Callback tickCallBack=(e)->{

        };
        subscribeBroadcast(TickBroadcast.class,tickCallBack);
        messages.put(TickBroadcast.class,tickCallBack);
        Callback DataPreProcesscallback=(e)->{
            this.cpu.cluster.send_to_cpu();
            while(messageBus.getSubscribeBroadCasts().get(this).isEmpty())
            {
                try {
                    messageBus.getSubscribeBroadCasts().get(this).wait();
                } catch (InterruptedException ee) {
                    // TODO Auto-generated catch block
                    ee.printStackTrace();
                }
            }
            messageBus.getSubscribeBroadCasts().get(this).remove();
            Data.Type type=this.cpu.get_current_databbtach_type();
            long current_batch_total_time = 0;

            if(type==Data.Type.Images)
            {
                current_batch_total_time=((32/this.cpu.get_number_of_cores())*4);

            }
            if(type==Data.Type.Tabular)
            {
                current_batch_total_time=((32/this.cpu.get_number_of_cores())*1);

            }
            if(type==Data.Type.Text)
            {
                current_batch_total_time=((32/this.cpu.get_number_of_cores())*2);

            }
            if(this.timepassed<current_batch_total_time)
            {
                this.timepassed+=messageBus.getTickTime();
            }
            else
            {
                this.timepassed=0;
                this.cpu.proccess();
            }

            if(this.cpu.check_if_completed())
            {
                super.complete((Event<DataBatch>)getFirstMessage(),cpu.proccess());
            }
        };
        subscribeEvent(DataPreProcessEvent.class,DataPreProcesscallback);
        messages.put(DataPreProcessEvent.class,DataPreProcesscallback);
    }


}