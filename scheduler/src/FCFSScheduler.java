import java.util.*;

public class FCFSScheduler {

    public SchedulerResult schedule(List<Process> processes) {

        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort(Comparator.comparingInt(Process::getArrivalTime));

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, ProcessMetrics> metrics = new LinkedHashMap<>();

        int time = 0;

        for (Process p : sorted) {
            if (time < p.getArrivalTime()) {
                time = p.getArrivalTime();
            }

            int start = time;
            int end = start + p.getBurstTime();

            gantt.add(new GanttEntry(p.getPid(), start, end));

            int completionTime = end;
            int turnaroundTime = completionTime - p.getArrivalTime();
            int waitingTime = turnaroundTime - p.getBurstTime();
            int responseTime = start - p.getArrivalTime();

            ProcessMetrics m = new ProcessMetrics(
                    p,
                    completionTime,
                    turnaroundTime,
                    waitingTime,
                    responseTime
            );

            metrics.put(p.getPid(), m);

            time = end;
        }

        return new SchedulerResult(gantt, metrics);
    }
}
