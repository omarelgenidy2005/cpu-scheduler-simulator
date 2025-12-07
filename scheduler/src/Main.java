import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            List<Process> processes = ProcessCSVreader.loadProcesses("processes.csv");

            FCFSScheduler fcfs = new FCFSScheduler();
            SchedulerResult fcfsResult = fcfs.schedule(processes);
            OutputFormatter.printScheduleReport("FCFS", fcfsResult);

            SJFNonPreemptiveScheduler sjf = new SJFNonPreemptiveScheduler();
            SchedulerResult sjfResult = sjf.schedule(processes);
            OutputFormatter.printScheduleReport("SJF (Non-Preemptive)", sjfResult);

            MultiLevelQueueScheduler mlq = new MultiLevelQueueScheduler();
            SchedulerResult mlqResult = mlq.schedule(processes);
            OutputFormatter.printScheduleReport("Multi-Level Queue (MLQ)", mlqResult);

            MultiLevelFeedbackQueueScheduler mlfq = new MultiLevelFeedbackQueueScheduler();
            SchedulerResult mlfqResult = mlfq.schedule(processes);
            OutputFormatter.printScheduleReport("Multi-Level Feedback Queue (MLFQ)", mlfqResult);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
