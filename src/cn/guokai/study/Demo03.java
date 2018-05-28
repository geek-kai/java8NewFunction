package cn.guokai.study;

import cn.guokai.study.pojo.WordCounter;
import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by guokai on 2018/5/22.
 */
public class Demo03 {

    private final String SENTENCE =
            "Nel mezzo del cammin di nostra vita " +
                    "mi ritrovai in una selva oscura" +
                    " ché la dritta via era smarrita ";

    @Test
    public void test1(){

            int counter = 0;
            boolean lastSpace = true;
            for (char c : SENTENCE.toCharArray()) {
                if (Character.isWhitespace(c)) {
                    lastSpace = true;
                } else {
                    if (lastSpace) counter++;
                    lastSpace = false;
                }
            }
        System.out.println(counter);

        }

        @Test
        public void tets2(){
            Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                    .mapToObj(SENTENCE::charAt);
            System.out.println("Found " + WordCounter.countWords(stream )+ " words");
        }



}
