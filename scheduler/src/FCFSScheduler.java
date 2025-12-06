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

            ProcessMetrics m = new ProcessMetrics(p);
            m.completionTime = end;
            m.turnaroundTime = m.completionTime - m.arrivalTime;
            m.waitingTime = m.turnaroundTime - m.burstTime;
            m.responseTime = start - m.arrivalTime;

            metrics.put(m.pid, m);

            time = end;
        }

        return new SchedulerResult(gantt, metrics);
    }
}