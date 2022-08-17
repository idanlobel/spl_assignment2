package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.TrainedModel;

import java.util.List;

public class StudentOutput {
    private String name;
    private String department;
    private String status;
    private int publications;
    private int papersRead;
    private List<TrainedModel> trainedModels;

    public StudentOutput(String name,String department,String status,int publications,int papersRead,List<TrainedModel> trainedModels) {
        this.name=name;
        this.department=department;
        this.status=status;
        this.publications=publications;
        this.papersRead=papersRead;
        this.trainedModels=trainedModels;
    }
    public String getName() {
        return name;
    }

}
