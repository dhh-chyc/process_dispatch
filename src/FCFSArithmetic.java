public class FCFSArithmetic {

    private Process[] processes;
    private ProcessQueue queue = new ProcessQueue(0);

    public FCFSArithmetic(Process[] processes){
        this.processes = processes;
        sortProcess();
    }

    private Process continueProcess;//代表时间片还未结束的进程
    public void start(){
        int currentTime = 0;
        while (true){
            Process process = hasProcess(currentTime);
            if(process != null){
                queue.add(process);
            }
            Process executeProcess = null;
            if(continueProcess != null){
                executeProcess = continueProcess;
            }else{
                executeProcess = getExecuteProcess();
            }
            if(executeProcess == null && handDownProcess() == false){
                break;
            }else{
                if(executeProcess == null){
                    currentTime++;
                    continue;
                }else{
                    executeProcess.setDoneTime(executeProcess.getDoneTime() + 1);
                    if(executeProcess.getDoneTime() == executeProcess.getServeTime()){
                        executeProcess.setCompleteTime(currentTime + 1);
                        continueProcess = null;
                    }else{
                        continueProcess = executeProcess;
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
        return process;
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
    //判断队列中是否还有进程
    private boolean handDownProcess(){
        for(Process p : this.processes){
            if(p.getDoneTime() != p.getServeTime()){
                return true;
            }
        }
        return false;
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
