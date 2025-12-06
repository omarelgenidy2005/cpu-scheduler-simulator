import java.util.*;

public class PriorityNonPreemptiveScheduler {

    private final List<String> ganttChart = new ArrayList<>();

    public void schedule(List<Process> processes) {

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int time = 0;
        int completed = 0;
        int n = processes.size();

        Map<Process, Integer> startTime = new HashMap<>();
        Map<Process, Integer> completionTime = new HashMap<>();
        Map<Process, Integer> waitingTime = new HashMap<>();
        Map<Process, Integer> turnaroundTime = new HashMap<>();
        Map<Process, Integer> responseTime = new HashMap<>();

        boolean[] visited = new boolean[n];

        while (completed < n) {

            int bestIndex = -1;
            int bestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);

                if (!visited[i] && p.getArrivalTime() <= time) {

                    if (p.getPriority() < bestPriority) {
                        bestPriority = p.getPriority();
                        bestIndex = i;
                    }
                }
            }

            if (bestIndex == -1) {
                ganttChart.add("IDLE");
                time++;
                continue;
            }

            Process current = processes.get(bestIndex);

            startTime.putIfAbsent(current, time);

            ganttChart.add(current.getPid());

            time += current.getBurstTime();
            visited[bestIndex] = true;
            completed++;

            completionTime.put(current, time);

            int tat = time - current.getArrivalTime();
            turnaroundTime.put(current, tat);

            int wt = tat - current.getBurstTime();
            waitingTime.put(current, wt);

            int rt = startTime.get(current) - current.getArrivalTime();
            responseTime.put(current, rt);
        }

        printResults(processes, startTime, completionTime, waitingTime, turnaroundTime, responseTime);
    }


    private void printResults(
            List<Process> processes,
            Map<Process, Integer> startTime,
            Map<Process, Integer> completionTime,
            Map<Process, Integer> waitingTime,
            Map<Process, Integer> turnaroundTime,
            Map<Process, Integer> responseTime) {

        System.out.println("\n===== Priority Scheduling (Non-Preemptive) =====");
        System.out.println("\nGantt Chart: " + ganttChart);

        System.out.println("\nPID\tAT\tBT\tP\tCT\tTAT\tWT\tRT");
        for (Process p : processes) {
            System.out.println(
                    p.getPid() + "\t" +
                            p.getArrivalTime() + "\t" +
                            p.getBurstTime() + "\t" +
                            p.getPriority() + "\t" +
                            completionTime.get(p) + "\t" +
                            turnaroundTime.get(p) + "\t" +
                            waitingTime.get(p) + "\t" +
                            responseTime.get(p)
            );
        }
    }
}
