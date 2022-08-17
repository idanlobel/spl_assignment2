package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model implements Event {



    private Data data;
    private String name;
    public Model(Data d,String name)
    {
        this.data=d;
        this.name=name;
    }



    public Data getdata()
    {
        return this.data;
    }


    public void UpdateData()
    {
        this.data.proccess_data();
    }




}