public class SJFArithmetic {
    private Process[] processes;
    private ProcessQueue queue = new ProcessQueue(0);

    public SJFArithmetic(Process[] processes){
        this.processes = processes;
        sortProcess();
    }

    public void start(){
        int current = 0;
        for(Process p : this.processes){
            p.setComeTime(current);
            for(int i=0;i<p.getServeTime();i++){
                p.setDoneTime(p.getDoneTime() + 1);
                current++;
            }
            p.setCompleteTime(current);
        }
        System.out.println(toResultString());
    }

    //按照进程服务时间排序
    private void sortProcess(){
        Process[] pro = this.processes;
        Process process = new Process();//用于作为交换媒介
        for(int i = 0; i < pro.length; i++){
            for(int j = 0; j < pro.length - 1 - i; j++){
                if(pro[j].getServeTime() > pro[j+1].getServeTime()){
                    process = pro[j];
                    pro[j] = pro[j+1];
                    pro[j+1] = process;
                }
            }
        }
        for(Process p : this.processes){
            this.queue.add(p);
        }
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
