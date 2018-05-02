package cn.guokai.study.pojo;

/**
 * Created by 17629 on 2018/4/23.
 */
public class Apple {

    Integer weight;
    String name;

    public Apple(Integer integer) {
        this.weight=integer;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Apple(Integer weight, String name) {
        this.weight = weight;
        this.name = name;
    }


}
