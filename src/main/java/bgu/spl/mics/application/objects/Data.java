
package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {

    /**
     * Enum representing the Data type.
     */
    public enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;
    private int size;// might be 100000 and 1000000 anything.....



    public Data(String t,int size)
    {
        this.processed=0;
        if(t.equals("Images") | t.equals("images"))
        {
            this.type=Type.Images;
        }
        else
        {
            if(t.equals("Text") | t.equals("text"))
            {
                this.type=Type.Text;
            }
            else
            {
                this.type=Type.Tabular;
            }
        }
        this.size=size;
    }


//    public int get_index_of_notprocessed() {
//    	return this.processed;
//    }


    public void proccess_data()
    {
        processed=processed+1000;
    }

    public int get_processed() {
        return this.processed;
    }

    public int size() {
        return this.size;
    }

    public Type get_type() {
        return type;
    }


}