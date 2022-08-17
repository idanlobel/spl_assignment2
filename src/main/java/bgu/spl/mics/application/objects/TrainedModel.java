package bgu.spl.mics.application.objects;


public class TrainedModel {
    private String name;
    private Data data;
    private String status;
    private String results;

    public TrainedModel(String name, Data data, String status, String result) {
        this.name = name;
        this.data = data;
        this.status = status;
        this.results = result;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setData(Data data) {
        this.data = data;
    }
    public void setResults(String results) {
        this.results = results;
    }
    public String getName() {//delet it
        // TODO Auto-generated method stub
        return name;
    }
}

