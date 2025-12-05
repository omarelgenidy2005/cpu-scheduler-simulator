public class Process {

    private final String pid;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;

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

    @Override
    public String toString() {
        return "Process{" +
                "pid='" + pid + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
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
