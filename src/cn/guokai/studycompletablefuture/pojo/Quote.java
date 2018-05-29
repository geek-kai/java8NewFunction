package cn.guokai.studycompletablefuture.pojo;

/**
 * Created by guokai on 2018/5/29.
 */
public class Quote {

    private  String shopName;
    private  double price;
    private  Discount.Code discountCode;

    public Quote(String shopName, double price, Discount.Code discountCode) {
        this.shopName = shopName;
        this.price = price;
        this.discountCode = discountCode;
    }

    public static Quote parse(String s) {
        System.out.println("执行Quote的线程"+Thread.currentThread().getName());
        String[] split = s.split(":");
        String shopName = split[0];
        double price = Double.parseDouble(split[1]);
        Discount.Code discountCode = Discount.Code.valueOf(split[2]);
        return new Quote(shopName, price, discountCode);
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Discount.Code getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(Discount.Code discountCode) {
        this.discountCode = discountCode;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "shopName='" + shopName + '\'' +
                ", price=" + price +
                ", discountCode=" + discountCode +
                '}';
    }
}
