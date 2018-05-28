package cn.guokai.study;

import cn.guokai.study.pojo.Child;
import cn.guokai.study.pojo.Parent;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by 17629 on 2018/4/23.
 */
public class Demo1 {

    public static void main(String[] args) {
//        //获取一个目录下的所有隐藏文件
//        File[] files = new File("E:\\").listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isHidden();
//            }
//        });
//
//        System.out.println(files.length);
//
//        //使用java8新特性编程 把File的isHidden当作参数传递 函数试编程
//
//        File[] files1 = new File("E:\\").listFiles(File::isHidden);
//
//        System.out.println(files1.length);

//        Parent parent=new Child();
//        System.out.println(parent.getName());
//        System.out.println(parent.name);

        LocalDateTime now = LocalDateTime.of(2019,3,1,17,3,2,2);
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        System.out.println(now.format(simpleDateFormat));
    }
}
