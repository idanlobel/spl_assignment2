package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {


    private int start_index; // not sure wtf iss this
    private Data data;// not sure why we need , in the sheet it is shown that we need

    public DataBatch(Data intel,int star_index)
    {
        this.data=intel;
        this.start_index=star_index;
    }



//	    public int get_index_of_last_processed()
//	    {
//	    	return this.start_index+1000;
//	    }



}