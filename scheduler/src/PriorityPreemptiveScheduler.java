import java.util.*;


public class PriorityPreemptiveScheduler {

    static class ProcInfo {
        Process p;         
        int remaining;       
        int completionTime;  
        int firstStartTime;  
        boolean started;     

        ProcInfo(Process p) {
            this.p = p;
            this.remaining = p.getBurstTime();
            this.started = false;
        }
    }

   
    static class GanttBlock {
        String pid;
        int start;
        int end;   // end is exclusive

        GanttBlock(String pid, int start, int end) {
            this.pid = pid;
            this.start = start;
            this.end = end;
        }

        public String toString() {
            return "|" + pid + " (" + start + " -> " + end + ")|";
        }
    }

    public static void run(List<Process> processes) {

        List<ProcInfo> infos = new ArrayList<>();
        for (Process p : processes) {
            infos.add(new ProcInfo(p));
        }

        
        int startTime = processes.get(0).getArrivalTime();
        for (Process p : processes) {
            if (p.getArrivalTime() < startTime) {
                startTime = p.getArrivalTime();
            }
        }

        int time = startTime;        
        int finished = 0;             
        int n = infos.size();         

        List<GanttBlock> gantt = new ArrayList<>();

  
        while (finished < n) {

            ProcInfo best = null;

            for (ProcInfo info : infos) {
                Process p = info.p;

                
                if (p.getArrivalTime() <= time && info.remaining > 0) {

                    if (best == null) {
                        best = info;
                    } else {
                        int curPr = p.getPriority();
                        int bestPr = best.p.getPriority();

                        
                        if (curPr < bestPr) {
                            best = info;
                        } else if (curPr == bestPr) {
                           
                            if (p.getArrivalTime() < best.p.getArrivalTime()) {
                                best = info;
                            }
                        }
                    }
                }
            }

            if (best == null) {
                time++;
                continue;
            }

            if (!best.started) {
                best.firstStartTime = time;
                best.started = true;
            }

            // Update Gantt chart: extend last block or start a new one
            if (gantt.isEmpty()) {
                gantt.add(new GanttBlock(best.p.getPid(), time, time + 1));
            } else {
                GanttBlock last = gantt.get(gantt.size() - 1);
                if (last.pid.equals(best.p.getPid())) {
                   
                    last.end++;
                } else {
                    gantt.add(new GanttBlock(best.p.getPid(), time, time + 1));
                }
            }
            best.remaining--;
            time++;

            if (best.remaining == 0) {
                best.completionTime = time;
                finished++;
            }
        }

        // ========== OUTPUT ==========

        System.out.println("\n=== Preemptive Priority Scheduling ===\n");

        System.out.println("Gantt Chart:");
        for (GanttBlock b : gantt) {
            System.out.print(b + " ");
        }
        System.out.println("\n");

        System.out.println("Per-process metrics:");
        System.out.println("PID\tAT\tBT\tPR\tCT\tTAT\tWT\tRT");

        for (ProcInfo info : infos) {
            Process p = info.p;

            int at = p.getArrivalTime();
            int bt = p.getBurstTime();
            int pr = p.getPriority();
            int ct = info.completionTime;
            int tat = ct - at;          // Turnaround time
            int wt = tat - bt;          // Waiting time
            int rt = info.firstStartTime - at;  // Response time

            System.out.println(
                    p.getPid() + "\t" +
                    at + "\t" +
                    bt + "\t" +
                    pr + "\t" +
                    ct + "\t" +
                    tat + "\t" +
                    wt + "\t" +
                    rt
            );
        }
    }
    
}
