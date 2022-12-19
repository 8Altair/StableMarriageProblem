package solver;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Reader
{
    private static final int NUM_THREADS = 4; // Number of threads to use for file reading and writing

    // Read preferences from file
    static List<int[]> readPreferencesFromFile(String fileName)
    {
        List<int[]> Preferences = new ArrayList<>();

        // Use a thread pool to read the file in parallel
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<int[]>> futures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                // Submit a task to read the preferences for a single person
                String finalLine = line;
                futures.add(executor.submit(() ->
                {
                    String[] preferenceStrings = finalLine.split(",");

                    int[] preferences = new int[preferenceStrings.length];
                    for (int i = 0; i < preferenceStrings.length; i++)
                    {
                        preferences[i] = Integer.parseInt(preferenceStrings[i]);
                    }
                    return preferences;
                }));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Wait for all tasks to complete and add the preferences to the list
        for (Future<int[]> future : futures)
        {
            try
            {
                Preferences.add(future.get());
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        return Preferences;
    }
}