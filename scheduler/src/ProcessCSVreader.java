import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProcessCSVreader {


    public static List<Process> loadProcesses(String filePath) throws IOException {
        List<Process> processes = new ArrayList<>();
        Set<String> usedPids = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;


                if (isFirstLine && looksLikeHeader(line)) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;

                String[] parts = line.split(",");
                if (parts.length != 4) {
                    throw new IllegalArgumentException(
                            "Invalid column count in line: \"" + line + "\". Expected 4 columns."
                    );
                }

                String pid = parts[0].trim();
                String arrivalStr = parts[1].trim();
                String burstStr = parts[2].trim();
                String priorityStr = parts[3].trim();

                int arrival;
                int burst;
                int priority;

                try {
                    arrival = Integer.parseInt(arrivalStr);
                    burst = Integer.parseInt(burstStr);
                    priority = Integer.parseInt(priorityStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Non-integer value in line: \"" + line + "\"", e
                    );
                }

                if (usedPids.contains(pid)) {
                    throw new IllegalArgumentException(
                            "Duplicate PID detected: " + pid
                    );
                }

                Process process = new Process(pid, arrival, burst, priority);
                processes.add(process);
                usedPids.add(pid);
            }
        }

        if (processes.isEmpty()) {
            throw new IllegalArgumentException("No valid processes found in file: " + filePath);
        }


        return processes;
    }

    private static boolean looksLikeHeader(String line) {
        String lower = line.toLowerCase();
        return lower.contains("pid") || lower.contains("arrival");
    }
}
