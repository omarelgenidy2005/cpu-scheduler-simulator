import java.util.*;

public class RoundRobin{

    public SchedulerResult schedule(List<Process> processes, int quantum) {

        if (quantum <= 0) {
            throw new IllegalArgumentException("Quantum must be > 0");
        }

        List<Process> sorted = new ArrayList<>();
        for (Process p : processes) {
            Process copy = new Process(
                    p.getPid(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getPriority()
            );
            sorted.add(copy);
        }

        sorted.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparing(Process::getPid));

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, ProcessMetrics> metrics = new LinkedHashMap<>();

        Queue<Process> readyQueue = new LinkedList<>();

        int time = 0;
        int index = 0;
        int n = sorted.size();
        int completed = 0;

        while (completed < n) {

            while (index < n && sorted.get(index).getArrivalTime() <= time) {
                readyQueue.add(sorted.get(index));
                index++;
            }

            if (readyQueue.isEmpty()) {
                if (index < n) {
                    time = sorted.get(index).getArrivalTime();
                    continue;
                } else {
                    break;
                }
            }

            Process p = readyQueue.poll();

            if (p.getStartTime() == -1) {
                p.setStartTime(time);
            }

            int runTime = Math.min(quantum, p.getRemainingTime());
            int start = time;

            int used = 0;
            while (used < runTime && p.getRemainingTime() > 0) {
                p.setRemainingTime(p.getRemainingTime() - 1);
                time++;
                used++;

                while (index < n && sorted.get(index).getArrivalTime() <= time) {
                    readyQueue.add(sorted.get(index));
                    index++;
                }
            }

            int end = time;
            gantt.add(new GanttEntry(p.getPid(), start, end));

            if (p.getRemainingTime() == 0) {
                p.setCompletionTime(end);

                int completionTime = end;
                int turnaroundTime = completionTime - p.getArrivalTime();
                int waitingTime = turnaroundTime - p.getBurstTime();
                int responseTime = p.getStartTime() - p.getArrivalTime();

                ProcessMetrics m = new ProcessMetrics(
                        p,
                        completionTime,
                        turnaroundTime,
                        waitingTime,
                        responseTime
                );

                metrics.put(p.getPid(), m);
                completed++;
            } else {
                readyQueue.add(p);
            }
        }

        return new SchedulerResult(gantt, metrics);
    }
}