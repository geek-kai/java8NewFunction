package cn.guokai.studycompletablefuture;

import cn.guokai.studycompletablefuture.pojo.Discount;
import cn.guokai.studycompletablefuture.pojo.Quote;
import cn.guokai.studycompletablefuture.pojo.Shop;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by guokai on 2018/5/29.
 */
public class Demo02 {
    List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("newShop"), new Shop("newShop")
            , new Shop("newShop"), new Shop("newShop"), new Shop("newShop"), new Shop("newShop")
            , new Shop("newShop"), new Shop("newShop"), new Shop("newShop"));
    /**
     * 自定义线程池大小充分发挥效率
     */
    private final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                    new ThreadFactory() {
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        }
                    });

    /**
     * 采用分布式流
     * 因为测试机 jvm只有7个线程 所以每次 只会执行7个
     * 执行时间 size/7 * 休眠时间
     * 这种情况下 利用分布式流不会比异步效率高
     */
    @Test
    public void test() {

        long startTime = System.nanoTime();
        List<String> ps4 = shops.parallelStream().map(shop -> shop.getPriceandDiscountCode("ps4")).map(Quote::parse).map(Discount::applyDiscount).collect(Collectors.toList());

        for (String s : ps4) {
            System.out.println(s);
        }

        System.out.println((System.nanoTime() - startTime) / 1000000);
    }

    /**
     * 经过代码输出验证
     * 这样做和 test3的区别是
     * 因为分布式流的影响 会随机开线程 每个线程又会随机开一定的 异步线程 执行逻辑
     * test3的模式是一个主线程 异步线程执行
     * 效率对比 几乎没差别
     * 思考 test2 会不会开的线程池太多造成资源浪费
     * 总结 test3的方式更加推荐
     */
    @Test
    public void test2() {

        long startTime = System.nanoTime();
        shops.parallelStream().peek((shop) -> System.out.println("异步Stream" + Thread.currentThread().getName())).map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceandDiscountCode("ns"), executor)).map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor))).map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println((System.nanoTime() - startTime) / 1000000);
    }

    /**
     * 利用同步异步以及制定的线程池提高效率
     */
    @Test
    public void test3() {

        long startTime = System.nanoTime();
        //异步通知每个商户执行操作 即使异步还在延迟 （休眠模拟) 也会返回
        List<CompletableFuture<String>> futures = shops.stream().peek((shop) -> System.out.println("同步1stream " + Thread.currentThread().getName())).map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceandDiscountCode("ns"), executor)).map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)))
                .collect(Collectors.toList());
        System.out.println("end");
        //经测试 stream 和异步流 差别很低 因为 之前的操作 其实所有的异步都在执行了
        //可以理解为 主分支上的join造成了主分支阻塞 但是异步已经在不同的线程异步阻塞了 所以执行时间为 模拟休眠时间之和
        futures.stream().peek((shop) -> System.out.println("同步2stream " + Thread.currentThread().getName())).map(CompletableFuture::join).collect(Collectors.toList());


        System.out.println((System.nanoTime() - startTime) / 1000000);
    }
}
