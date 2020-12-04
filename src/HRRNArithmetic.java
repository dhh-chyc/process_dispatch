public class HRRNArithmetic {
    private Process[] processes;

    public HRRNArithmetic(Process[] processes){
        this.processes = processes;
        sortProcess();
    }

    public void start(){
        Process executeProcess = getHighPriority(0);
        int current = 0;
        while (true){
            if(handDownProcess() == false){
                break;//所有的进程服务已经完成
            }else{
                if(executeProcess == null){//表示仍然有进程需要服务，但是当前CPU空闲
                    current++;
                    executeProcess = getHighPriority(current);
                    continue;
                }else{
                    executeProcess.setDoneTime(executeProcess.getDoneTime() + 1);
                    if(executeProcess.getDoneTime() == executeProcess.getServeTime()){
                        executeProcess.setCompleteTime(current + 1);
                        executeProcess = getHighPriority(current);
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
    //判断当前时间点响应比最高的进程
    private Process getHighPriority(int current){
        Process pro = this.processes[0];
        for(Process p : this.processes){
            if(p.getDoneTime() == 0){//从未执行的进程中查找响应比最高的进程
                if(p.getProcessPriority(current) > pro.getProcessPriority(current)){
                    pro = p;
                }
            }
        }
        if(pro.getProcessPriority(current) == -1f){
            return null;
        }
        return pro;
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
