package cn.guokai.studycompletablefuture;

import cn.guokai.studycompletablefuture.pojo.Shop;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by guokai on 2018/5/28.
 */
public class Demo {
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
     * 采用异步api模式 主线程不会因为getPriceByshopName方法陷入阻塞可以去执行别的任务
     *
     */
    @Test
    public void test1(){
        long start = System.nanoTime();
        Future<Double> futurePrice = Shop.getPriceByGoodName("my favorite product");
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

    /**
     * 模拟一个在线商店列表并且查询某个商品价格
     * 同步不并行流处理
     */
    @Test
    public void test2(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<String> collect = shops.stream().map(shop -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(gooName))).collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    /**
     * 模拟一个在线商店列表并且查询某个商品价格
     * 同步并行流处理
     * 并行流会快很多
     * 因为多核处理任务
     */
    @Test
    public void test3(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<String> collect = shops.parallelStream().map(shop -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(gooName))).collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    /**
     * 模拟一个在线商店列表并且查询某个商品价格
     * 计算异步尝试 模式1
     * 这种模式下 其实 是顺序执行的
     * 关键点就是 流有延迟性！！！
     * CompletableFuture对象只有在真正使用的时候才会触发 因为join的原因造成了阻塞 这样效率很低
     *
     * 理解关键点 lambda的延迟性！
     *
     */
    @Test
    public void test4(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<String> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(
                () -> shop.getShopName() + "%s price is" +
                        shop.getPrice(gooName))).map(CompletableFuture::join).collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    /**
     * 模拟一个在线商店列表并且查询某个商品价格
     * 计算异步尝试 模式2
     * 两个map分开写 效率会快很多但还不是最高
     *
     */
    @Test
    public void test5(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<CompletableFuture<String>> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(
                () -> shop.getShopName() + "%s price is" +
                        shop.getPrice(gooName),executor)).collect(Collectors.toList());
        List<String> collect1 = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
        for (String s : collect1) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    /**
     *
     *
     */
    @Test
    public void test6(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<CompletableFuture<String>> collect = shops.parallelStream().map(shop -> CompletableFuture.supplyAsync(
                () -> shop.getShopName() + "%s price is" +
                        shop.getPrice(gooName),executor)).collect(Collectors.toList());
        List<String> collect1 = collect.parallelStream().map(CompletableFuture::join).collect(Collectors.toList());
        for (String s : collect1) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    /**
     * 两个map放一起利用并行流
     */
    @Test
    public void test7(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<String> collect = shops.parallelStream().map(shop -> CompletableFuture.supplyAsync(
                () -> shop.getShopName() + "%s price is" +
                        shop.getPrice(gooName),executor)).map(CompletableFuture::join).collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    /**
     *
     */
    @Test
    public void test8(){

        long start = System.nanoTime();
        String gooName="iphoneX";
        List<CompletableFuture<String>> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(
                () -> shop.getShopName() + "%s price is" +
                        shop.getPrice(gooName),executor)).collect(Collectors.toList());
        List<String> collect1 = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
        for (String s : collect1) {
            System.out.println(s);
        }
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }
}
