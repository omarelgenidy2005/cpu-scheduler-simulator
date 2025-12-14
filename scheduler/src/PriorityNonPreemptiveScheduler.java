import java.util.*;

public class PriorityNonPreemptiveScheduler {


    public SchedulerResult schedule(List<Process> processes) {

        List<Process> sorted = new ArrayList<>(processes);

        sorted.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getPriority)
                .thenComparing(Process::getPid));

        int n = sorted.size();
        int time = 0;
        int completed = 0;

        boolean[] done = new boolean[n];

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, ProcessMetrics> metrics = new LinkedHashMap<>();

        while (completed < n) {

            int bestIndex = -1;
            int bestPriority = Integer.MAX_VALUE;


            for (int i = 0; i < n; i++) {
                Process p = sorted.get(i);
                if (!done[i] && p.getArrivalTime() <= time) {
                    if (p.getPriority() < bestPriority) {
                        bestPriority = p.getPriority();
                        bestIndex = i;
                    }
                }
            }

            if (bestIndex == -1) {

                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!done[i] && sorted.get(i).getArrivalTime() > time) {
                        nextArrival = Math.min(nextArrival, sorted.get(i).getArrivalTime());
                    }
                }
                if (nextArrival == Integer.MAX_VALUE) {
                    break;
                }
                gantt.add(new GanttEntry("IDLE", time, nextArrival));
                time = nextArrival;
                continue;
            }

            Process current = sorted.get(bestIndex);

            int start = time;
            int end   = start + current.getBurstTime();


            gantt.add(new GanttEntry(current.getPid(), start, end));


            int completionTime = end;
            int turnaroundTime = completionTime - current.getArrivalTime();
            int waitingTime    = turnaroundTime - current.getBurstTime();
            int responseTime   = start - current.getArrivalTime();

            ProcessMetrics m = new ProcessMetrics(
                    current,
                    completionTime,
                    turnaroundTime,
                    waitingTime,
                    responseTime
            );
            metrics.put(current.getPid(), m);

            done[bestIndex] = true;
            completed++;
            time = end;
        }

        return new SchedulerResult(gantt, metrics);
    }
}
