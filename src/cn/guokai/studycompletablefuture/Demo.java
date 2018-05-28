package cn.guokai.studycompletablefuture;

import cn.guokai.studycompletablefuture.pojo.Shop;
import org.junit.Test;

import java.util.concurrent.Future;

/**
 * Created by guokai on 2018/5/28.
 */
public class Demo {

    /**
     * 采用异步api模式 主线程不会因为getPriceByshopName方法陷入阻塞可以去执行别的任务
     *
     */
    @Test
    public void test1(){
        long start = System.nanoTime();
        Future<Double> futurePrice = Shop.getPriceByshopName("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime
                + " msecs");
        // 执行更多任务，比如查询其他商店
        System.out.println("模拟执行别的业务");

        // 在计算商品价格的同时
        try {
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }
}
