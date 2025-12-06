import java.util.*;
public class MultiLevelFeedbackQueueScheduler {
    private final int[] quantums;
    public MultiLevelFeedbackQueueScheduler(int[] quantums) {
        if (quantums == null || quantums.length == 0) {
            throw new IllegalArgumentException("quantums must not be empty");
        }
        this.quantums = Arrays.copyOf(quantums, quantums.length);
    }
    public MultiLevelFeedbackQueueScheduler() {
        this(new int[]{2, 4, 8});
    }
    private static class RunProcess {
        final Process process;
        int level;
        int remaining;
        int usedInCurrentQuantum;
        int firstStartTime = -1;
        RunProcess(Process process) {
            this.process = process;
            this.level = 0;
            this.remaining = process.getBurstTime();
        }
    }
    public SchedulerResult schedule(List<Process> processes) {
        List<RunProcess> all = new ArrayList<>();
        for (Process p : processes) {
            all.add(new RunProcess(p));
        }
        all.sort(Comparator.comparingInt(rp -> rp.process.getArrivalTime()));
        int levels = quantums.length;
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
                queues.get(0).offer(all.get(index));
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
                current.usedInCurrentQuantum = 0;
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
                    current.usedInCurrentQuantum = 0;
                    currentStartTime = time;
                    if (current.firstStartTime == -1) {
                        current.firstStartTime = time;
                    }
                }
            }
            current.remaining--;
            current.usedInCurrentQuantum++;
            time++;
            if (current.remaining == 0) {
                gantt.add(new GanttEntry(current.process.getPid(), currentStartTime, time));
                Process p = current.process;
                ProcessMetrics m = new ProcessMetrics(p);
                m.completionTime = time;
                m.turnaroundTime = m.completionTime - m.arrivalTime;
                m.waitingTime = m.turnaroundTime - m.burstTime;
                m.responseTime = current.firstStartTime - m.arrivalTime;
                metricsMap.put(m.pid, m);
                current = null;
                currentStartTime = -1;
                completed++;
            } else if (current.usedInCurrentQuantum == quantums[current.level]) {
                gantt.add(new GanttEntry(current.process.getPid(), currentStartTime, time));
                if (current.level < levels - 1) {
                    current.level++;
                }
                current.usedInCurrentQuantum = 0;
                queues.get(current.level).offer(current);
                current = null;
                currentStartTime = -1;
            }
        }
        return new SchedulerResult(gantt, metricsMap);
    }
}
