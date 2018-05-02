package cn.guokai.studystream.test;

import cn.guokai.study.pojo.Apple;
import cn.guokai.studystream.pojo.Dish;
import cn.guokai.studystream.utils.Utils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by 17629 on 2018/4/23.
 */
public class Demo {
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

    public enum CaloricLevel {DIET, NORMAL, FAT}

    ;

    @Test
    public void fun() {

        List<String> lowCaloricDishesName =
                menu.parallelStream()
                        .filter(dish -> dish.getCalories() < 400)
                        .sorted(Comparator.comparing(Dish::getCalories))
                        .map(Dish::getName)
                        .collect(Collectors.toList());

        for (String str : lowCaloricDishesName) {
            System.out.println(str);
        }
    }

    @Test
    public void fun1() {
        List<String> title = Arrays.asList("Java8", "In", "Action");
        Stream<String> stream = title.stream();
        List<String> list = new ArrayList<>();
        stream.forEach(list::add);
        for (String s : list
                ) {
            System.out.println(s);
        }
        //流只能遍历一次！！！
//        stream.forEach(System.out::print);
    }

    @Test
    public void fun2() {
        List<String> names =
                menu.stream()
                        .filter(d -> {
                            System.out.println("filtering" + d.getName());
                            return d.getCalories() > 300;
                        })
                        .map(d -> {
                            System.out.println("mapping" + d.getName());
                            return d.getName();
                        })
                        .limit(3)
                        .collect(Collectors.toList());
        System.out.println(names);
    }

    //选出两道荤菜
    @Test
    public void fun3() {
        menu.parallelStream().
                filter(dish -> dish.getType() == Dish.Type.MEAT).
                sorted(Comparator.comparing(Dish::getCalories)).
                map(Dish::getName).forEach(System.out::println);

        List<String> list = menu.parallelStream().
                filter(dish -> dish.getType() == Dish.Type.MEAT).
                sorted(Comparator.comparing(Dish::getCalories)).
                map(Dish::getName).collect(Collectors.toList());


        for (String str : list) {
            System.out.println(str);
        }

    }

    @Test
    public void fun4() {
        String words[] = {"hello", "world"};
        List<String> list = Arrays.stream(words)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        for (String str : list) {
            System.out.println(str);

        }


    }

    @Test
    public void fun5() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> list = numbers1.stream().flatMap(
                i -> numbers2.stream().filter(j -> (i + j) % 3 == 0).
                        map(j -> new int[]{i, j})
        ).collect(Collectors.toList());

