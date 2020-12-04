import java.util.List;

public class RRArithmetic {

    private Process[] processes;//进程数组
    private int queueNum;//就绪队列数量
    private int queueTime;//队列时间片的底 n^(i)
    private ProcessQueue queue = new ProcessQueue(1);

    public RRArithmetic(Process[] processes, int queueTime){
        this.processes = processes;
        this.queueNum = 1;
        this.queueTime = queueTime;
        sortProcess();
    }

    private Process continueProcess;
    public void start(){
        int current = 0;
        while (true){
            Process process = hasProcess(current);
            if(process != null){//如果有进程到来添加到队列尾
                if(!this.queue.hasProcess(process)){
                    this.queue.add(process);
                }
            }
            Process executeProcess;//如果上一个时间片的进程还没执行完毕，就继续执行，否则执行队首进程
            if(continueProcess == null){
                executeProcess = getExecuteProcess();
                if(executeProcess != null){
                    executeProcess.getRecordDoTime().add(current);
                }
            }else{
                executeProcess = continueProcess;
            }
            if(handDownProcess() == false){//如果所有进程执行完毕，退出循环
                break;
            }else{
                if(executeProcess == null){//表示CPU空闲，但是还有进程没执行完毕
                    current++;
                    continue;
                }else{
                    executeProcess.setDoneTime(executeProcess.getDoneTime() + 1);
                    executeProcess.getRecordDoTime().add(current + 1);
                    if(executeProcess.getDoneTime() == executeProcess.getServeTime()){
                        executeProcess.setCompleteTime(current + 1);//如果已为该进程服务的时间等于该进程要求服务的时间，则该进程已执行完毕，不用再插入队列
                        continueProcess = null;
                    }else{
                        if(whetherTurnQueue(executeProcess)){//判断是否到了该进程转移到其它队列的时机
                            Process p = hasProcess(current + 1);
                            if(p != null){
                                this.queue.add(p);
                            }
                            this.queue.add(executeProcess);
                            continueProcess = null;
                        }else{
                            continueProcess = executeProcess;
                        }
                    }
                }
            }
            current++;
        }
        System.out.println(toResultString());
    }

    //按照进程到达时间排序
    private void sortProcess(){
        Process[] pro = this.processes;
        Process process = new Process();//用于作为交换媒介
        for(int i = 0; i < pro.length; i++){
            for(int j = 0; j < pro.length - 1 - i; j++){
                if(pro[j].getComeTime() > pro[j+1].getComeTime()){
                    process = pro[j];
                    pro[j] = pro[j+1];
                    pro[j+1] = process;
                }
            }
        }
    }

    //得到队列中将要进行的进程，进程全部结束返回null，否则返回将要执行的进程，将其从原队列弹出
    private Process getExecuteProcess(){
        Process process = this.queue.poll();
        if(process != null){
            return process;
        }
        return null;
    }
    //判断队列中是否还有进程
    private boolean handDownProcess(){
        for(Process p : this.processes){
            if(p.getDoneTime() != p.getServeTime()){
                return true;
            }
        }
        return false;
    }
    //判断这一时刻是否有进程到来，有进程到来返回进程对象，无进程到来返回null
    private Process hasProcess(int currentTime){
        for(Process p : this.processes){
            if(p.getComeTime() == currentTime){
                return p;
            }
        }
        return null;
    }

    //判断该进程是否到了转移队列的时机，时间列表
    private boolean whetherTurnQueue(Process process){
        //根据进程的recordTime列表内容，从后向前查找，计算该进程已持续执行多长时间
        int qid = process.getQueueId();
        int ctime = (int)(Math.pow(this.queueTime, qid));
        int recoreLength = process.getRecordDoTime().size();
        List<Integer> recordTime = process.getRecordDoTime();
        for(int i=0;i<ctime;i++){
            try{
                int a = recordTime.get(recoreLength-1 - i);
                int b = recordTime.get(recoreLength-2 - i);
                if((a-1) != b){
                    return false;
                }
            }catch(Exception e){
                return false;
            }
        }
        return true;
    }
    //得到平均周转时间
    private double getAverageRevolveTime(){
        int sum = 0;
        for(Process p : this.processes){
            sum = sum + p.getRevolveTime();
        }
        return ((double)sum) / this.processes.length;
    }
    //得到平均带权周转时间
    private double getWAverageRevolveTime(){
        double sum = 0.0;
        for(Process p : this.processes){
            sum = sum + p.getwRevolveTime();
        }
        return sum/this.processes.length;
    }
    //显示数据
    private String toResultString() {
        String str = "";
        for(Process p : this.processes){
            str = str + p.toString() + "\n";
        }
        str = str + "平均周转时间为："+getAverageRevolveTime()+"\n";
        str = str + "平均带权周转时间为："+getWAverageRevolveTime();
        return str;
    }
}