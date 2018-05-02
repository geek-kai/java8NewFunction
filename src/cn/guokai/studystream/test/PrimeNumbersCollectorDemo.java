package cn.guokai.studystream.test;

import cn.guokai.studystream.collector.PrimeNumbersCollector;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by 17629 on 2018/5/2.
 */
public class PrimeNumbersCollectorDemo {

    @Test
    public void fun(){
        Map<Boolean, List<Integer>> map = partitionPrimesWithCustomCollector(13);
        System.out.println(map);
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(new PrimeNumbersCollector());
    }
}
