public class Process {

    private final String pid;
    private final int arrivalTime;
    private final int burstTime;

    private final int priority;

    private int remainingTime;
    private int startTime;
    private int completionTime;

    public Process(String pid, int arrivalTime, int burstTime, int priority) {
        if (pid == null || pid.isBlank()) {
            throw new IllegalArgumentException("PID cannot be null or blank");
        }
        if (arrivalTime < 0) {
            throw new IllegalArgumentException("Arrival time cannot be negative");
        }
        if (burstTime <= 0) {
            throw new IllegalArgumentException("Burst time must be > 0");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("Priority cannot be negative");
        }

        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;

        this.remainingTime = burstTime;
        this.startTime = -1;
        this.completionTime = -1;
    }

    public String getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        if (remainingTime < 0) {
            throw new IllegalArgumentException("Remaining time cannot be negative");
        }
        this.remainingTime = remainingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        if (startTime < 0) {
            throw new IllegalArgumentException("Start time cannot be negative");
        }
        this.startTime = startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        if (completionTime < 0) {
            throw new IllegalArgumentException("Completion time cannot be negative");
        }
        this.completionTime = completionTime;
    }

    public int getTurnaroundTime() {
        if (completionTime < 0) return -1;
        return completionTime - arrivalTime;
    }

    public int getWaitingTime() {
        if (completionTime < 0) return -1;
        return getTurnaroundTime() - burstTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pid='" + pid + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", remainingTime=" + remainingTime +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Process)) return false;
        Process process = (Process) o;
        return pid.equals(process.pid);
    }

    @Override
    public int hashCode() {
        return pid.hashCode();
    }
}
