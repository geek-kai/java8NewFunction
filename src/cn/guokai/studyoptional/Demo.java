package cn.guokai.studyoptional;

import cn.guokai.studyoptional.model.Insurance;

import java.util.Optional;

/**
 * Created by guokai on 2018/5/22.
 */
public class Demo {

    public static void main(String[] args) {
        Insurance insurance=new Insurance();
        Optional<Insurance> insurance1 = Optional.ofNullable(insurance);
        Optional<String> s = insurance1.map(Insurance::getName);
        System.out.println(s.isPresent());
    }
}