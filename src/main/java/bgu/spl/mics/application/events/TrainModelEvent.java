package bgu.spl.mics.application.events;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent extends Model {
    public TrainModelEvent(Data d,String name) {
        super(d,name);
    }
}
