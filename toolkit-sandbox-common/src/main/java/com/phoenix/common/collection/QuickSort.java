package com.phoenix.common.collection;

import static java.lang.System.out;

import java.util.Comparator;
import java.util.Random;

public class QuickSort<T> {
	// ------------------------------ CONSTANTS ------------------------------

	private static final boolean DEBUGGING = false;


	// ------------------------------ FIELDS ------------------------------

	// callback object we are passed that has
	// a Comparator( <? super T> a, <? super T> b ) method.
	private Comparator<? super T> comparator;

	// pointer to the array of user's objects we are sorting
	private T[] userArray;

	// -------------------------- PUBLIC STATIC METHODS
	// --------------------------

	/**
	 * sort the user's array
	 *
	 * @param userArray
	 *            Array of Objects to be sorted.
	 * @param comparator
	 *            Comparator delegate that can compare two Objects and tell
	 *            which should come first.
	 */
	public static <T> void sort(T[] userArray, Comparator<? super T> comparator) {
		QuickSort<T> h = new QuickSort<T>();
		h.comparator = comparator;
		h.userArray = userArray;
		if (h.isAlreadySorted()) {
			return;
		}
		h.quicksort(0, userArray.length - 1);
		if (h.isAlreadySorted()) {
			return;
		}
		if (DEBUGGING) {
			// debug ensure sort is working
			if (!h.isAlreadySorted()) {
				out.println("Sort failed");
			}
		}
	}// end sort

	// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * dummy constructor to prevent its use. Use static method sort.
	 */
	private QuickSort() {
	}

	// -------------------------- OTHER METHODS --------------------------

	// check if user's array is already sorted
	private boolean isAlreadySorted() {
		for (int i = 1; i < userArray.length; i++) {
			if (comparator.compare(userArray[i], userArray[i - 1]) < 0) {
				return false;
			}
		}
		return true;
	}// end isAlreadySorted

	// Partition by splitting this chunk to sort in two and
	// get all big elements on one side of the pivot and all
	// the small elements on the other.
	private int partition(int lo, int hi) {
		// If set were unsorted lo is as good as any other a guess at a suitable
		// mid point.
		// If set were almost sorted, (lo+hi/2) would be a better guess.
		// I have left it as it was in the original.
		T pivot = userArray[lo];
		while (true) {
			// A specialised sort for strings might avoid comparing the the
			// first few common chars to all
			// Strings in the partition.
			while (comparator.compare(userArray[hi], pivot) >= 0 && lo < hi) {
				hi--;
			}
			while (comparator.compare(userArray[lo], pivot) < 0 && lo < hi) {
				lo++;
			}
			if (lo < hi) {
				// exchange objects on either side of the pivot
				T temp = userArray[lo];
				userArray[lo] = userArray[hi];
				userArray[hi] = temp;
			} else {
				return hi;
			}
		}// end while
	}// end partition

	// recursive quicksort that breaks array up into sub
	// arrays and sorts each of them.
	private void quicksort(int p, int r) {
		if (p < r) {
			int q = partition(p, r);
			if (q == r) {
				q--;
			}
			quicksort(p, q);
			quicksort(q + 1, r);
		}// end if
	}// end quicksort
}// end class QuickSort
