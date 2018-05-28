package cn.guokai.designpatternforlambda.strategy;

/**
 * Created by guokai on 2018/5/22.
 * 利用lambda让策略者模式更加灵活
 */
public class Validator {
    private static  ValidationStrategy v ;

    public Validator(ValidationStrategy validationStrategy) {
        this.v=validationStrategy;
    }

    public  Boolean check(String s){
        return v.execute(s);
    }
}
