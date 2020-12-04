public class MainTest {

    public static void main(String[] args){
//        System.out.println("---------------抢占式多级反馈队列调度算法---------------");
//        Process[] processes = new Process[]{new Process(1,0,3),
//                new Process(2,2,6),
//                new Process(3,4,4),
//                new Process(4,6,5),
//                new Process(5,8,2)};
//        MFBQArithmetic mfbq = new MFBQArithmetic(processes, 3);
//        mfbq.startFighting();
//
//        System.out.println("---------------非抢占式多级反馈队列调度算法---------------");
//        Process[] processes1 = new Process[]{new Process(1,0,3),
//                new Process(2,2,6),
//                new Process(3,4,4),
//                new Process(4,6,5),
//                new Process(5,8,2)};
//        MFBQArithmetic mfbq1 = new MFBQArithmetic(processes1, 3);
//        mfbq1.start();

//        System.out.println("---------------先来先服务算法---------------");
//        Process[] processes2 = new Process[]{new Process(1,1,3),
//                new Process(2,4,1),
//                new Process(3,2,2),
//                new Process(4,15,4),
//                new Process(5,3,5)};
//        FCFSArithmetic fcfsArithmetic = new FCFSArithmetic(processes2);
//        fcfsArithmetic.start();

//        System.out.println("---------------短作业优先算法---------------");
//        Process[] processes3 = new Process[]{new Process(1,1,3),
//                new Process(2,4,1),
//                new Process(3,2,2),
//                new Process(4,15,4),
//                new Process(5,3,5)};
//        SJFArithmetic sjfArithmetic = new SJFArithmetic(processes3);
//        sjfArithmetic.start();

//        System.out.println("---------------高响应比优先调度算法---------------");
//        Process[] processes4 = new Process[]{new Process(1,1,3),
//                new Process(2,4,1),
//                new Process(3,2,2),
//                new Process(4,15,3),
//                new Process(5,3,5)};
//        HRRNArithmetic hrrnArithmetic = new HRRNArithmetic(processes4);
//        hrrnArithmetic.start();

        System.out.println("---------------轮转调度算法---------------");
        Process[] processes1 = new Process[]{new Process(1,0,4),
                new Process(2,1,3),
                new Process(3,2,4)};
        RRArithmetic rrArithmetic1 = new RRArithmetic(processes1, 2);
        rrArithmetic1.start();

    }
}
