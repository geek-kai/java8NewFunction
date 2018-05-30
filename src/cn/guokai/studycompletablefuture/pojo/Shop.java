package cn.guokai.studycompletablefuture.pojo;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by guokai on 2018/5/28.
 */
public class Shop {

    private String shopName;

    public static Future<Double> getPriceByGoodName(String goodName) {
//        CompletableFuture<Double> future = new CompletableFuture<>();
//        //计算交给别的线程 异步执行
//        new Thread(() -> {
//            try {
//                Double price = getPrice(name);
//                //设置future的返回值
//                future.complete(price);
//            }catch (Exception ex){
//                future.completeExceptionally(ex);
//            }
//        }).start();
//        return future;
        /*
        supplyAsync方法接受一个生产者（Supplier）作为参数，返回一个CompletableFuture
        对象，该对象完成异步执行后会读取调用生产者方法的返回值。生产者方法会交由ForkJoinPool
        池中的某个执行线程（Executor）运行，但是你也可以使用supplyAsync方法的重载版本，传
        递第二个参数指定不同的执行线程执行生产者方法。
         */
        return CompletableFuture.supplyAsync(() -> calculatePrice(goodName));
    }

    public Shop(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public static void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public double getPrice(String product) {
        return calculatePrice(product);
    }
    private static double calculatePrice(String product) {
        System.out.println(Thread.currentThread().getName()+"模拟shop网络延迟");
        randomDelay();
        Random random=new Random();
        System.out.println(Thread.currentThread().getName()+"shop延迟结束");
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public String getPriceandDiscountCode(String product) {
        double price = calculatePrices(product);
        Random random=new Random();
        Discount.Code code = Discount.Code.values()[
                random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", shopName, price, code);
    }
    private double calculatePrices(String product) {
        System.out.println(Thread.currentThread().getName()+"模拟shop网络延迟");
        randomDelay();
        System.out.println(Thread.currentThread().getName()+"shop延迟结束");
        Random random=new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public static void randomDelay() {
        Random random=new Random();
        int delay = 500 + random.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }






}


