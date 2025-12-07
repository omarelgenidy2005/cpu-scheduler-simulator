import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            
            List<Process> processes = ProcessCSVreader.loadProcesses("C:\\Users\\moham\\IdeaProjects\\cpu-scheduler-simulator-project\\scheduler\\src\\processesok.csv");

            System.out.println("Loaded processes:");
            for (Process p : processes) {
                System.out.println(p);
            }
            FCFSScheduler fcfs = new FCFSScheduler();
            SchedulerResult fcfsResult = fcfs.schedule(processes);
            OutputFormatter.printScheduleReport("FCFS", fcfsResult);

            PriorityNonPreemptiveScheduler scheduler =
                    new PriorityNonPreemptiveScheduler();

            scheduler.schedule(processes);

            SJFNonPreemptiveScheduler sjf = new SJFNonPreemptiveScheduler();
            SchedulerResult sjfResult = sjf.schedule(processes);
            OutputFormatter.printScheduleReport("SJF (Non-Preemptive)", sjfResult);

            MultiLevelQueueScheduler mlq = new MultiLevelQueueScheduler();
            SchedulerResult mlqResult = mlq.schedule(processes);
            OutputFormatter.printScheduleReport("Multi-Level Queue (MLQ)", mlqResult);

            MultiLevelFeedbackQueueScheduler mlfq = new MultiLevelFeedbackQueueScheduler();
            SchedulerResult mlfqResult = mlfq.schedule(processes);
            OutputFormatter.printScheduleReport("Multi-Level Feedback Queue (MLFQ)", mlfqResult);

            PriorityPreemptiveScheduler.run(processes);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
