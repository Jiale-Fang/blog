package pers.fjl.blogai.image;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.wlld.MatrixTools.Matrix;
import org.wlld.config.StudyPattern;
import org.wlld.imageRecognition.Operation;
import org.wlld.imageRecognition.Picture;
import org.wlld.imageRecognition.TempleConfig;
import org.wlld.nerveEntity.ModelParameter;

public class App {
    public static void main(String[] args) throws Exception {
        test3();
    }

    // 第一阶段学习
    public static void test1() throws Exception {
        Picture picture = new Picture();//读取图片，将图片降维转化为矩阵
        TempleConfig templeConfig = new TempleConfig();//配置模板类
        //第三个参数和第四个参数分别是训练图片的宽和高，为保证训练的稳定性请保证训练图片大小的一致性，大致比例、分类数量：（图片一个物体加上背景）
        templeConfig.init(StudyPattern.Accuracy_Pattern, true, 640, 640, 4);
        //将配置模板类作为构造塞入计算类
        Operation operation = new Operation(templeConfig);
        //一阶段 循环读取不同的图片
        for (int i = 1; i < 1500; i++) {
            System.out.println("开始第一阶段:" + i);
            //读取本地URL地址图片,并转化成矩阵
            Matrix a = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\a" + i + ".jpg");
            Matrix b = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\b" + i + ".jpg");
            Matrix c = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\c" + i + ".jpg");
            Matrix d = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\d" + i + ".jpg");
            //矩阵塞入运算类进行学习，第一个参数是图片矩阵，第二个参数是图片分类标注ID，第三个参数是第一次学习固定false
            // 第二次学习的时候，第三个参数必须是true 取得是参数矩阵
            operation.learning(a, 1, false);
            operation.learning(b, 2, false);
            operation.learning(c, 3, false);
            operation.learning(d, 4, false);
        }
        templeConfig.finishStudy();//结束学习
        //获取学习结束的模型参数,并将model保存数据库
        ModelParameter modelParameter = templeConfig.getModel();
        String model = JSON.toJSONString(modelParameter);
        System.out.println(model);
    }

    // 第二阶段学习
    public static void test2() throws Exception {
        Picture picture = new Picture();//读取图片，将图片降维转化为矩阵
        TempleConfig templeConfig = new TempleConfig();//配置模板类
        //第三个参数和第四个参数分别是训练图片的宽和高，为保证训练的稳定性请保证训练图片大小的一致性，大致比例、分类数量：（图片一个物体加上背景）
        templeConfig.init(StudyPattern.Accuracy_Pattern, true, 640, 640, 4);

        // 下一次学习就拿上次结果反序列化出来插入
        ModelParameter modelParameter1 = JSONObject.parseObject(ModelData.Data, ModelParameter.class);
        templeConfig.insertModel(modelParameter1);

        //将配置模板类作为构造塞入计算类
        Operation operation = new Operation(templeConfig);
        //一阶段 循环读取不同的图片
        for (int i = 1; i < 1500; i++) {
            System.out.println("开始第二阶段:" + i);
            //读取本地URL地址图片,并转化成矩阵
            Matrix a = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\a" + i + ".jpg");
            Matrix b = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\b" + i + ".jpg");
            Matrix c = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\c" + i + ".jpg");
            Matrix d = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\d" + i + ".jpg");
            //矩阵塞入运算类进行学习，第一个参数是图片矩阵，第二个参数是图片分类标注ID，第三个参数是第一次学习固定false
            // 第二次学习的时候，第三个参数必须是true 取得是参数矩阵
            operation.learning(a, 1, true);
            operation.learning(b, 2, true);
            operation.learning(c, 3, true);
            operation.learning(d, 4, true);
        }
        templeConfig.startLvq();//第三阶段学习，由于速度原因没用全连接
        //获取学习结束的模型参数,并将model保存数据库
        ModelParameter modelParameter = templeConfig.getModel();
        String model = JSON.toJSONString(modelParameter);
        System.out.println(model);
//        //验证的时候拿没测试过的，第三阶段直接tosee就可以了
//        for (int i = 10; i < 20; i++) {
//            Matrix a = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\a" + i + ".jpg");
//            Matrix c = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\c" + i + ".jpg");
//            // 获取分类的编号
//            int aNub = operation.toSee(a);
//            int cNub = operation.toSee(c);
//            System.out.println("a"+aNub);
//            System.out.println("c"+cNub);
//        }
    }

    // 验证
    public static void test3() throws Exception {
        Picture picture = new Picture();//读取图片，将图片降维转化为矩阵
        TempleConfig templeConfig = new TempleConfig();//配置模板类
        templeConfig.init(StudyPattern.Accuracy_Pattern, true, 640, 640, 2);
        // 下一次学习就拿上次结果反序列化出来插入
        ModelParameter modelParameter1 = JSONObject.parseObject(ModelData.Data2, ModelParameter.class);
        templeConfig.insertModel(modelParameter1);
        //将配置模板类作为构造塞入计算类
        Operation operation = new Operation(templeConfig);
        // 直接tosee
        int aWrong = 0;
        int bWrong = 0;
        int cWrong = 0;
        int dWrong = 0;
        for (int i = 1501; i < 1800; i++) {
            Matrix a = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\a" + i + ".jpg");
            Matrix b = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\b" + i + ".jpg");
            Matrix c = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\c" + i + ".jpg");
            Matrix d = picture.getImageMatrixByLocal("G:\\download\\java\\ai\\picture\\d" + i + ".jpg");
            // 获取分类的编号
            int aNub = operation.toSee(a);
            int bNub = operation.toSee(b);
            int cNub = operation.toSee(c);
            int dNub = operation.toSee(d);
            if (aNub != 1) {
                aWrong = aWrong + 1;
            } else if (bNub != 2) {
                bWrong = bWrong +1;
            } else if (cNub != 3) {
                cWrong = cWrong +1;
            } else if (dNub != 4) {
                dWrong = dWrong +1;
            }
            System.out.println("a" + aNub + "    第" + i + "次");
            System.out.println("b" + bNub + "    第" + i + "次");
            System.out.println("c" + cNub + "    第" + i + "次");
            System.out.println("d" + dNub + "    第" + i + "次");
        }
        System.out.println(aWrong);
        System.out.println(bWrong);
        System.out.println(cWrong);
        System.out.println(dWrong);
    }
}
