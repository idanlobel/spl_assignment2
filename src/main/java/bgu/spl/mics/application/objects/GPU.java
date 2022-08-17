package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.events.TestModelEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    public DataBatch get_current_DataBatch() {
        DataBatch current=this.unproccessed_data.get(current_databatch);
        current_databatch=current_databatch+1;
        return current;
    }

    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

    private static AtomicInteger nextId = new AtomicInteger();


    private Type type;
    private Model model;
    private List<DataBatch> unproccessed_data;
    private List<DataBatch> proccessed_data;
    private List<DataBatch> trained_data;
    private TestModelEvent testModel;
    private int current_databatch;
    private int ID;
    Cluster cluster;

    //we should add an array or list to save proccessed data
    //gpu trains the proccessed date and each time he finishes with training one he marks it as completed
    //for example lets assume we have 100,000 data
    //we make and array of size 100 of databatches
    //we send them to cluster, so it will send them to cpus to proccess them
    //we get from cluster proccessed data and we train that using gpu service
    //once we finished training specefic databtach we mark it as trained and proced
    //once all are finished training we send it back to bus using complete method of messagebus

    public void setModel(Model model){
        this.model=model;
    }

    public GPU(Model model_to_proccess,String type)
    {
        this.model=model_to_proccess;
        cluster=Cluster.getInstance();
        if(type.equals("RTX3090"))
        {
            this.type=Type.RTX3090;
        }
        else
        {
            if(type.equals("RTX2080"))
            {
                this.type=Type.RTX2080;
            }
            else
            {
                this.type=Type.GTX1080;
            }
        }
        this.unproccessed_data=new ArrayList<DataBatch>();
        this.proccessed_data=new ArrayList<DataBatch>();
        this.trained_data=new ArrayList<DataBatch>();
        this.ID=nextId.incrementAndGet();
        this.testModel=null;
    }


    public void send_result()
    {

        Future<Model> result=new Future();
        result.resolve(this.model);



    }

    public void makebatch()
    {
        int peacies=this.model.getdata().size()/1000;
        int index=0;
        while(peacies>0)
        {
            this.unproccessed_data.add(new DataBatch(model.getdata(),index*1000));
            peacies--;
            index++;

        }
    }


    public void send_to_cluster()
    {

        for(DataBatch db:this.unproccessed_data)
        {
            AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer> pair=new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<>(db,this.model.getdata().get_type()),this.ID);
            cluster.getInstance().get_from_gpu(pair);
        }


    }

    public void get_from_cluster(AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer> pair)
    {

        this.proccessed_data.add(pair.getKey().getKey());

    }




    /*
     * @PRE:this.model.getdata().get_processed()==@POS(Tthis.model.getdata().get_processed()-1000)
     */
    public void update_data_proccessed_model()
    {
        this.model.UpdateData();
    }

    public boolean isfull()
    {
        if(this.type==Type.RTX3090)
        {
            if(this.proccessed_data.size()==32)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        if(this.type==Type.RTX2080)
        {
            if(this.proccessed_data.size()==16)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        if(this.type==Type.GTX1080)
        {
            if(this.proccessed_data.size()==8)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;

    }

    public int GetID()
    {
        return this.ID;
    }

    private void train_databatches()
    {
        for(DataBatch db:this.proccessed_data)
        {
            this.proccessed_data.remove(db);
            this.trained_data.add(db);
        }
    }

    private boolean check_if_completed_training()
    {
        return this.trained_data.size()==this.model.getdata().size()/1000;
    }


    public void updateModel(Model m){
        model=m;
    }

    public Model getModel(){
        return model;
    }

    public int get_supposed_proccessed()// how much is supposed to be proccessed data to start training
    {
        return this.model.getdata().size()/1000;
    }
    public String testModel(){
        double r=Math.random();
        if(testModel.getDegree()== Student.Degree.MSc){
            if(r<=0.6)
                return "good";
            return "bad";
        }
        if(r<=0.8)
            return "good";
        return "bad";
    }
    public Type getType(){
        return type;
    }
    public DataBatch Train()
    {
        DataBatch current_databtach=this.proccessed_data.remove(0);
        this.trained_data.add(current_databtach);
        return current_databtach;


    }

    public GPU.Type get_type()
    {
        return this.type;
    }





}