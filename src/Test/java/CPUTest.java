import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.Model;

class CPUTest {

    private static CPU cpu;
    private Model model;
    private Data d;
    private DataBatch db;

   // @Before
//    public void setup() throws Exception{
//        d=new Data(null,10000);
//        //model=new Model(d);
//        db=new DataBatch(d,0);
//        cpu=new CPU(4);
//    }
//
//    @Test
//    public void test_send_to_cluster()
//    {
//        cpu=new CPU(4);
//        cpu.send_to_cluster();
//        assertEquals(false,cpu.status());
//    }
//
//    @Test
//    public void test_get_from_cluster()
//    {
//        cpu=new CPU(4);
//        db=new DataBatch(d,0);
//        cpu.get_from_cluster(db);
//        assertEquals(cpu.getlist().contains(db),true);
//
//    }
//
//    @Test
//    public void test_process()
//    {
//        cpu=new CPU(4);
//        cpu.proccess();
//        assertEquals(true,cpu.status());
//
//    }


}
