package cn.guokai.studycompletablefuture;

import cn.guokai.studycompletablefuture.pojo.Discount;
import cn.guokai.studycompletablefuture.pojo.ExchangeRate;
import cn.guokai.studycompletablefuture.pojo.Quote;
import cn.guokai.studycompletablefuture.pojo.Shop;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Consumer;
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
     *  thenCompose 两个异步存在依赖 第一个结束第二个才会开始
     *  thenComposeAsync 两个异步存在依赖 但是第二个异步会新开线程 用不好会造成浪费影响性能
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
//        经测试 stream 和异步流 差别很低 因为 之前的操作 其实所有的异步都在执行了
//        可以理解为 主分支上的join造成了主分支阻塞 但是异步已经在不同的线程异步阻塞了 所以执行时间为 模拟休眠时间之和
        List<String> collect = futures.stream().peek((shop) -> System.out.println("同步2stream " + Thread.currentThread().getName())).map(CompletableFuture::join).collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }


        System.out.println((System.nanoTime() - startTime) / 1000000);
        Scanner scanner=new Scanner(System.in);
        scanner.next();
    }


    /**
     * 当两个异步任务不存在依赖的时候
     * thenCombine 两个任务没有关联可以这样做 任务一不用等待任务2
     *
     *
     */
    @Test
    public void test4() {

        long startTime = System.nanoTime();
//        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(CompletableFuture.supplyAsync(() -> ExchangeRate.conversion(ExchangeRate.Type.CH, ExchangeRate.Type.CU), executor));
           CompletableFuture<Double> objectCompletableFuture = CompletableFuture.supplyAsync(() -> {
               System.out.println(Thread.currentThread().getName()+"开始执行汇率计算");return ExchangeRate.conversion(ExchangeRate.Type.CH, ExchangeRate.Type.CU);}, executor);

        Consumer<Shop> ns = (Shop shop) -> CompletableFuture.supplyAsync(() -> shop.getPrice("ns"), executor);

        //异步通知每个商户执行操作 即使异步还在延迟 （休眠模拟) 也会返回
        List<CompletableFuture<Double>> futures = shops.stream().peek((shop) -> System.out.println("同步1stream " + Thread.currentThread().getName())).map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice("ns"), executor))
                .map(future -> future.thenCombineAsync(
                        objectCompletableFuture, (price, rate) -> {
                            System.out.println("汇率 "+rate+"价格"+price);return  price*rate;}
                )).collect(Collectors.toList());
        System.out.println("end");
        //经测试 stream 和异步流 差别很低 因为 之前的操作 其实所有的异步都在执行了
        //可以理解为 主分支上的join造成了主分支阻塞 但是异步已经在不同的线程异步阻塞了 所以执行时间为 模拟休眠时间之和
        List<Double> collect = futures.stream().peek((shop) -> System.out.println("同步2stream " + Thread.currentThread().getName())).map(CompletableFuture::join).collect(Collectors.toList());
        for (Double s : collect) {
            System.out.println(s);
        }

        System.out.println((System.nanoTime() - startTime) / 1000000);
    }

    /**
     * 模拟商店计算完价格实时打出不等待 不是同时打出
     */
    @Test
    public void test5()  {
        long startTime = System.nanoTime();
        CompletableFuture[] myPhone27S = findPricesStream("myPhone27S")
                .map(f -> f.thenAccept(
                        s -> System.out.println(s + " (done in " +
                                ((System.nanoTime() - startTime) / 1_000_000) + " msecs)")))
                .toArray(size -> new CompletableFuture[size]);
        //allof 保证了所有的商家都会执行完方法  anyof相反获得第一个执行完的
        //如果不这样做 主线程main跑完了 可能导致有些商家线程还没有执行完所以不会打印出商品信息
        System.out.println("end");
        CompletableFuture.allOf(myPhone27S).join();
        System.out.println("All shops have now responded in "
                + ((System.nanoTime() - startTime) / 1_000_000) + " msecs");


    }

    /**
     * 利用一个规约操作也能达到 test5的效果
     * 不过test5可能从理解层面更容易理解
     *
     */
    @Test
    public void test6(){
        long startTime = System.nanoTime();
        List<CompletableFuture<Void>> myPhone27S = findPricesStream("myPhone27S")
                .map(f -> f.thenAccept(
                        s -> System.out.println(s + " (done in " +
                                ((System.nanoTime() - startTime) / 1_000_000) + " msecs)")))
                .collect(Collectors.toList());
        myPhone27S.stream().map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println("All shops have now responded in "
                + ((System.nanoTime() - startTime) / 1_000_000) + " msecs");

    }

    private Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream().peek((shop) -> System.out.println(product + Thread.currentThread().getName())).map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceandDiscountCode("ns"), executor)).map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)));
    }
}