        System.out.println(list.size());
    }


    @Test
    public void fun6() {
        //至少匹配一个
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu has last one vegetarian");
        }
    }

    @Test
    public void fun7() {
        menu.stream()
                .filter(dish -> {
                    System.out.println(dish.getName());
                    return dish.isVegetarian();
                })

                .findAny()
                .ifPresent(d -> System.out.println(d.getName()));
    }

    @Test
    public void fun8() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        int sum = numbers.stream().reduce(2, (a, b) -> a * b);
        Optional<Integer> reduce = numbers.stream().reduce((a, b) -> a * b);
        System.out.println(sum);
        reduce.ifPresent(System.out::println);

    }

    @Test
    public void fun9() {
        Integer sum = menu.stream().map(dish -> 1).reduce(0, (a, b) -> a + b);
        System.out.println(sum);
        System.out.println(menu.stream().count());
    }

    @Test
    public void fun10() {
        //数值流 没有自动装箱拆箱的成本 效率高
        int sum = menu.parallelStream().mapToInt(Dish::getCalories).sum();
        System.out.println(sum);
        OptionalInt optionalInt = menu.parallelStream().mapToInt(Dish::getCalories).max();
        optionalInt.ifPresent(System.out::println);
        IntStream range = IntStream.range(0, 0);
        System.out.println(range.max().isPresent());

    }

    @Test
    public void fun11() {
        Stream<double[]> stream = IntStream.rangeClosed(1, 100).boxed().flatMap(
                a -> IntStream.rangeClosed(a, 100).mapToObj(
                        b -> new double[]{a, b, Math.sqrt(a * a + b * b)}
                ).filter(t -> t[2] % 1 == 0)
        );
        stream.limit(40).forEach(t ->
                System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
    }

    @Test
    public void fun12() {
        try {

            Stream<String> lines = Files.lines(Paths.get("E:\\需求sql\\xuqiu2.txt"), Charset.forName("GBK"));
            long count = lines.flatMap(
                    line -> Arrays.stream(line.split(" "))).distinct().count();
            System.out.println(count);
//            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void fun13() {
        Stream.iterate(new int[]{0, 1}, n -> new int[]{n[1], n[0] + n[1]})
                .limit(20)
                .forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));

        Utils.test();

    }

    @Test
    public void fun14() {
        long howManyDishes = menu.stream().collect(counting());
        System.out.println(howManyDishes);
        Optional<Dish> collect = menu.stream().collect(maxBy(Comparator.comparing(Dish::getCalories)));


    }

    //汇总操作
    @Test
    public void fun15() {
        IntSummaryStatistics collect = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(menu.stream().collect(Collectors.summingInt(Dish::getCalories)));
        System.out.println(collect.getSum());
        System.out.println(collect.getAverage());
        DoubleSummaryStatistics collect1 = menu.stream().collect(Collectors.summarizingDouble(Dish::getCalories));
        System.out.println(collect1.getSum());
        System.out.println(collect1.getAverage());
        String str = menu.stream().map(Dish::getName).collect(joining(","));
        System.out.println(str);
        System.out.println(menu.stream().map(Dish::getName).collect(joining(",", "start menu", " endmenu")));


    }

    //汇总操作
    @Test
    public void fun16() {

        Optional<Dish> optional = menu.stream().collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
        optional.ifPresent(dish -> System.out.println(dish.getCalories()));
    }

    @Test
    public void fun17() {

        List<Integer> list = Arrays.asList();
        //如果为空获取值为0 这样写更安全 防止空值情况
        Integer integer = list.stream().reduce(Integer::sum).orElse(0);
        System.out.println(integer);
        System.out.println(list.stream().mapToInt(Integer::intValue).sum());

    }

    //分组
    @Test
    public void fun18() {

        //根据菜单类型分组
        Map<Dish.Type, List<Dish>> map = menu.parallelStream().collect(Collectors.groupingBy(Dish::getType));

        for (Map.Entry<Dish.Type, List<Dish>> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        //根据热量分组低热量 中热量 高热量
        Map<String, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400)
                        return "低热量";
                    else if (dish.getCalories() > 400 && dish.getCalories() <= 600)
                        return "中热量";
                    else
                        return "高热量";

                }));

        for (Map.Entry<String, List<Dish>> entry : dishesByCaloricLevel.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }


    }

    //多级分组
    @Test
    public void fun19() {
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                })
                        )
                );
        for (Map.Entry<Dish.Type, Map<CaloricLevel, List<Dish>>> entry : dishesByTypeCaloricLevel.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    //查找每个子组中热量最高的Dish
    @Test
    public void fun20() {

        Map<Dish.Type, Dish> map = menu.parallelStream().collect(Collectors.groupingBy(Dish::getType, collectingAndThen(
                maxBy(Comparator.comparing(Dish::getCalories)), Optional::get
        )));

        for (Map.Entry<Dish.Type, Dish> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

    }

    @Test
    public void fun21() {
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType =
                menu.stream().collect(
                        groupingBy(Dish::getType, mapping(
                                dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                },
                                toSet())));
        for (Map.Entry<Dish.Type, Set<CaloricLevel>> entry : caloricLevelsByType.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

    }

    @Test
    public void fun22(){
        Map<Boolean, Long> map = menu.parallelStream().collect(Collectors.partitioningBy(Dish::isVegetarian, Collectors.counting()));

        System.out.println(map);
    }

    @Test
    public void fun23(){

    }
}
