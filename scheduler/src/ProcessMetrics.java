public class ProcessMetrics {

    private final Process process;
    private final int completionTime;
    private final int turnaroundTime;
    private final int waitingTime;
    private final int responseTime;

    public ProcessMetrics(Process process,
                          int completionTime,
                          int turnaroundTime,
                          int waitingTime,
                          int responseTime) {
        this.process = process;
        this.completionTime = completionTime;
        this.turnaroundTime = turnaroundTime;
        this.waitingTime = waitingTime;
        this.responseTime = responseTime;
    }

    public Process getProcess() {
        return process;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getResponseTime() {
        return responseTime;
    }
}
