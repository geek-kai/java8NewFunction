package cn.guokai.studystream.test;

import cn.guokai.studystream.collector.TolistCollector;
import cn.guokai.studystream.pojo.Dish;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by 17629 on 2018/5/2.
 */
public class MyCollectorDemo {

    List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH));
    @Test
    public void fun(){
        List<Integer> lists = Stream.iterate(0, n -> n + 1).limit(100000).collect(new TolistCollector<Integer>());
        for (Integer i :lists) {
            System.out.println(i);
        }
    }
}
