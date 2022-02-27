import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;


/**
 * mybatisPlus自动生成配置类
 *
 * @author fangjiale 2021年01月26日
 */
public class MPAutoGenerator {

    @Test
    public void testGenerator(){
        //1. 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("G:");
//        gc.setOutputDir("G:\\download\\app\\Git\\gitRepos2\\blog\\blog-extension\\src\\main\\java");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor("fjl");
        gc.setIdType(IdType.AUTO);
        gc.setServiceImplName("%sServiceImpl");
        gc.setServiceName("%sService");

        //2.数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/blog?serverTimezone=GMT%2B8&characterEncoding=UTF-8");
        dsc.setUsername("root");
        dsc.setPassword("123456");

        //3.策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true);//全局大写配置
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setEntityLombokModel(true);
//        strategy.setTablePrefix("sys_");
//        strategy.setInclude("sys_order");//数据库中的表
//        strategy.setInclude("sys_role");
//        strategy.setInclude("sys_permission");
//        strategy.setInclude("sys_customer");
//        strategy.setInclude("user");

        //4.包名策略配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("pers.fjl.common");
        pc.setMapper("dao");
        pc.setService("service");
//        pc.setModuleName("test");
        pc.setEntity("po");
        pc.setController("controller");
        pc.setXml("mapper");

        //5.整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(gc)
                .setDataSource(dsc)
                .setStrategy(strategy)
                .setPackageInfo(pc);

        //6.执行
        ag.execute();
    }
}
