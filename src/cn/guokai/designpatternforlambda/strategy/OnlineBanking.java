package cn.guokai.designpatternforlambda.strategy;

import java.util.function.Consumer;

/**
 * Created by guokai on 2018/5/22.
 * 利用lambda让模板模式更加灵活
 */
public class OnlineBanking {

    public void processCustomer(String str, Consumer<String> makeCustomerHappy){
         String s=str;
         //不同的业务执行不同的逻辑 利用方法传递lambda表达式
        makeCustomerHappy.accept(str);
    }
}
