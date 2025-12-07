import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OutputFormatter {

    public static void printScheduleReport(String algorithmName, SchedulerResult result) {
        if (result == null || result.getMetrics() == null || result.getMetrics().isEmpty()) {
            System.out.println("No metrics to display for " + algorithmName);
            return;
        }

        List<GanttEntry> gantt = result.getGantt();
        List<ProcessMetrics> metrics =
                new ArrayList<>(result.getMetrics().values()); 

        System.out.println();
        System.out.println("====================================================");
        System.out.println("         " + algorithmName + " Scheduling Report    ");
        System.out.println("====================================================");
        System.out.println();

        if (gantt != null && !gantt.isEmpty()) {
            printGanttChart(gantt);
            System.out.println();
        }

        printProcessTable(metrics);
        System.out.println();
        printGlobalMetrics(metrics);
        System.out.println("====================================================");
        System.out.println();
    }

    

    private static void printGanttChart(List<GanttEntry> ganttEntries) {
        System.out.println("Gantt Chart (time on bottom):");

        if (ganttEntries.isEmpty()) {
            System.out.println("[no execution]");
            return;
        }

        StringBuilder top = new StringBuilder();
        StringBuilder middle = new StringBuilder();
        StringBuilder bottom = new StringBuilder();
        StringBuilder timeLine = new StringBuilder();

        int currentTime = ganttEntries.get(0).getStart();
        timeLine.append(currentTime);

        for (GanttEntry entry : ganttEntries) {
            String label = entry.getPid();
            int blockWidth = Math.max(label.length() + 2, 4); 

            top.append("+").append(repeat("-", blockWidth));
            middle.append("|").append(center(label, blockWidth));
            bottom.append("+").append(repeat("-", blockWidth));

            int end = entry.getEnd();
            String endStr = String.valueOf(end);
            int spaces = blockWidth - endStr.length() + 1;
            timeLine.append(repeat(" ", spaces)).append(end);
        }

        top.append("+");
        bottom.append("+");
        middle.append("|");

        System.out.println(top);
        System.out.println(middle);
        System.out.println(bottom);
        System.out.println(timeLine);
    }

   

    private static void printProcessTable(List<ProcessMetrics> metrics) {
        System.out.println("Per-Process Metrics:");
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-6s %-6s %-6s %-6s %-6s %-6s %-6s%n",
                "PID", "AT", "BT", "CT", "TAT", "WT", "RT");
        System.out.println("------------------------------------------------------------");

        metrics.stream()
                .sorted(Comparator.comparing(m -> m.pid))
                .forEach(m -> System.out.printf("%-6s %-6d %-6d %-6d %-6d %-6d %-6d%n",
                        m.pid,
                        m.arrivalTime,
                        m.burstTime,
                        m.completionTime,
                        m.turnaroundTime,
                        m.waitingTime,
                        m.responseTime));

        System.out.println("------------------------------------------------------------");
    }

  

    private static void printGlobalMetrics(List<ProcessMetrics> metrics) {
        int n = metrics.size();

        double sumWT = 0.0;
        double sumTAT = 0.0;
        double sumRT = 0.0;

        int minArrival = Integer.MAX_VALUE;
        int maxCompletion = Integer.MIN_VALUE;
        int totalBurst = 0;

        for (ProcessMetrics m : metrics) {
            sumWT += m.waitingTime;
            sumTAT += m.turnaroundTime;
            sumRT += m.responseTime;

            if (m.arrivalTime < minArrival) {
                minArrival = m.arrivalTime;
            }
            if (m.completionTime > maxCompletion) {
                maxCompletion = m.completionTime;
            }

            totalBurst += m.burstTime;
        }

        double avgWT = sumWT / n;
        double avgTAT = sumTAT / n;
        double avgRT = sumRT / n;

        int totalTime = Math.max(1, maxCompletion - minArrival);

        double throughput = (double) n / totalTime;
        double cpuUtil = (100.0 * totalBurst) / totalTime;

        System.out.println("Global Metrics:");
        System.out.printf("  Average Waiting Time    : %.2f time units%n", avgWT);
        System.out.printf("  Average Turnaround Time : %.2f time units%n", avgTAT);
        System.out.printf("  Average Response Time   : %.2f time units%n", avgRT);
        System.out.printf("  Throughput              : %.4f processes/unit time%n", throughput);
        System.out.printf("  CPU Utilization         : %.2f%%%n", cpuUtil);
    }

   

    private static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int totalPadding = width - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return repeat(" ", leftPadding) + text + repeat(" ", rightPadding);
    }
}
