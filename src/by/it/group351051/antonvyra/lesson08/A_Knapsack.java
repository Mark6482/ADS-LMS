package by.it.group351051.antonvyra.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.


Sample Input:
10 3
1 4 8
Sample Output:
10

Sample Input 2:

15 3
2 8 16
Sample Output 2:
14

*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        // Read input
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt(); // Maximum weight capacity of the knapsack
        int n = scanner.nextInt(); // Number of different gold weights
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Initialize DP array
        int[] dp = new int[w + 1];

        // Solve the problem using dynamic programming
        for (int i = 1; i <= w; i++) {
            for (int j = 0; j < n; j++) {
                if (gold[j] <= i) {
                    dp[i] = Math.max(dp[i], dp[i - gold[j]] + gold[j]);
                }
            }
        }

        // The maximum weight that can be carried
        return dp[w];
    }


    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson08/dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
