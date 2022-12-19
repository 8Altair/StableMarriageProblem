package solver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Writer
{
    private static final int NUM_THREADS = 4; // Number of threads to use for file reading and writing

    static void writeMatchingsToFile(String fileName, int[] matchings)
    {
        // Use a thread pool to write the file in parallel
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<?>> futures = new ArrayList<>();

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < matchings.length; i++)
            {
                // Submit a task to write the matching for a single person
                final int matching = matchings[i];
                int finalI = i;
                BufferedWriter finalWriter = writer;
                futures.add(executor.submit(() ->
                {
                    try
                    {
                        finalWriter.write(finalI + "," + matching + "\n");
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }));
            }

            // Shut down the thread pool
            executor.shutdown();
            try
            {
                if (!executor.awaitTermination(15, TimeUnit.SECONDS))
                {
                    executor.shutdownNow();
                }
            }
            catch (InterruptedException e)
            {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures)
        {
            try
            {
                future.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        // Close the writer
        try
        {
            if (writer != null)
            {
                writer.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter("output.txt"));
            // Submit a task to process a single file
            BufferedWriter finalWriter = writer;
            futures.add(executor.submit(() ->
            {
                try
                {
                    // Write the matchings to the output file
                    for (int i = 0; i < matchings.length; i++)
                    {
                        finalWriter.write(i + "," + matchings[i] + "\n");
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }));

            // Shut down the thread pool
            executor.shutdown();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures)
        {
            try
            {
                future.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        // Close the writer
        try
        {
            if (writer != null)
            {
                writer.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
}
