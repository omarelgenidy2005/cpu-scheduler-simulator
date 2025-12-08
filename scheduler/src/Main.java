import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\omer\\OneDrive\\Desktop\\Operating Systems\\project\\project el os\\cpu-scheduler-simulator\\scheduler\\src\\processesok.csv";

        try {
            List<Process> processes = ProcessCSVreader.loadProcesses(path);
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





            RoundRobin rr = new RoundRobin();
            int quantum = 2;

            SchedulerResult rrResult = rr.schedule(processes, quantum);
            OutputFormatter.printScheduleReport("Round Robin (q=" + quantum + ")", rrResult);
          
            PriorityPreemptiveScheduler.run(processes); //omar elgnidy part

        } catch (IOException e) {
            System.err.println("I/O error while reading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid process data: " + e.getMessage());
        }
    }
}
