package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //JsonReader reader = new JsonReader(new FileReader("C:\\Users\\bayan\\Desktop\\okay.json"));
        JsonReader reader = new JsonReader(new FileReader("C:\\Users\\idanl\\OneDrive - post.bgu.ac.il\\סמסטר ג'\\SPL\\assignment2\\example_input.json"));
        reader.beginObject();
        String student_name=null;
        String student_department = null;
        String status = null;
        String model_name=null;
        String model_type=null;
        int model_size = 0;
        String conf_name=null;
        int conf_date = 0;
        int ticktime = 0;
        int duration = 0;
        String gpu_type=null;
        int cpu_cores=0;

        List<Student> listofstudents=new ArrayList<Student>();
        List<Model> listofmodels=new ArrayList<Model>();
        List<GPU> listofgpu=new ArrayList<GPU>();
        List<CPU> listofcpu=new ArrayList<CPU>();
        List<ConfrenceInformation> listofconf=new ArrayList<ConfrenceInformation>();
        List<StudentService> listofstudentserv=new ArrayList<StudentService>();
        List<GPUService> listofgpuserv=new ArrayList<GPUService>();
        List<CPUService> listofcpuserv=new ArrayList<CPUService>();
        List<ConferenceService> listofconfserv=new ArrayList<ConferenceService>();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "Students":
                {
                    reader.beginArray();//array of students
                    while(reader.hasNext())
                    {
                        reader.beginObject();//creating new student
                        while(reader.hasNext())
                        {
                            switch (reader.nextName())
                            {
                                case "name":
                                    student_name=reader.nextString();
                                    break;
                                case "department":
                                    student_department=reader.nextString();
                                    break;
                                case "status":
                                    status=reader.nextString();
                                    break;
                                case "models":
                                {
                                    listofstudents.add(new Student(student_name,student_department,status));
                                    reader.beginArray();//array of modesl
                                    while(reader.hasNext())
                                    {
                                        reader.beginObject();//creating new model
                                        while(reader.hasNext())
                                        {
                                            switch (reader.nextName())
                                            {
                                                case "name":
                                                    model_name=reader.nextString();
                                                    break;
                                                case "type":
                                                    model_type=reader.nextString();
                                                    break;
                                                case "size":
                                                    model_size=reader.nextInt();
                                                    break;
                                            }
                                        }

                                        listofstudents.get(listofstudents.size()-1).add_model(new Model(new Data(model_type,model_size),model_name));
                                        reader.endObject();// finished model
                                    }
                                    reader.endArray();//array of models

                                }
                            }
                        }
                        reader.endObject();//fisnihed student
                    }
                    reader.endArray();//finsihed array of students
                    break;
                }
                case "GPUS":
                {
                    reader.beginArray();

                    while(reader.hasNext())
                    {

                        switch (reader.nextString())
                        {
                            case "RTX3090":
                                gpu_type=reader.nextString();
                                listofgpu.add(new GPU(null,gpu_type));
                                break;

                            case "RTX2080":
                                gpu_type=reader.nextString();
                                listofgpu.add(new GPU(null,gpu_type));
                                break;

                            case "GTX1080":
                                gpu_type=reader.nextString();
                                listofgpu.add(new GPU(null,gpu_type));
                                break;
                        }
                    }
                    reader.endArray();
                    break;
                }
                case "CPUS":
                {
                    reader.beginArray();

                    while(reader.hasNext())
                    {
                        cpu_cores=reader.nextInt();
                        listofcpu.add(new CPU(cpu_cores));
                    }
                    reader.endArray();
                    break;
                }

                case "Conferences":
                {
                    reader.beginArray();
                    while(reader.hasNext())
                    {
                        reader.beginObject();//creating new confrence

                        while(reader.hasNext())
                        {
                            switch (reader.nextName())
                            {
                                case "name":
                                    conf_name=reader.nextString();
                                    break;
                                case "date":
                                    conf_date=reader.nextInt();
                                    break;
                            }
                        }
                        listofconf.add(new ConfrenceInformation(conf_name, conf_date));
                        reader.endObject();
                    }
                    reader.endArray();
                    break;
                }
                case "TickTime":
                    ticktime=reader.nextInt();
                    break;
                case "Duration":
                    duration=reader.nextInt();
                    break;
            }
        }

        TimeService t= new TimeService ("timer",ticktime,duration);
        Thread timeThread=new Thread(t);
        timeThread.start();
        timeThread.join();
        for(GPU gpu:listofgpu)
        {
            listofgpuserv.add(new GPUService("gpu",gpu));
        }

        for(CPU cpu:listofcpu)
        {
            listofcpuserv.add(new CPUService("cpu",cpu));

        }

        for(ConfrenceInformation conf:listofconf)
        {
            listofconfserv.add(new ConferenceService("cpu",conf));

        }

        for(Student s:listofstudents)
        {
            listofstudentserv.add(new StudentService("student", s));
        }
        for(StudentService studserv:listofstudentserv)
        {
            Thread studentThread=new Thread(studserv);
            studentThread.start();
        }
        for(GPUService gpuserv:listofgpuserv)
        {

            Thread gpuThread=new Thread(gpuserv);
            gpuThread.start();
        }

        for(CPUService cpuserv:listofcpuserv)
        {
            Thread cpuThread=new Thread(cpuserv);
            cpuThread.start();
        }



        for(ConferenceService confserv:listofconfserv)
        {
            Thread conferenceThread=new Thread(confserv);
            conferenceThread.run();
        }
        for (int i = 0; i < listofstudents.size(); i++) {
            Student student = listofstudents.get(i);
            output.getInstance()
                    .addStudent(new StudentOutput(student.getName(), student.getDepartment(), student.statusToString(),
                            student.getPublications(), student.getPapersRead(), student.getTrainedModels()));
        }
        try(Writer writer = new FileWriter("output.json")){
            gson.toJson(output.getInstance(), writer);}
//        Set<Thread> threads = Thread.getAllStackTraces().keySet();
//        for(Thread ts:threads){
//            System.out.println(ts.getName());
//        }


    }
}