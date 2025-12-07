import java.util.*;

public class SJFNonPreemptiveScheduler {

    public SchedulerResult schedule(List<Process> processes) {

        List<Process> all = new ArrayList<>(processes);
        all.sort(Comparator.comparingInt(Process::getArrivalTime));

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, ProcessMetrics> metrics = new LinkedHashMap<>();

        List<Process> ready = new ArrayList<>();
        int time = 0;
        int index = 0;
        int n = all.size();
        int completed = 0;

        while (completed < n) {

            while (index < n && all.get(index).getArrivalTime() <= time) {
                ready.add(all.get(index));
                index++;
            }

            if (ready.isEmpty()) {
                time++;
                continue;
            }

            ready.sort(Comparator.comparingInt(Process::getBurstTime));

            Process p = ready.remove(0);

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
            completed++;
        }

        return new SchedulerResult(gantt, metrics);
    }
}
