package cn.guokai.studycompletablefuture.pojo;

/**
 * Created by guokai on 2018/5/29.
 */

/**
 * 汇率转换
 */
public class ExchangeRate {
    public enum  Type{
        US("美元",0),
        CH("人名币",1),
        CU("欧元",2),
        JA("日元",3);
        private String name;

        private Integer value;

        Type(String name, Integer value) {
            this.name = name;
            this.value = value;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }
    public static Double conversion(Type type,Type type2){
        System.out.println("模拟执行汇率转化的线程开始"+Thread.currentThread().getName());
//        delay();
        System.out.println("模拟执行汇率转化的线程结束"+Thread.currentThread().getName());
        return 0.08;
    }

    private static void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
