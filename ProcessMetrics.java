public class ProcessMetrics {
    public final String pid;
    public final int arrivalTime;
    public final int burstTime;
    public int completionTime;
    public int turnaroundTime;
    public int waitingTime;
    public int responseTime;
    public ProcessMetrics(Process p) {
        this.pid = p.getPid();
        this.arrivalTime = p.getArrivalTime();
        this.burstTime = p.getBurstTime();
    }
    @Override
    public String toString() {
        return pid + " | CT=" + completionTime + " | TAT=" + turnaroundTime +
                " | WT=" + waitingTime + " | RT=" + responseTime;
    }
}