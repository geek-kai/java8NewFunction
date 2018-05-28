package cn.guokai.designpatternforlambda.strategy;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by guokai on 2018/5/22.
 */
public class Demo {

    /**
     * 从某种程度来说lambda表达式更符合策略模式的设计
     * 不需要写具体的实现类 某种意义上来说去除了冗余让代码更简洁！
     */
    @Test
    public void test(){
        Validator numericValidator =
                new Validator((s) -> s.matches("[a-z]*"));
        boolean b1 = numericValidator.check("asd");
        Validator lowerCaseValidator =
                new Validator((s) -> s.matches("\\d+"));
        boolean b2 = lowerCaseValidator.check("123");

        System.out.println(b1);

        System.out.println(b2);
    }

    @Test
    /**
     * 利用lambda实现责任链模式
     */
    public void test2(){
        UnaryOperator<String> headerProcessing =
                (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing =
                (String text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline =
                headerProcessing.andThen(spellCheckerProcessing);
        String result = pipeline.apply("Aren't labdas really sexy?!!");
        System.out.println(result);
    }
}
