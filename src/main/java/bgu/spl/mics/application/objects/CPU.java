package bgu.spl.mics.application.objects;

import java.util.*;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int number_of_cores;
    public Cluster cluster;
    private Queue<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer>> queueofdatabatch_unproccessed;
    private List<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer>> list_proccessed_databatch;
    boolean process;

    public CPU(int cores)
    {
        cluster=Cluster.getInstance();
        this.number_of_cores=cores;
        this.queueofdatabatch_unproccessed=new PriorityQueue<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer>>();
        //this.list_proccessed_databatch=cluster.get_hash();
        this.list_proccessed_databatch=new ArrayList<AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer>>();
        process=false;
    }

    /*
  @PRE:this.process=true && !listofdatabatch.size().isEmpty()
  @POST:this.process=false && listofdatabatch.size().isEmpty()
   */
    public void send_to_cluster(AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer> pair)
    {

        cluster.getInstance().get_from_cpu(pair,this);


    }

    public void get_from_cluster_proccessed(AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>, Integer> pair)// if gpu has full slots of proccessed data
    {
        this.list_proccessed_databatch.add(pair);
    }


    /*
   	@PRE:listofdatabatch.size().isEmpty()
   	@POST:!listofdatabatch.size().isEmpty()
   	 */
    public void get_from_cluster(AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>,Integer> pair)
    {
        this.queueofdatabatch_unproccessed.add(pair);

    }


    public DataBatch proccess()
    {
        AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<DataBatch,Data.Type>,Integer> current_databtach=this.queueofdatabatch_unproccessed.remove();
        this.list_proccessed_databatch.add(current_databtach);
        return current_databtach.getKey().getKey();


    }

    public boolean status()
    {
        return this.process;
    }

    public int waiting_databatches()
    {
        return this.queueofdatabatch_unproccessed.size();
    }

    public int get_number_of_cores()
    {
        return this.number_of_cores;
    }

    public Data.Type get_current_databbtach_type()
    {
        return this.queueofdatabatch_unproccessed.peek().getKey().getValue();
    }

    public boolean check_if_completed()
    {
        return this.cluster.check_if_completed();
    }


    //we proccess databatchs using cpuservices
    //timeservice of cpu
//    updatetick()
//    {
//    	if(current_time-batch_start_time>10)
//    	{
//    		.........
//    	}
//    else
//    {
//    	do_nothing
//    }
//    }

}