package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.events.TrainModelEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private  List<Model> listofmodels;
    private int current_model_index;
    private List<TrainModelEvent> trainedModels;

    public Student(String name,String department,String deg)
    {
        current_model_index=0;
        this.name=name;
        this.department=department;
        if(deg.equals("MSc") | deg.equals("MSC") | deg.equals("MsC"))

        {
            this.status=Degree.MSc;
        }
        else
        {
            this.status=Degree.PhD;
        }
        listofmodels=new ArrayList<Model>();
    }



    public void add_model(Model model) {
        this.listofmodels.add(model);

    }

    public Student.Degree getStatus()
    {
        return this.status;
    }

    public void increase_publication(int size)
    {
        this.publications=this.publications+size;
    }
    public void increase_reads()
    {
        this.publications=this.publications+1;
    }

    public void increase_papersRead(int size) {
        this.papersRead=this.papersRead+size;
    }

    public Data getcurrentdata()
    {
        Data currentdata=this.listofmodels.get(current_model_index).getdata();

        return currentdata;
    }

    public Model getcurrentModel()
    {
        Model currentmod=this.listofmodels.get(current_model_index);

        return currentmod;
    }

    public void increase_index()
    {
        current_model_index=current_model_index+1;
    }

    public boolean check_if_more_models()
    {
        return this.current_model_index<this.listofmodels.size()-1;
    }



    public int getPublications() {
        return this.publications;
    }



    public String getName() {

        return this.name;
    }



    public int getPapersRead() {
        // TODO Auto-generated method stub
        return this.papersRead;
    }



    public String getDepartment() {
        // TODO Auto-generated method stub
        return this.department;
    }



    public String statusToString() {
        if(this.status.equals(Degree.MSc))
        {
            return "MSc";
        }
        else
        {
            return "PHD";
        }
    }


    public void Update_trainedModels(TrainModelEvent m)
    {
        this.trainedModels.add(m);
    }
    public List<TrainModelEvent> getTrainedModels() {
        return this.trainedModels;
    }

}