package cn.guokai.studycompletablefuture.pojo;

/**
 * Created by guokai on 2018/5/29.
 */
public class Discount {
    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
        private final int percentage;
        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    private static double apply(double price, Code code) {
        System.out.println(Thread.currentThread().getName()+"模拟计算折扣延迟");
        delay();
        System.out.println(Thread.currentThread().getName()+"模拟计算折扣延迟结束");
        return (price * (100 - code.percentage) / 100);

    }

    public static void delay() {
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " +
                Discount.apply(quote.getPrice(),
                        quote.getDiscountCode());
    }




}
