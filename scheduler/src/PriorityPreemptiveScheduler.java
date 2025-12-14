import java.util.*;

public class PriorityPreemptiveScheduler  {

    static class ProcInfo {
        Process p;
        int remaining;
        int firstStartTime = -1;
        int completionTime = -1;

        ProcInfo(Process p) {
            this.p = p;
            this.remaining = p.getBurstTime();
        }
    }


    public SchedulerResult schedule(List<Process> processes) {

        // work on copies so we don't mutate original list
        List<ProcInfo> infos = new ArrayList<>();
        for (Process p : processes) {
            Process copy = new Process(
                    p.getPid(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getPriority()
            );
            infos.add(new ProcInfo(copy));
        }

        // sort for deterministic scanning
        infos.sort(Comparator.comparingInt(i -> i.p.getArrivalTime()));

        int n = infos.size();
        int time = infos.get(0).p.getArrivalTime();
        int finished = 0;

        List<GanttEntry> gantt = new ArrayList<>();

        String currentPid = null;
        int blockStart = time;

        while (finished < n) {

            // choose best process (highest priority, i.e. lowest priority value)
            ProcInfo best = null;
            for (ProcInfo info : infos) {
                Process p = info.p;
                if (p.getArrivalTime() <= time && info.remaining > 0) {
                    if (best == null) {
                        best = info;
                    } else {
                        int curPr = p.getPriority();
                        int bestPr = best.p.getPriority();
                        if (curPr < bestPr ||
                                (curPr == bestPr && p.getArrivalTime() < best.p.getArrivalTime())) {
                            best = info;
                        }
                    }
                }
            }

            String pidThisUnit;

            if (best == null) {
                // CPU is idle this unit
                pidThisUnit = "IDLE";
            } else {
                pidThisUnit = best.p.getPid();
                if (best.firstStartTime == -1) {
                    best.firstStartTime = time;
                }
            }

            // start a new block if PID changed
            if (currentPid == null) {
                currentPid = pidThisUnit;
                blockStart = time;
            } else if (!currentPid.equals(pidThisUnit)) {
                gantt.add(new GanttEntry(currentPid, blockStart, time));
                currentPid = pidThisUnit;
                blockStart = time;
            }

            // execute one time unit
            if (best == null) {
                time++; // idle
            } else {
                best.remaining--;
                time++;
                if (best.remaining == 0) {
                    best.completionTime = time;
                    finished++;
                }
            }
        }

        // close the last block
        if (currentPid != null) {
            gantt.add(new GanttEntry(currentPid, blockStart, time));
        }

        // build metrics for OutputFormatter
        Map<String, ProcessMetrics> metrics = new LinkedHashMap<>();

        for (ProcInfo info : infos) {
            Process p = info.p;

            int at = p.getArrivalTime();
            int bt = p.getBurstTime();
            int ct = info.completionTime;
            int tat = ct - at;
            int wt = tat - bt;
            int rt = info.firstStartTime - at;

            ProcessMetrics m = new ProcessMetrics(
                    p,
                    ct,
                    tat,
                    wt,
                    rt
            );
            metrics.put(p.getPid(), m);
        }

        return new SchedulerResult(gantt, metrics);
    }
}
