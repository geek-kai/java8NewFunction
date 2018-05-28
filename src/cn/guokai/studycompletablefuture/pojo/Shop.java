package cn.guokai.studycompletablefuture.pojo;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Supplier;

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
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public double getPrice(String product) {
        return calculatePrice(product);
    }
    private static double calculatePrice(String product) {
        delay();
        Random random=new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

}


