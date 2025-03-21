package by.it.group310951.suponenko.lesson05;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;


/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии,
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class C_QSortOptimized {

    // Класс "отрезок"
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

    private void quickSort(Segment[] segments, int left, int right) {
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = segments[left];
            int i = left + 1;
            while (i <= gt) {
                int cmp = segments[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(segments, lt++, i++);
                } else if (cmp > 0) {
                    swap(segments, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - left < right - gt) {
                quickSort(segments, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(segments, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    private int binarySearch(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int pos = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                pos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return pos;
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int count = 0;
            int pos = binarySearch(segments, points[i]);
            if (pos == -1) {
                result[i] = 0;
                continue;
            }
            for (int j = pos; j >= 0; j--) {
                if (segments[j].stop >= points[i])
                    count++;
            }
            result[i] = count;
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson05/dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
}
