package solver;

public class Index
{
    // Get the index of an element in an array
    static int getIndex(int[] array, int element)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == element)
            {
                return i;
            }
        }
        return -1;
    }
}
