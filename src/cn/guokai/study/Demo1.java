package cn.guokai.study;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by 17629 on 2018/4/23.
 */
public class Demo1 {

    public static void main(String[] args) {
        //获取一个目录下的所有隐藏文件
        File[] files = new File("E:\\").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isHidden();
            }
        });

        System.out.println(files.length);

        //使用java8新特性编程 把File的isHidden当作参数传递 函数试编程

        File[] files1 = new File("E:\\").listFiles(File::isHidden);

        System.out.println(files1.length);
    }
}
