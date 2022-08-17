package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.ConfrenceInformation;

import java.util.LinkedList;
import java.util.List;


public class output {
    private static output instance = null;
    private List<StudentOutput> Students;
    private List<ConfrenceInformation> conferences;
    private int cpuTimeUsed;
    private int gpuTimeUsed;
    private int batchesProcessed;

    public static synchronized output getInstance() {
        if (instance == null)
            instance = new output();
        return instance;
    }
    public output() {
        Students=new LinkedList<>();
        conferences=new LinkedList<>();
    }
    public void incBatchesProcessed() {
        batchesProcessed = batchesProcessed+1;
    }
    public void addConference(ConfrenceInformation conference) {
        this.conferences.add(conference);
    }
    public void addCpuTimeUsed(int num) {
        cpuTimeUsed = cpuTimeUsed+num;
    }
    public void addGpuTimeUsed(int num) {
        gpuTimeUsed = gpuTimeUsed+num;
    }
    public void addStudent(StudentOutput student) {
        Students.add(student);
    }
    public int getBatchesProcessed() {
        return batchesProcessed;
    }
}

