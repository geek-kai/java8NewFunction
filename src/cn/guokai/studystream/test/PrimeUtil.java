package cn.guokai.studystream.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by 17629 on 2018/5/2.
 */
public class PrimeUtil {

    //判断是否是质数
    public static boolean isPrime(int candidate) {
        //优化设置
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    //利用流分割质数
    public static Map<Boolean,List<Integer>> partitionPrimes(int n){
        return IntStream.rangeClosed(2,n).boxed().collect(
                Collectors.partitioningBy(candidate->isPrime(candidate))
        );
    }
}
