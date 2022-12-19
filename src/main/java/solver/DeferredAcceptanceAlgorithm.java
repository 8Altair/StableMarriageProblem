package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static solver.Index.getIndex;
import static solver.Reader.readPreferencesFromFile;
import static solver.Writer.writeMatchingsToFile;

public class DeferredAcceptanceAlgorithm
{
    public static void algorithm()
    {
        // Read preferences from file
        List<int[]> menPreferences = readPreferencesFromFile("C:\\Users\\Altair\\Desktop\\Men.txt");
        List<int[]> womenPreferences = readPreferencesFromFile("C:\\Users\\Altair\\Desktop\\Women.txt");

        // Initialize men and women lists
        List<Integer> men = new ArrayList<>();
        List<Integer> women = new ArrayList<>();
        for (int i = 0; i < menPreferences.size(); i++)
        {
            men.add(i);
            women.add(i);
        }

        // Initialize matchings to empty lists
        int[] menMatches = new int[men.size()];
        Arrays.fill(menMatches, -1);
        int[] womenMatches = new int[women.size()];
        Arrays.fill(womenMatches, -1);

        // Run deferred acceptance algorithm
        while (!men.isEmpty())
        {   //System.out.println("Tu sam.");
            int man = men.remove(0);
            int[] manPreferences = menPreferences.get(man);
            int woman = manPreferences[0];

            if (womenMatches[woman] == -1)
            {
                // Woman is free, so make the match
                menMatches[man] = woman;
                womenMatches[woman] = man;
            }
            else
            {
                // Woman is not free, so check her preferences
                int[] womanPreferences = womenPreferences.get(woman);
                int currentPartner = womenMatches[woman];
                if (getIndex(womanPreferences, man) < getIndex(womanPreferences, currentPartner))
                {
                    // Woman prefers man, so make the match and add current partner back to list of men
                    menMatches[man] = woman;
                    womenMatches[woman] = man;
                    men.add(currentPartner);
                }
                else
                {
                    // Woman prefers current partner, so add man back to list of men
                    men.add(man);
                }
            }
        }
        //for (int menMatch : menMatches) System.out.println(menMatch);

        // Write matchings to file
        writeMatchingsToFile("C:\\Users\\Altair\\Desktop\\Matchings.txt", menMatches);
    }
}
