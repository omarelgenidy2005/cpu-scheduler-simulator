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
            // here you would pass `processes` to FCFS, SJF, etc.
            PriorityPreemptiveScheduler.run(processes);  //omar elgenidy part
        } catch (IOException e) {
            System.err.println("I/O error while reading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid process data: " + e.getMessage());
        }
    }
}
