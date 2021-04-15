package pers.fjl.blogai.randomforest;

import org.wlld.randomForest.DataTable;
import org.wlld.randomForest.RandomForest;
import pers.fjl.blogai.po.Student;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LangTest {
    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1() throws Exception {
        Set<String> column = new HashSet<>();
        column.add("math");
        column.add("eng");
        column.add("chi");
        column.add("h1");
        column.add("h2");
        column.add("h3");
        column.add("point");
        DataTable dataTable = new DataTable(column);
        dataTable.setKey("point");
        //根据每个树返回的分类，通过期限值加权得到权值最高的分类返回结果
        RandomForest randomForest = new RandomForest(6);
        randomForest.init(dataTable);//唤醒树
        //创建实体类输入数据
        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            student.setMath(getPoint());
            student.setEng(getPoint());
            student.setChi(getPoint());
            student.setH1(getPoint());
            student.setH2(getPoint());
            student.setH3(getPoint());
            student.setPoint(getPoint());
            randomForest.insert(student);
        }
        randomForest.study();
        Student student = new Student();
        student.setMath(getPoint());
        student.setEng(getPoint());
        student.setChi(getPoint());
        student.setH1(getPoint());
        student.setH2(getPoint());
        student.setH3(getPoint());
        student.setPoint(getPoint());
        int point = randomForest.forest(student);
        System.out.println("当前学生成绩的综合评定："+point);
    }

    private static int getPoint() {
        return new Random().nextInt(3) + 1;
    }
}
