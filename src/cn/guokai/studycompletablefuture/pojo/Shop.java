package cn.guokai.studycompletablefuture.pojo;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by guokai on 2018/5/28.
 */
public class Shop {

    public static Future<Double> getPriceByshopName(String name){
        CompletableFuture <Double> future=new CompletableFuture<>();
        //计算交给别的线程 异步执行
        new Thread(()->{
            Double price=getPrice(name);
            //设置future的返回值
            future.complete(price);
        }).start();
        return future;
    }

    private static Double getPrice(String name) {

        //模拟延迟
        try {
            Thread.sleep(1000);
            Random random=new Random();
            return random.nextDouble() * name.charAt(0) + name.charAt(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}


