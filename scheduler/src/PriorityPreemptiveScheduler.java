import java.util.ArrayList;
import java.util.List;

public class PriorityPreemptiveScheduler {

    // class to keep track of the needed values for each process
    private static class ProcessState {
        Process process;
        int remainingTime;
        Integer completionTime;
        Integer firstStartTime;

        ProcessState(Process p) {
            this.process = p;
            this.remainingTime = p.getBurstTime();
        }
    }

    private static class GanttEntry {
        String pid;
        int start;
        int end;

        GanttEntry(String pid, int start, int end) {
            this.pid = pid;
            this.start = start;
            this.end = end;
        }

        public String toString() {
            return "|" + pid + " (" + start + " -> " + end + ")|";
        }
    }

    public static void run(List<Process> processes) {

        // turn each process into a mutable state
        List<ProcessState> states = new ArrayList<>();
        for (Process p : processes) {
            states.add(new ProcessState(p));
        }

        // find the smallest arrival time (simulation start)
        int startTime = processes.get(0).getArrivalTime();
        for (Process p : processes) {
            if (p.getArrivalTime() < startTime) {
                startTime = p.getArrivalTime();
            }
        }

        int time = startTime;
        int finished = 0;
        int n = states.size();
        int totalBurst = 0;

        for (Process p : processes) {
            totalBurst += p.getBurstTime();
        }

        List<GanttEntry> gantt = new ArrayList<>();

        // run the simulation until all processes finish
        while (finished < n) {

            // pick the ready process that has the highest priority
            ProcessState best = null;

            for (ProcessState ps : states) {
                Process p = ps.process;

                if (p.getArrivalTime() <= time && ps.remainingTime > 0) {

                    if (best == null) {
                        best = ps;
                    } else {
                        // smaller priority value means higher priority
                        if (p.getPriority() < best.process.getPriority()) {
                            best = ps;
                        } else if (p.getPriority() == best.process.getPriority()) {
                            // tie → earlier arrival wins
                            if (p.getArrivalTime() < best.process.getArrivalTime()) {
                                best = ps;
                            }
                        }
                    }
                }
            }

            if (best == null) {
                
                time++;
                continue;
            }

            if (best.firstStartTime == null) {
                best.firstStartTime = time;
            }

            // update the Gantt chart
            if (gantt.isEmpty()) {
                gantt.add(new GanttEntry(best.process.getPid(), time, time + 1));
            } else {
                GanttEntry last = gantt.get(gantt.size() - 1);
                if (last.pid.equals(best.process.getPid())) {
                    last.end++;
                } else {
                    gantt.add(new GanttEntry(best.process.getPid(), time, time + 1));
                }
            }

            best.remainingTime--;
            time++;

            if (best.remainingTime == 0) {
                best.completionTime = time;
                finished++;
            }
        }

        System.out.println("\n=== Preemptive Priority Scheduling ===\n");

        System.out.println("Gantt Chart:");
        for (GanttEntry g : gantt) {
            System.out.print(g + " ");
        }
        System.out.println("\n");

        double totalWT = 0;
        double totalTAT = 0;
        double totalRT = 0;

        System.out.println("Per-process metrics:");
        System.out.println("PID\tAT\tBT\tPR\tCT\tTAT\tWT\tRT");

        for (ProcessState ps : states) {
            Process p = ps.process;

            int ct = ps.completionTime;
            int tat = ct - p.getArrivalTime();
            int wt = tat - p.getBurstTime();
            int rt = ps.firstStartTime - p.getArrivalTime();

            totalWT += wt;
            totalTAT += tat;
            totalRT += rt;

            System.out.println(
                    p.getPid() + "\t" +
                            p.getArrivalTime() + "\t" +
                            p.getBurstTime() + "\t" +
                            p.getPriority() + "\t" +
                            ct + "\t" +
                            tat + "\t" +
                            wt + "\t" +
                            rt
            );
        }

        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;
        double avgRT = totalRT / n;

        double totalTime = time - startTime;
        double throughput = n / totalTime;
        double utilization = (totalBurst * 100.0) / totalTime;

        System.out.println("\nAverages:");
        System.out.println("Average Waiting Time    = " + avgWT);
        System.out.println("Average Turnaround Time = " + avgTAT);
        System.out.println("Average Response Time   = " + avgRT);

        System.out.println("\nOther metrics:");
        System.out.println("Throughput      = " + throughput + " processes per unit time");
        System.out.println("CPU Utilization = " + utilization + "%");
    }
}
