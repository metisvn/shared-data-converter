package vn.simidoc.data.share.convert;

import java.util.Random;

public class ArrayShuffleUtil
{
    public static int[] GetShuffleExchanges(int size)
    {
        int[] exchanges = new int[size - 1];
        Random rand = new Random();
        for (int i = size - 1; i > 0; i--)
        {
            int n = rand.nextInt(i + 1);
            exchanges[size - 1 - i] = n;
        }
        return exchanges;
    }

    public static int[] Shuffle(int[] array)
    {
        int size = array.length;
        int[] exchanges = GetShuffleExchanges(size);
        for (int i = size - 1; i > 0; i--)
        {
            int n = exchanges[size - 1 - i];
            int tmp = array[i];
            array[i] = array[n];
            array[n] = tmp;
        }
        return array;
    }

}