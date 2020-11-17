import java.util.ArrayList;
import java.util.List;

public class Process{

    private int processId;//进程标识符
    private int comeTime;//进程到来时间
    private int serveTime;//系统服务时间
    private int queueId;//进程当前所在队列
    private int doneTime = 0;//进程已执行时间
    private int completeTime;//进程完成时间
    private int revolveTime;//进程周转时间
    private double wRevolveTime;//带权周转时间
    private List<Integer> recordDoTime = new ArrayList<>();

    public Process(){}
    public Process(int processId, int comeTime, int serveTime){
        this.processId = processId;
        this.comeTime = comeTime;
        this.serveTime = serveTime;
    }

    public int getProcessId() {
        return processId;
    }
    public void setProcessId(int processId) {
        this.processId = processId;
    }
    public int getComeTime() {
        return comeTime;
    }
    public void setComeTime(int comeTime) {
        this.comeTime = comeTime;
    }
    public int getServeTime() {
        return serveTime;
    }
    public void setServeTime(int serveTime) {
        this.serveTime = serveTime;
    }
    public int getQueueId() {
        return queueId;
    }
    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }
    public int getDoneTime() {
        return doneTime;
    }
    public void setDoneTime(int doneTime) {
        this.doneTime = doneTime;
    }
    public int getCompleteTime() {
        return completeTime;
    }
    public void setCompleteTime(int completeTime) {
        this.completeTime = completeTime;
    }
    public int getRevolveTime() {
        return this.completeTime - this.comeTime;
    }
    public double getwRevolveTime() {
        return getRevolveTime() / (double)serveTime;
    }
    public List<Integer> getRecordDoTime() {
        return recordDoTime;
    }
    public void setRecordDoTime(List<Integer> recordDoTime) {
        this.recordDoTime = recordDoTime;
    }

    @Override
    public String toString() {
//        ", queueId=" + queueId +
//                ", doneTime=" + doneTime +

//        "，recordTime="+ getRecordDoTime().toString()+
        return "Process{" +
                "processId=" + processId +
                ", comeTime=" + comeTime +
                ", serveTime=" + serveTime +
                ", completeTime=" + completeTime +
                ", revolveTime=" + getRevolveTime() +
                ", wRevolveTime=" + getwRevolveTime() +
                "，recordTime="+ getRecordDoTime().toString()+
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        Process p = (Process) obj;
        if(p.getProcessId() == this.getProcessId()){
            return true;
        }else{
            return false;
        }
    }
}