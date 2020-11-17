public class MainTest {

    public static void main(String[] args){
        System.out.println("---------------抢占式多级反馈队列调度算法---------------");
        Process[] processes = new Process[]{new Process(1,0,3),
                new Process(2,2,6),
                new Process(3,4,4),
                new Process(4,6,5),
                new Process(5,8,2)};
        MFBQArithmetic mfbq = new MFBQArithmetic(processes, 3);
        mfbq.startFighting();

        System.out.println("---------------非抢占式多级反馈队列调度算法---------------");
        Process[] processes1 = new Process[]{new Process(1,0,3),
                new Process(2,2,6),
                new Process(3,4,4),
                new Process(4,6,5),
                new Process(5,8,2)};
        MFBQArithmetic mfbq1 = new MFBQArithmetic(processes1, 3);
        mfbq1.start();
    }
}
