import java.util.*;

public class MFBQArithmetic {

    private Process[] processes;//进程数组
    private int queueNum;//就绪队列数量
    private Map<Integer, ProcessQueue> queueMap = new HashMap<>();
    private int currentTime = 0;

    public MFBQArithmetic(Process[] processes, int queueNum){
        this.processes = processes;
        this.queueNum = queueNum;
        sortProcess();
        initProcessQueue();
    }
    //抢占式，被抢占之后，原来的的进程回到队尾，转移队列的时机是连续执行了规定时间没有执行完
    private Process cProcess;//代表持续执行的进程
    public void startFighting(){
        currentTime = 0;
        while(true){
            Process process = hasProcess(currentTime);//判断当前是否有进程来临
            if(process != null){
                this.queueMap.get(0).add(process);//如果有新进程来临，加入0号队列队尾
                if(cProcess != null){//代表新进程来的时候还有一个进程的时间片没有结束，开始抢占!
                    this.queueMap.get(cProcess.getQueueId()).add(cProcess);//将正在执行的进程插回原队列尾
                    cProcess = null;//清除原先持续执行的进程
                }
            }
            Process executeProcess;//代表当前工作的进程
            if(cProcess != null){
                executeProcess = cProcess;//如果当前进程的时间片没有结束，设置为将要执行的进程
            }else{
                executeProcess = getExecuteProcess();//如果当前进程的时间片结束了，得到下一个应该执行的进程
                if(executeProcess != null){
                    executeProcess.getRecordDoTime().add(currentTime);//添加这个进程这个时间片的开始时间，用于计算该进程已执行多长时间
                }
            }
            if(executeProcess == null && cProcess == null  && handDownProcess() == false){
                break;//如果没有 将要执行的进程 和 时间片没有结束的进程 和 没有处理的进程，代表算法结束
            }else{
                if(executeProcess == null){
                    currentTime++;//代表过去了一个单位时间
                    continue;
                }else{
                    executeProcess.setDoneTime(executeProcess.getDoneTime() + 1);//设置已为该进程服务多长时间
                    executeProcess.getRecordDoTime().add(currentTime+1);//为该进程这一单位时间的工作添加记录
                    if(executeProcess.getDoneTime() == executeProcess.getServeTime()){
                        executeProcess.setCompleteTime(currentTime+1);//如果已为该进程服务的时间等于该进程要求服务的时间，则该进程已执行完毕，不用再插入队列
                        cProcess = null;
                    }else{
                        int pid = executeProcess.getQueueId();//得到该进程所在的队列id（每一个进程在加入队列时为其设置所在队列，具体查看ProcessQueue类add方法）
                        if(whetherTurnFight(executeProcess)){//判断是否到了该进程转移到其它队列的时机，具体查看该类的whetherTurnFight私有方法
                            if(pid > this.queueNum - 2){//如果该进程已处于最后一个队列，插回原队列
                                this.queueMap.get(this.queueNum-1).add(executeProcess);
                            }else{//如果该进程不处于最后一个队列，则插入下一个队列
                                this.queueMap.get(pid+1).add(executeProcess);
                            }
                            cProcess = null;
                        }else{//当前进程没有到转移到别的队列的时机，将其设置成需要持续执行的cProcess
                            cProcess = executeProcess;
                        }
                    }
                }
            }
            currentTime++;//代表过去了一个单位时间
        }
        System.out.println(toResultString());
    }

    //非抢占式
    private Process continueProcess;//代表时间片还未结束的进程
    public void start(){
        currentTime = 0;
        while(true){
            Process process = hasProcess(currentTime);//判断当前是否有进程来临
            if(process != null){
                this.queueMap.get(0).add(process);//如果有新进程来临，插入第0号队列，不抢占
            }
            Process executeProcess;//代表将要执行的队列
            if(continueProcess != null){
                executeProcess = continueProcess;//上一秒执行的进程的时间片还未结束，将其设置为将要执行的进程
            }else{
                executeProcess = getExecuteProcess();
            }
            if(executeProcess == null && continueProcess == null && handDownProcess() == false){
                break;//队列里没有需要执行的进程，且没有正在执行的进程，则算法结束
            }else{
                if(executeProcess == null){
                    currentTime++;
                    continue;
                }else{
                    executeProcess.setDoneTime(executeProcess.getDoneTime() + 1);//设置该已为该进程服务了多长时间
                    if(executeProcess.getDoneTime() == executeProcess.getServeTime()){//判断该进程是否完成
                        executeProcess.setCompleteTime(currentTime+1);
                        continueProcess = null;
                    }else{
                        int pid = executeProcess.getQueueId();
                        if(whetherTurn(executeProcess)){//判断该进程是否应该转移到下一个队列
                            if(pid > this.queueNum - 2){
                                this.queueMap.get(this.queueNum-1).add(executeProcess);
                            }else{
                                this.queueMap.get(pid+1).add(executeProcess);
                            }
                            continueProcess = null;
                        }else{
                            continueProcess = executeProcess;//不应该转移到下一个队列，代表该进程的时间片还没有结束
                        }
                    }
                }
            }
            currentTime++;
        }
        System.out.println(toResultString());
    }

    //按照进程到达时间排序
    private void sortProcess(){
        Process[] pro = this.processes;
        Process process = new Process();//用于作为交换媒介
        for(int i = 0; i < pro.length; i++){
            for(int j = i; j < pro.length - 1; j++){
                if(pro[j].getComeTime() > pro[j+1].getComeTime()){
                    process = pro[j];
                    pro[j] = pro[j+1];
                    pro[j+1] = process;
                }
            }
        }
    }
    //初始化进程队列
    private void initProcessQueue(){
        int sum = this.queueNum;
        for(int i = 0; i < sum; i++){
            ProcessQueue processQueue = new ProcessQueue(i);
            this.queueMap.put(i, processQueue);
        }
    }
    //得到队列中将要进行的进程，进程全部结束返回null，否则返回将要执行的进程，将其从原队列弹出
    private Process getExecuteProcess(){
        int pnum = this.queueMap.size();
        for(int i = 0; i < pnum; i++){
            Process process = this.queueMap.get(i).poll();
            if(process != null){
                return process;
            }
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
    //非抢占式，判断该进程是否到了转移队列的时机
    private boolean whetherTurn(Process process){
        //非抢占式算法，可以不必计算进程已执行多长时间，根据已服务时间可以计算其当前所在队列
        int t = process.getQueueId();
        int sum = 0;
        for(int i = 0; i <= t; i++){
            sum = sum + (int)Math.pow(2, i);
        }
        if(process.getDoneTime() < sum){
            return false;
        }else{
            return true;
        }
    }
    //抢占式，判断该进程是否到了转移队列的时机，时间列表
    private boolean whetherTurnFight(Process process){
        //根据进程的recordTime列表内容，从后向前查找，计算该进程已持续执行多长时间
        int qid = process.getQueueId();
        int ctime = (int)(Math.pow(2, qid));
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
