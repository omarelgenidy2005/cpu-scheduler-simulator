import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String path = "processesok.csv";

        try {
            List<Process> processes = ProcessCSVreader.loadProcesses(path);
            System.out.println("Loaded processes:");
            for (Process p : processes) {
                System.out.println(p);
            }
            
            FCFSScheduler fcfs = new FCFSScheduler();
            SchedulerResult r1 = fcfs.schedule(processes);

            System.out.println("===== FCFS =====");
            r1.getGantt().forEach(System.out::println);
            r1.getMetrics().values().forEach(System.out::println);

            SJFNonPreemptiveScheduler sjf = new SJFNonPreemptiveScheduler();
            SchedulerResult r2 = sjf.schedule(processes);

            System.out.println("\n===== SJF NON-PREEMPTIVE =====");
            r2.getGantt().forEach(System.out::println);
            r2.getMetrics().values().forEach(System.out::println);
            PriorityPreemptiveScheduler.run(processes);  //omar elgenidy part
        } catch (IOException e) {
            System.err.println("I/O error while reading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid process data: " + e.getMessage());
        }
    }
}
