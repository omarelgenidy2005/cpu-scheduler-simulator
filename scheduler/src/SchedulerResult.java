import java.util.List;
import java.util.Map;

public class SchedulerResult {

    private final List<GanttEntry> gantt;
    private final Map<String, ProcessMetrics> metrics;

    public SchedulerResult(List<GanttEntry> gantt, Map<String, ProcessMetrics> metrics) {
        this.gantt = gantt;
        this.metrics = metrics;
    }

    public List<GanttEntry> getGantt() {
        return gantt;
    }

    public Map<String, ProcessMetrics> getMetrics() {
        return metrics;
    }
}