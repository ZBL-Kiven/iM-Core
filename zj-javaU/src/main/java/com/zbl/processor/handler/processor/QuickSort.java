package com.zbl.processor.handler.processor;
import java.util.List;

/**
 * Created by ZJJ on 19/12/11
 * A java quick-sort utils
 * Supports data that implements the comparable interface for efficient and fast sorting.
 * the tool algorithm complexity is O(n*log.n)
 */

class QuickSort {

    static <T extends Comparable<? super T>> void sort(List<T> lst) {
        sort(lst, 0, lst.size() - 1, 16);
    }

    /**
     * @param arr   the array to be sorted
     * @param left  leftClosed
     * @param right rightClosed
     * @param k     When recursive recursion to the sub-problem size <= k, use insert sort optimization
     * @param <T>   generic comparable type to be sorted
     */
    private static <T extends Comparable<? super T>> void sort(List<T> arr, int left, int right, int k) {
        if (right - left <= k) {
            insertionSort(arr, left, right);
            return;
        }
        if (left >= right) return;
        int[] ret = partition(arr, left, right);
        sort(arr, left, ret[0], k);
        sort(arr, ret[1], right, k);
    }

    private static <T extends Comparable<? super T>> void insertionSort(List<T> arr, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            T cur = arr.get(i);
            int j = i - 1;
            for (; j >= 0 && cur.compareTo(arr.get(j)) < 0; j--) {
                arr.set(j + 1, arr.get(j));
            }
            arr.set(j + 1, cur);
        }
    }

    private static <T extends Comparable<? super T>> int[] partition(List<T> arr, int left, int right) {
        swap(arr, left, (int) (Math.random() * (right - left + 1) + left));
        T base = arr.get(left);
        int i = left;
        int j = right;
        int cur = i;
        while (cur <= j) {
            if (arr.get(cur).compareTo(base) == 0) {
                cur++;
            } else if (arr.get(cur).compareTo(base) < 0) {
                swap(arr, cur++, i++);
            } else {
                swap(arr, cur, j--);
            }
        }
        return new int[]{i - 1, j + 1};
    }

    private static <T> void swap(List<T> arr, int i, int j) {
        if (i != j) {
            T temp = arr.get(i);
            arr.set(i, arr.get(j));
            arr.set(j, temp);
        }
    }
}