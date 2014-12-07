package com.phoenix.common.collection;

import java.util.Comparator;
import java.util.Random;

import static java.lang.System.out;

public class TestHeapSort {
	/**
     * Sort alphabetically, case sensitive.
     * <p/>
     * Defines an alternate sort order for String.
     */
    private static class StringComparator implements Comparator<String>
        {
    // -------------------------- PUBLIC INSTANCE  METHODS --------------------------


        /**
         * Sort alphabetically, case sensitive.
         * Defines an alternate sort order for String.
         * Compare two String Objects.
         * Informally, returns (a-b), or +ve if a is more positive than b.
         *
         * @param a first String to compare
         * @param b second String to compare
         *
         * @return +ve if a&gt;b, 0 if a==b, -ve if a&lt;b
         */
        public final int compare( String a, String b )
            {
            return a.compareTo( b );
            }
        }

    // --------------------------- main() method ---------------------------

    // Test HeapSort by sorting N random Strings
    public static void main( String[] args )
        {
        int N = 100000;
        String[] anArray = new String[ N ];
        Random wheel = new Random( 149 );
        for ( int i = 0; i < anArray.length; i++ )
            {
            // keys of form A9999
            anArray[ i ] = "A" + ( ( wheel.nextInt() & Integer.MAX_VALUE ) % 10000 );
            }
        out.println( "Start HeapSort items=" + N );
        long start = System.currentTimeMillis();
        HeapSort.sort( anArray, new StringComparator() );
        long stop = System.currentTimeMillis();
        out.println( "Elapsed:"
                     + ( stop - start ) );
        // waste a little time to let user admire the results
        try
            {
            Thread.sleep( 3000 );
            }
        catch ( InterruptedException e )
            {
            }
        }// end main
    }// end class TestHeapSort
