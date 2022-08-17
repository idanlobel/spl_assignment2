package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DataBatch;

public class DataPreProcessEvent implements Event {
    private DataBatch dataBatch;
    public DataPreProcessEvent(DataBatch d) {
        dataBatch=d;
    }
}
