
public class MessageBusImplTest {
//    private static MessageBusImpl messageBus;
//    private ExampleEvent event;
//    private ExampleBroadcast broadcast;
//    private ConferenceService conferenceService;
//    private CPUService cpuService;
//    private GPUService gpuService;
//    private StudentService studentService;
//    private TimeService timeService;
//    private String result;
//    @Before
//    public void setUp() throws Exception {
//        messageBus=MessageBusImpl.getInstance();
//        conferenceService=new ConferenceService("conference");
//        cpuService=new CPUService("gpu");
//        gpuService=new GPUService("gpu");
//        studentService=new StudentService("student1");
//        timeService=new TimeService("time");
//        event=new ExampleEvent("student 1");
//        broadcast=new ExampleBroadcast("gpu");
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void testSubscribeEvent() {
//        messageBus.subscribeEvent(event.getClass(),gpuService);
//        assertEquals(true,messageBus.getMessagesHashMap().get(gpuService).contains(event));
//        messageBus.subscribeEvent(event.getClass(),cpuService);
//        assertEquals(true,messageBus.getMessagesHashMap().get(cpuService).contains(event));
//    }
//
//    @Test
//    public void testSubscribeBroadcast() {
//        messageBus.sendBroadcast(broadcast);
//        assertEquals(true,messageBus.getMessagesHashMap().get(gpuService));
//        assertEquals(true,messageBus.getMessagesHashMap().get(cpuService));
//        assertEquals(true,messageBus.getMessagesHashMap().get(conferenceService));
//        assertEquals(true,messageBus.getMessagesHashMap().get(studentService));
//        assertEquals(true,messageBus.getMessagesHashMap().get(timeService));
//    }
//
//    @Test
//    public void testComplete() {
//        result="blabla";
//        messageBus.complete(event,result);
//        assertEquals(true,messageBus.getEventFutureHashMape().get(event).get()==result);
//    }
//
//    @Test
//    public void testSendBroadcast() {
//        messageBus.sendBroadcast(broadcast);
//        for (MicroService microService: messageBus.getSubscribeBroadCasts().keySet()){
//            Queue queue=messageBus.getSubscribeBroadCasts().get(microService);
//            if(queue.contains(broadcast.getClass())) {
//                assertEquals(true,messageBus.getMessagesHashMap().get(microService).contains(broadcast));
//            }
//        }
//    }
//    @Test
//    public void testSendEvent() {
//
//
//        messageBus.subscribeEvent(event.getClass(),gpuService);
//        messageBus.sendEvent(event);
//        assertEquals(true,messageBus.getMessagesHashMap().get(gpuService).contains(event));
//        //I don't have a way to check if the microservice is in the end of the queue of this event type (for round-rubin design pattern
//    }
//    @Test
//    public void testRegister() {
//        messageBus.register(timeService);
//        messageBus.register(gpuService);
//        messageBus.register(conferenceService);
//        messageBus.register(studentService);
//        messageBus.register(cpuService);
//        assertEquals(true,messageBus.getMessagesHashMap().keySet().contains(timeService));
//        assertEquals(true,messageBus.getMessagesHashMap().keySet().contains(gpuService));
//        assertEquals(true,messageBus.getMessagesHashMap().keySet().contains(conferenceService));
//        assertEquals(true,messageBus.getMessagesHashMap().keySet().contains(studentService));
//        assertEquals(true,messageBus.getMessagesHashMap().keySet().contains(cpuService));
//    }
//
//    @Test
//    public void testUnregister() {
//        messageBus.unregister(timeService);
//        messageBus.unregister(gpuService);
//        messageBus.unregister(conferenceService);
//        messageBus.unregister(studentService);
//        messageBus.unregister(cpuService);
//        assertEquals(false,messageBus.getMessagesHashMap().keySet().contains(timeService));
//        assertEquals(false,messageBus.getMessagesHashMap().keySet().contains(gpuService));
//        assertEquals(false,messageBus.getMessagesHashMap().keySet().contains(conferenceService));
//        assertEquals(false,messageBus.getMessagesHashMap().keySet().contains(studentService));
//        assertEquals(false,messageBus.getMessagesHashMap().keySet().contains(cpuService));
//    }
//    @Test
//    public void testAwaitMessage() throws InterruptedException {
//
//        Message testme;
//        messageBus.subscribeEvent(event.getClass(),studentService);
//        messageBus.sendEvent(event);
//        assertEquals(true,messageBus.awaitMessage(studentService).equals(event));
//
//
//
////        cpuService.initialize();
//    }
}

