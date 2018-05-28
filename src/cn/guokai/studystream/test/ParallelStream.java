package cn.guokai.studystream.test;

import org.junit.Test;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by guokai on 2018/5/3.
 */
public class ParallelStream {

    public static long  measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + sum);
            System.out.println(Thread.currentThread().getName());
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    public static void main(String[] args) {
        //性能测试 采用iterate
        System.out.println("Sequential sum done in:" +
                measureSumPerf(n-> Stream.iterate(1L, i -> i + 1)
                        .limit(n)
                        .reduce(0L, Long::sum), 10_000_000) + " msecs");

        //采用java底层实现 java底层快
        System.out.println("Sequential sum done in:" +
                measureSumPerf(n-> {long result = 0;
                    for (long i = 1L; i <= n; i++) {
                        result += i;
                    }
                    return result;}, 10_000_000) + " msecs");
        //并行流更慢 因为这里的iterate是一个整体 没办法分块 所有并行流用不好会很慢 而且iterate还涉及到自动装箱的操作 很影响性能
        System.out.println("Sequential sum done in:" +
                measureSumPerf(n-> Stream.iterate(1L, i -> i + 1)
                        .limit(n).parallel()
                        .reduce(0L, Long::sum), 10_000_000) + " msecs");

    }

    //正确的并行流处理方式
    @Test
    public void fun(){
        System.out.println("Parallel range sum done in:" +
                measureSumPerf(ParallelStream::parallelRangedSum, 10_000_000) +
                " msecs");
    }

    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }
}
