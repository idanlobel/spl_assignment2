package bgu.spl.mics.application.events;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class TestModelEvent extends Model {
    private Student.Degree degree;
    public TestModelEvent(String name, Data d,Student.Degree degree) {
        super(d,name);
        this.degree=degree;
    }

    public Student.Degree getDegree(){
        return degree;
    }
}
