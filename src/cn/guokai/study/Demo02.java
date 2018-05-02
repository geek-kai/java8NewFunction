package cn.guokai.study;

import cn.guokai.study.pojo.Apple;
import cn.guokai.study.pojo.Letter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by 17629 on 2018/4/23.
 */
public class Demo02 {

    public static void main(String[] args) {

        List<Integer> integers = Arrays.asList(1, 21, 2, 3);

        Consumer<Integer> consumer = (Integer j) -> {
            j = j + 1;
            System.out.println(j);
        };


        int i = 10;

        consumer.accept(i);
        i = i + 1;
        System.out.println(i);
    }

    @Test
    public void fun() {
        int portNumber = 1337;
        Runnable r = () -> System.out.println(portNumber);
//        portNumber = portNumber+ 31337;
    }

    @Test
    public void fun2() {
        List<String> str = Arrays.asList("a", "b", "A", "B");
        str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        for (String s : str
                ) {
            System.out.println(s);
        }

        str.sort(String::compareToIgnoreCase);

        for (String s : str
                ) {
            System.out.println(s);
        }
    }


    @Test
    public void fun3() {
        Function<Integer, Apple> function = Apple::new;
        Apple apply = function.apply(10);
        System.out.println(apply.getWeight());
    }

    @Test
    public void fun4() {
        List<Apple> list = Arrays.asList(
                new Apple(10, "ceshi1"),
                new Apple(120, "ceshi2"),
                new Apple(1, "ceshi312"),
                new Apple(3, "ceshi1"),
                new Apple(4, "ceshi1"),
                new Apple(4, "ceshi2"));

        list.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getName).reversed());

        for (Apple apple :list
                ) {
            System.out.println(apple.getWeight()+"---->"+apple.getName());
        }
    }

    @Test
    public void fun5() {

        Function<String, String> addHeader = Letter::addHeader;
        Function<String, String> transformationPipeline
                = addHeader.andThen(Letter::checkSpelling)
                .andThen(Letter::addFooter);
        System.out.println(transformationPipeline.apply("text labda"));
    }
}
