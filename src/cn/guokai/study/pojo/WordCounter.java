package cn.guokai.study.pojo;

import java.util.stream.Stream;

/**
 * Created by guokai on 2018/5/22.
 */
public class WordCounter {

    /**
     * 单词总数
     */
    private Integer counter;
    /**
     * 是否是最后一个空格
     */
    private Boolean lastSpeace;

    public WordCounter(Integer counter, Boolean lastSpeace) {
        this.counter = counter;
        this.lastSpeace = lastSpeace;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Boolean getLastSpeace() {
        return lastSpeace;
    }

    public void setLastSpeace(Boolean lastSpeace) {
        lastSpeace = lastSpeace;
    }

    public WordCounter accumulate(Character c){
        if(Character.isWhitespace(c)){
            return lastSpeace ?this:new WordCounter(counter,true);
        }else {
            return lastSpeace ?new WordCounter(counter+1,false):this;
        }
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter,
                wordCounter.lastSpeace);
    }

    public static  int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
        return wordCounter.getCounter();
    }
    
}
