package cn.guokai.studystream.collector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by 17629 on 2018/5/2.
 */
public class TolistCollector<T> implements Collector<T,List<T>,List<T>> {

    //创建空的收集器
    @Override
    public Supplier<List<T>> supplier() {
        System.out.println(Thread.currentThread().getName()+" supplier");
        return ArrayList::new;
    }

    //累积遍历过的项目 原地修改项目
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        System.out.println(Thread.currentThread().getName()+" accumulator");

        return List::add;
    }


    //修改第一个累加器 将其与第二个累加器的值合并
    @Override
    public BinaryOperator<List<T>> combiner() {
        System.out.println(Thread.currentThread().getName()+" combiner");
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    //恒等函数
    @Override
    public Function<List<T>, List<T>> finisher() {
        System.out.println(Thread.currentThread().getName()+" finisher");

        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        System.out.println(Thread.currentThread().getName()+" characteristics");
        return Collections.unmodifiableSet(EnumSet.of(
                Characteristics.IDENTITY_FINISH,Characteristics.CONCURRENT));
    }
}
