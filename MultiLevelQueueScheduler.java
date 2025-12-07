import java.util.*;

public class MultiLevelQueueScheduler {

    private final int levels;

    public MultiLevelQueueScheduler(int levels) {
        if (levels <= 0) {
            throw new IllegalArgumentException("levels must be > 0");
        }
        this.levels = levels;
    }

    public MultiLevelQueueScheduler() {
        this(3);
    }

    private static class RunProcess {
        final Process process;
        int level;
        int remaining;
        int firstStartTime = -1;

        RunProcess(Process process, int level) {
            this.process = process;
            this.level = level;
            this.remaining = process.getBurstTime();
        }
    }

    public SchedulerResult schedule(List<Process> processes) {
        List<RunProcess> all = new ArrayList<>();
        for (Process p : processes) {
            int level = p.getPriority();
            if (level < 0) level = 0;
            if (level >= levels) level = levels - 1;
            all.add(new RunProcess(p, level));
        }

        all.sort(Comparator.comparingInt(rp -> rp.process.getArrivalTime()));

        List<Deque<RunProcess>> queues = new ArrayList<>();
        for (int i = 0; i < levels; i++) {
            queues.add(new ArrayDeque<>());
        }

        List<GanttEntry> gantt = new ArrayList<>();
        Map<String, ProcessMetrics> metricsMap = new LinkedHashMap<>();

        int time = 0;
        int index = 0;
        int n = all.size();
        int completed = 0;
        RunProcess current = null;
        int currentStartTime = -1;

        while (completed < n) {

            while (index < n && all.get(index).process.getArrivalTime() <= time) {
                RunProcess rp = all.get(index);
                queues.get(rp.level).offer(rp);
                index++;
            }

            int highestNonEmpty = -1;
            for (int lvl = 0; lvl < levels; lvl++) {
                if (!queues.get(lvl).isEmpty()) {
                    highestNonEmpty = lvl;
                    break;
                }
            }

            if (current == null) {
                if (highestNonEmpty == -1) {
                    time++;
                    continue;
                }
                current = queues.get(highestNonEmpty).poll();
                currentStartTime = time;
                if (current.firstStartTime == -1) {
                    current.firstStartTime = time;
                }
            } else {
                if (highestNonEmpty != -1 && highestNonEmpty < current.level) {
                    if (currentStartTime != time) {
                        gantt.add(new GanttEntry(current.process.getPid(), currentStartTime, time));
                    }
                    queues.get(current.level).offer(current);
                    current = queues.get(highestNonEmpty).poll();
                    currentStartTime = time;
                    if (current.firstStartTime == -1) {
                        current.firstStartTime = time;
                    }
                }
            }

            current.remaining--;
            time++;

            if (current.remaining == 0) {
                gantt.add(new GanttEntry(current.process.getPid(), currentStartTime, time));

                Process p = current.process;
                int completionTime = time;
                int turnaroundTime = completionTime - p.getArrivalTime();
                int waitingTime = turnaroundTime - p.getBurstTime();
                int responseTime = current.firstStartTime - p.getArrivalTime();

                ProcessMetrics m = new ProcessMetrics(
                        p,
                        completionTime,
                        turnaroundTime,
                        waitingTime,
                        responseTime
                );

                metricsMap.put(p.getPid(), m);

                current = null;
                currentStartTime = -1;
                completed++;
            }
        }

        return new SchedulerResult(gantt, metricsMap);
    }
}
