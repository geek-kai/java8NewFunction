package cn.guokai.studystream.test;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by 17629 on 2018/5/2.
 */
public class PrimeDemo {

    @Test
    public void test(){
        Map<Boolean, List<Integer>> map = PrimeUtil.partitionPrimes(13);
        System.out.println(map);
        boolean b = IntStream.rangeClosed(2, 2)
                .noneMatch(i -> 2 % i == 0);
        System.out.println(b);

    }
}
