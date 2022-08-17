package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultsEvent implements Event {
    private Model model;
    private Student student;
    public PublishResultsEvent(Student s){
        this.student=s;
    }
    public Model getModel() {
        return this.model;
    }
}
