import java.util.ArrayList;
import java.util.List;

public class ProcessQueue{
    private int queueId;
    private int queueTime;
    private List<Process> processList = new ArrayList<>();

    public ProcessQueue(int queueId) {
        this.queueId = queueId;
    }

    public int getQueueTime() {
        return (int)Math.pow(this.queueTime, queueId);
    }
    public void setQueueTime(int queueTime) {
        this.queueTime = queueTime;
    }

    public boolean add(Process process) {
        process.setQueueId(this.queueId);//每个进程插入队列的时候设置它所在的队列ID
        process.setRecordDoTime(new ArrayList<Integer>());
        processList.add(process);
        return true;
    }

    public Process poll() {
        if(processList.size() == 0){
            return null;
        }
        Process p = processList.get(0);
        List<Process> plist = new ArrayList<>();
        for(int i = 1;i < processList.size(); i++){
            plist.add(processList.get(i));
        }
        this.processList = plist;
        return p;
    }

    public boolean hasProcess(Process process){
        for(Process p : this.processList){
            if(p.getProcessId() == process.getProcessId()){
                return true;
            }
        }
        return false;
    }

    public void insert(Process process){
        this.processList.add(0, process);
    }

    public int size() {
        return processList.size();
    }

    public boolean isEmpty() {
        if(processList.size()<=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "ProcessQueue{" +
                "processList=" + processList.toString() +
                '}';
    }
}
