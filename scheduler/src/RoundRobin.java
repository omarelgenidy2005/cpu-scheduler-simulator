import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin {

    private List<Process> processes;
    private List<GanttEntry> gantt;
    private int quantum;

    public RoundRobin(List<Process> processes, int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("Quantum must be > 0");
        }


        this.processes = new ArrayList<>(processes);
        this.gantt = new ArrayList<>();
        this.quantum = quantum;


        this.processes.sort((p1, p2) -> {
            if (p1.getArrivalTime() != p2.getArrivalTime()) {
                return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
            }
            return p1.getPid().compareTo(p2.getPid());
        });
    }

    public void schedule() {
        int time = 0;
        int n = processes.size();
        int finished = 0;
        int index = 0;

        Queue<Process> readyQueue = new LinkedList<>();

        while (finished < n) {


            while (index < n && processes.get(index).getArrivalTime() <= time) {
                readyQueue.add(processes.get(index));
                index++;
            }


            if (readyQueue.isEmpty()) {
                if (index < n) {
                    int nextArrival = processes.get(index).getArrivalTime();
                    if (nextArrival > time) {
                        gantt.add(new GanttEntry("IDLE", time, nextArrival));
                        time = nextArrival;
                        continue;
                    }
                } else {
                    break;
                }
            }

            Process current = readyQueue.poll();

            if (current.getStartTime() == -1) {
                current.setStartTime(time);
            }

            int runFor = Math.min(quantum, current.getRemainingTime());
            int sliceStart = time;

            for (int i = 0; i < runFor; i++) {
                current.setRemainingTime(current.getRemainingTime() - 1);
                time++;

                while (index < n && processes.get(index).getArrivalTime() <= time) {
                    readyQueue.add(processes.get(index));
                    index++;
                }

                if (current.getRemainingTime() == 0) {
                    break;
                }
            }

            int sliceEnd = time;
            gantt.add(new GanttEntry(current.getPid(), sliceStart, sliceEnd));

            if (current.getRemainingTime() == 0) {
                current.setCompletionTime(time);
                finished++;
            } else {
                readyQueue.add(current);
            }
        }
    }

    public List<GanttEntry> getGantt() {
        return gantt;
    }

    public List<Process> getProcesses() {
        return processes;
    }
}