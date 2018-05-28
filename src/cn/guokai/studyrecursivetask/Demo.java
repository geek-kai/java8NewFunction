package cn.guokai.studyrecursivetask;

/**
 * Created by guokai on 2018/5/19.
 */
public class Demo
{

    public static void main(String[] args) {
        long n= ForkJoinSumCalculator.forkJoinSum(1000000);
        System.out.println(n);
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
