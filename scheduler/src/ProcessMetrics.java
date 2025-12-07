public class ProcessMetrics {

    private final Process process;
    public final String pid;
    public final int arrivalTime;
    public final int burstTime;

    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private int responseTime;

    public ProcessMetrics(Process p) {
        this.process = p;
        this.pid = p.getPid();
        this.arrivalTime = p.getArrivalTime();
        this.burstTime = p.getBurstTime();

    }

    public ProcessMetrics(Process p,
                          int completionTime,
                          int turnaroundTime,
                          int waitingTime,
                          int responseTime) {
        this(p);
        this.completionTime = completionTime;
        this.turnaroundTime = turnaroundTime;
        this.waitingTime = waitingTime;
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return pid + " | CT= " + completionTime +
                " | TAT= " + turnaroundTime +
                " | WT= " + waitingTime +
                " | RT= " + responseTime;
    }

    public Process getProcess() { return process; }

    public int getCompletionTime() { return completionTime; }

    public int getTurnaroundTime() { return turnaroundTime; }

    public int getWaitingTime() { return waitingTime; }

    public int getResponseTime() { return responseTime; }
}
