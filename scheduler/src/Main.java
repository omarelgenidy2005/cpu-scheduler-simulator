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
            
            List<Process> processes = ProcessCSVreader.loadProcesses("C:\\Users\\moham\\IdeaProjects\\cpu-scheduler-simulator-project\\scheduler\\src\\processesok.csv");

            System.out.println("Loaded processes:");
            for (Process p : processes) {
                System.out.println(p);
            }
            FCFSScheduler fcfs = new FCFSScheduler();
            SchedulerResult fcfsResult = fcfs.schedule(processes);
            OutputFormatter.printScheduleReport("FCFS", fcfsResult);

            PriorityNonPreemptiveScheduler scheduler = new PriorityNonPreemptiveScheduler();

            scheduler.schedule(processes);

            SJFNonPreemptiveScheduler sjf = new SJFNonPreemptiveScheduler();
            SchedulerResult sjfResult = sjf.schedule(processes);
            OutputFormatter.printScheduleReport("SJF (Non-Preemptive)", sjfResult);

            System.out.println("===== FCFS =====");
            r1.getGantt().forEach(System.out::println);
            r1.getMetrics().values().forEach(System.out::println);

            SJFNonPreemptiveScheduler sjf = new SJFNonPreemptiveScheduler();
            SchedulerResult r2 = sjf.schedule(processes);

            PriorityPreemptiveScheduler.run(processes); //omar elgnidy part

        } catch (IOException e) {
            System.err.println("I/O error while reading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid process data: " + e.getMessage());
        }
    }
}
