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

            ProcessMetrics m = new ProcessMetrics(p);
            m.completionTime = end;
            m.turnaroundTime = m.completionTime - m.arrivalTime;
            m.waitingTime = m.turnaroundTime - m.burstTime;
            m.responseTime = start - m.arrivalTime;

            metrics.put(m.pid, m);

            time = end;
            completed++;
        }

        return new SchedulerResult(gantt, metrics);
    }
}