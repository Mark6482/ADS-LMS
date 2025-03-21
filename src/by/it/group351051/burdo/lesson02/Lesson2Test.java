package by.it.group351051.burdo.lesson02;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Lesson2Test {
    /*
    для прохождения тестов создайте JUnit-конфигурацию на свой пакет:
    Поля:
    Name:               Test burdo
    Test kind:          All in package
    Package:            by.it.group351051.burdo
    Search for test:    In whole project
    */


    @Test
    public void A_VideoRegistrator() {
        A_VideoRegistrator instance=new A_VideoRegistrator();
        double[] events=new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts=instance.calcStartTimes(events,1); //рассчитаем моменты старта, с длинной сеанса 1
        boolean ok=starts.toString().equals("[1.0, 2.2, 3.7, 5.5, 8.1]");
        assertTrue("slowA failed", ok);

        events=new double[]{1, 1.1, 0};
        starts=instance.calcStartTimes(events,1); //рассчитаем моменты старта, с длинной сеанса 1
        ok=starts.toString().equals("[0.0, 1.1]");
        assertTrue("slowA failed", ok);

        events=new double[]{1, 1.1, 1.11};;
        starts=instance.calcStartTimes(events,0.09); //рассчитаем моменты старта, с длинной сеанса 1
        ok=starts.toString().equals("[1.0, 1.1]");
        assertTrue("slowA failed", ok);
    }
    @Test
    public void B_Sheduler() {
        B_Sheduler instance = new B_Sheduler();
        B_Sheduler.Event[] events1 = {new B_Sheduler.Event(0, 3), new B_Sheduler.Event(0, 1), new B_Sheduler.Event(1, 2), new B_Sheduler.Event(3, 5),
                new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3), new B_Sheduler.Event(3, 6),
                new B_Sheduler.Event(2, 7), new B_Sheduler.Event(2, 3), new B_Sheduler.Event(2, 7), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(3, 5), new B_Sheduler.Event(2, 4), new B_Sheduler.Event(2, 3), new B_Sheduler.Event(3, 7),
                new B_Sheduler.Event(4, 5), new B_Sheduler.Event(6, 7), new B_Sheduler.Event(6, 9), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(8, 9), new B_Sheduler.Event(4, 6), new B_Sheduler.Event(8, 10), new B_Sheduler.Event(7, 10)
        };

        List<B_Sheduler.Event> starts = instance.calcStartTimes(events1, 0, 10);
        boolean ok=starts.toString().equals("[(0:1), (1:2), (2:3), (3:5), (6:7), (7:9)]");
        System.out.println(starts);

        assertTrue("B_Sheduler failed", ok);
        B_Sheduler.Event[] events2 = {
                new B_Sheduler.Event(0, 1), new B_Sheduler.Event(1, 2), new B_Sheduler.Event(1, 2), new B_Sheduler.Event(5, 7),
                new B_Sheduler.Event(4, 8), new B_Sheduler.Event(8, 9), new B_Sheduler.Event(8, 8), new B_Sheduler.Event(10, 11),
        };

        starts = instance.calcStartTimes(events2, 1, 9);


        ok=starts.toString().equals("[(1:2), (4:8), (8:8), (8:9)]");
        assertTrue("B_Sheduler failed", ok);


    }

    @Test
    public void C_GreedyKnapsack() throws Exception {
        String root=System.getProperty("user.dir")+"/src/";
        File f=new File(root+"by/it/group351051/burdo/lesson02/greedyKnapsack.txt");
        double costFinal=new C_GreedyKnapsack().calc(f);
        boolean ok=costFinal==200;
        assertTrue("B_Sheduler failed", ok);

        f=new File(root+"by/it/group351051/burdo/lesson02/greedyKnapsackWithZeroWeightItem.txt");
        costFinal=new C_GreedyKnapsack().calc(f);
        ok=costFinal==170.08;
        assertTrue("B_Sheduler failed", ok);
    }
}
