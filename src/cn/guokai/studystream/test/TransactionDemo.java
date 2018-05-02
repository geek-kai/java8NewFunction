package cn.guokai.studystream.test;

import cn.guokai.studystream.pojo.Trader;
import cn.guokai.studystream.pojo.Transaction;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by 17629 on 2018/4/25.
 */
public class TransactionDemo {

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );


    //找出2011年发生的交易 并且按照交易额降序排序
    @Test
    public void fun(){
        List<Transaction> collect = transactions.parallelStream().filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue)).collect(Collectors.toList());
        for (Transaction transaction:collect) {
            System.out.println(transaction);

        }
    }

    //交易员都来自哪些不同的城市工作
    @Test
    public void fun2(){
        List<String> collect = transactions.parallelStream().map(transaction -> transaction.getTrader().getCity()).distinct().collect(Collectors.toList());

        for (String str:collect) {
            System.out.println(str);

        }
    }

    //查找所有来自剑桥的交易员并且按照姓名字母排序
    @Test
    public void fun3(){
        List<Transaction> list = transactions.parallelStream().filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .sorted(Comparator.comparing(transaction -> transaction.getTrader().getName())).collect(Collectors.toList());

        for (Transaction transaction:list) {
            System.out.println(transaction);

        }
    }

    //返回所有交易员姓名并且按照字母顺序排序
    @Test
    public void fun4(){
        String names = transactions.parallelStream().map(transaction -> transaction.getTrader().getName())
                .sorted(String::compareTo).reduce("", (n1, n2) -> n1 + n2);

        System.out.println(names);

    }

    //有没有交易员在米兰工作的
    @Test
    public void fun5(){
        List<Transaction> list=transactions.parallelStream().filter(transaction -> transaction.getTrader().getCity().equals("Milan")).collect(Collectors.toList());

        for (Transaction transaction:list) {
            System.out.println(transaction);

        }

    }

    //最高交易额
    @Test
    public void fun6(){
        Optional<Integer> reduce = transactions.parallelStream().map(Transaction::getValue).reduce(Integer::max);
        reduce.ifPresent(System.out::print);
    }

    //找出交易额最小的交易员
    @Test
    public void fun7(){
//        Optional<Integer> reduce = transactions.parallelStream().map(Transaction::getValue).reduce(Integer::min);
//        reduce.ifPresent(System.out::print);
        Optional<Transaction> min = transactions.parallelStream().min(Comparator.comparing(Transaction::getValue));
        min.ifPresent(System.out::print);
    }



}
