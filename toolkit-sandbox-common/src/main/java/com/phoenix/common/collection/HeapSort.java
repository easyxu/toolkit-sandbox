package com.phoenix.common.collection;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Comparator;

import static java.lang.System.out;

/**
 * Source for Williams and Floyd's TopDown HeapSort.
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.7 2008-01-01 add generics to Comparator.
 * @since 1996
 */
public class HeapSort<T>
    {
    // ------------------------------ CONSTANTS ------------------------------

    private static final boolean DEBUGGING = false;


    // ------------------------------ FIELDS ------------------------------


    // callback object we are passed that has
    // a Comparator( <? super T> a, <? super T> b ) method.
    private Comparator<? super T> comparator;

    // Sort work area.
    // Arranged as a binary tree.
    // Each boss has two underling peons.
    // Peons of a boss are stored in 2i+1 and 2i+2.
    // They are both equal to or smaller than him.
    // The peons may be in either order.
    // The superboss, who has the biggest key, lives in slot 0.
    private T[] offices;

    // pointer to the array of user's objects we are sorting
    private T[] userArray;

    // last used office index. Current number of employees-1;
    private int lastIndex = -1;

    // -------------------------- PUBLIC STATIC METHODS --------------------------


    // create a HeapSort object and sort the user's array
    @SuppressWarnings("unchecked")
    public static <T> void sort( T[] userArray, Comparator<? super T> comparator )
        {
        HeapSort<T> h = new HeapSort<T>();
        h.userArray = userArray;
        h.comparator = comparator;
        if ( h.isAlreadySorted() )
            {
            return;
            }
        // cannot allocate an array of T
        // I don't know generics well enough to get rid of the warning.
        h.offices = ( T[] ) new Object[ userArray.length ];// work area
        h.lastIndex = -1;
        h.assignOffices();
        h.raidAllEmployeesBiggestFirst();
        if ( DEBUGGING )
            {
            if ( !h.isAlreadySorted() )
                {
                out.println( "Sort failed" );
                }
            }
        }// end sort

    // -------------------------- OTHER METHODS --------------------------

    // Assign all the user's objects to an office.
    // They are likely partially presorted, so we
    // work from the end back, assigning the
    // objects with likely biggest keys first
    // to the lowest numbered office slots.
    // We then allow each newcomer to challenge
    // his bosses for dominance.
    private void assignOffices()
        {
        for ( int i = userArray.length - 1; i >= 0; i-- )
            {
            int juniorIndex = ++lastIndex;
            offices[ juniorIndex ] = userArray[ i ];
            challenge( juniorIndex );
            }// end for
        }// end assignOffices

    // Promote challenger until he finds a boss
    // with a bigger key than him or he becomes the superboss.
    private void challenge( int challengerIndex )
        {
        T challenger = offices[ challengerIndex ];
        while ( challengerIndex > 0 )
            {
            int bossIndex = ( challengerIndex - 1 ) / 2;
            T boss = offices[ bossIndex ];
            // Clever generics should get rid of the warning here.
            // I don't understand generics well enough to fix this.
            if ( comparator.compare( boss, challenger ) < 0 )
                {
                // Boss is smaller than challenger
                // demote the boss.
                offices[ challengerIndex ] = boss;
                // Let challenger have his office.
                challengerIndex = bossIndex;
                }
            else
                {
                break;
                }
            // challenger has been promoted enough
            }// end while
        // install the challenger in his new office
        offices[ challengerIndex ] = challenger;
        }// end Challenge

    // check if user's array is already sorted
    private boolean isAlreadySorted()
        {
        for ( int i = 1; i < userArray.length; i++ )
            {
            // Clever generics should get rid of the warning here.
            // I don't understand generics well enough to fix this.
            if ( comparator.compare( userArray[ i ], userArray[ i - 1 ] ) < 0 )
                {
                return false;
                }
            }
        return true;
        }// end isAlreadySorted

    // There is a vacancy in the lowest echelon.
    // Move the most junior person in to fill it.
    // Then let him challenge his bosses.
    private void promoteJuniorToVacancy( int vacantIndex )
        {
        if ( vacantIndex < lastIndex )
            {
            offices[ vacantIndex ] = offices[ lastIndex ];
            challenge( vacantIndex );
            }
        }// end promoteJuniorToVacancy

    // Remove the employees biggest first.
    // Record this ordering in the userArray.
    // Future versions may store it in
    // offices[i] in the newly vacated slot
    // and leave userArray undisturbed.
    // Promote as necessary to fill the vacancy.
    private void raidAllEmployeesBiggestFirst()
        {
        for ( int i = offices.length - 1; i >= 0; i-- )
            {
            // raid the SuperBoss
            userArray[ i ] = offices[ 0 ];
            replaceVacancy( 0 );
            }
        }// end raidAllEmployeesBiggestFirst

    // Promote as necessary to fill the vacancy
    // and cascading vacancies.
    // When we are done, the Office list is one shorter.
    @SuppressWarnings({ "SameParameterValue" })
    private void replaceVacancy( int vacantIndex )
        {
        while ( vacantIndex < lastIndex )
            {
            int peonAIndex = vacantIndex * 2 + 1;
            if ( peonAIndex > lastIndex )
                {// There are no peons.
                promoteJuniorToVacancy( vacantIndex );
                break;
                }
            T peonA = offices[ peonAIndex ];
            int peonBIndex = peonAIndex + 1;
            if ( peonBIndex > lastIndex )
                {// There is only one peon.
                // Promote peonA.
                offices[ vacantIndex ] = peonA;
                // peonA's office is now vacant.
                vacantIndex = peonAIndex;
                promoteJuniorToVacancy( vacantIndex );
                break;
                }
            // There are two peons.
            T peonB = offices[ peonBIndex ];
            // Clever generics should get rid of the warning here.
            // I don't understand generics well enough to fix this.
            if ( comparator.compare( peonA, peonB ) < 0 )
                {// peonA is smaller.
                // Promote peonB
                offices[ vacantIndex ] = peonB;
                // peonB's office is now vacant.
                vacantIndex = peonBIndex;
                }
            else
                {// peonA is bigger or equal to peonB.
                // Promote peonA
                offices[ vacantIndex ] = peonA;
                // peonA's office is now vacant.
                vacantIndex = peonAIndex;
                }
            }// end while
        // the last office is now vacant.
        lastIndex--;
        }// end replaceVacancy
    }// end HeapSort
