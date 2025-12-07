import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            List<Process> processes = ProcessCSVreader.loadProcesses("processes.csv");

            FCFSScheduler fcfs = new FCFSScheduler();
            SchedulerResult fcfsResult = fcfs.schedule(processes);

            System.out.println("===== FCFS =====");
            fcfsResult.getGantt().forEach(System.out::println);
            fcfsResult.getMetrics().values().forEach(System.out::println);

            SJFNonPreemptiveScheduler sjf = new SJFNonPreemptiveScheduler();
            SchedulerResult sjfResult = sjf.schedule(processes);

            System.out.println("\n===== SJF NON-PREEMPTIVE =====");
            sjfResult.getGantt().forEach(System.out::println);
            sjfResult.getMetrics().values().forEach(System.out::println);

            MultiLevelQueueScheduler mlq = new MultiLevelQueueScheduler();
            SchedulerResult mlqResult = mlq.schedule(processes);

            System.out.println("\n===== MULTI-LEVEL QUEUE (MLQ) =====");
            mlqResult.getGantt().forEach(System.out::println);
            mlqResult.getMetrics().values().forEach(System.out::println);

            MultiLevelFeedbackQueueScheduler mlfq = new MultiLevelFeedbackQueueScheduler();
            SchedulerResult mlfqResult = mlfq.schedule(processes);

            System.out.println("\n===== MULTI-LEVEL FEEDBACK QUEUE (MLFQ) =====");
            mlfqResult.getGantt().forEach(System.out::println);
            mlfqResult.getMetrics().values().forEach(System.out::println);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
