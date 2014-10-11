package com.tongji.share.tools;

import static java.util.Collections.swap;

import java.util.Comparator;
import java.util.List;

public class Sort {
	public static <T extends Comparable<? super T>> int ascending(T t1, T t2) {
		return t1.compareTo(t2);
	}

	public static <T extends Comparable<? super T>> int descending(T t1, T t2) {
		return -ascending(t1, t2);
	}

//	public static <T extends Comparable<? super T>> 
//        void sort(List<T> list) {
//		sort(list, Sort::ascending); 
//		}

	public static <T> void sort(List<T> list, Comparator<? super T> c) {
		quickSort(list, 0, list.size() - 1, c);
	}

	private static <T> void quickSort(List<T> list, int left, int right,
			Comparator<? super T> c) {
		if (left < right) {
			int axis = partition(list, left, right, c);
			quickSort(list, left, axis - 1, c);
			quickSort(list, axis + 1, right, c);
		}
	}

	private static <T> int partition(List<T> list, int left, int right,
			Comparator<? super T> c) {
		T s = list.get(left);
		int axis = partitionUnprocessed(list, left + 1, right, s, c);
		swap(list, left, axis);
		return axis;
	}

	private static <T> int partitionUnprocessed(List<T> list, int left,
			int right, T s, Comparator<? super T> c) {
		int i = lookRight(list, left, right, s, c);
		int j = lookLeft(list, right, i, s, c);
		if (i < j) {
			swap(list, i, j);
			return partitionUnprocessed(list, i + 1, j - 1, s, c);
		}
		return j;
	}

	private static <T> int lookRight(List<T> list, int from, int to, T s,
			Comparator<? super T> c) {
		int i = from;
		while (i < to + 1 && c.compare(list.get(i), s) <= 0) {
			i++;
		}
		return i;
	}

	private static <T> int lookLeft(List<T> list, int from, int to, T s,
			Comparator<? super T> c) {
		int j = from;
		while (j > to - 1 && c.compare(list.get(j), s) > 0) {
			j--;
		}
		return j;
	}
	
//	public static void main(String[] args) {
////        List<Integer> list = 
////            new ArrayList<>(Arrays.asList(10, 9, 1, 2, 5, 3, 8, 7, 12, 11));
////        
////        sort(list);
////        out.println(list);
//        sort(list, c)
//        sort(list, Sort::descending);
//        out.println(list);
//    }
}