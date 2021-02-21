package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.*;
import pers.fjl.server.dao.*;
import pers.fjl.server.service.ReportService;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 博文数据服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-02-18
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ViewsStatisticsMapper viewsStatisticsMapper;
    @Resource
    private CommentsStatisticsMapper commentsStatisticsMapper;
    @Resource
    private ThumbsUpStatisticsMapper thumbsUpStatisticsMapper;
    @Resource
    private BlogDao blogDao;
    @Resource
    private CommentDao commentDao;

    private Integer j = 0;
    private Map<String, Object> map;  // 相当于option
    private ArrayList<Object> series;   // 相当于series
    private HashMap<Object, Object> seriesMap; // 里面有个data数组存放要渲染的数据

    @Cacheable(value = {"Report"}, key = "#root.methodName+'['+#uid+']'")
    public Map<String, Object> getReport(Long uid) throws Exception {
        map = new HashMap<>();
        series = new ArrayList<>();
        Map<String, Object> xAxis = new HashMap<>();  // 相当于xAxis
        xAxis.put("data", getDateArray());

        seriesMap = new HashMap<>();

        QueryWrapper<ViewsStatistics> wrapper1 = new QueryWrapper<>();
        wrapper1.select("ifnull(sum(one_day),0) as oneDay", "ifnull(sum(two_day),0) as twoDay", "ifnull(sum(three_day),0) as threeDay",
                "ifnull(sum(four_day),0) as fourDay", "ifnull(sum(five_day),0) as fiveDay", "ifnull(sum(six_day),0) as sixDay", "ifnull(sum(seven_day),0) as sevenDay")
                .eq("uid", uid);
        ViewsStatistics viewsStatistics = viewsStatisticsMapper.selectOne(wrapper1);
        seriesMap.put("name", "访问量");
        seriesMap.put("data", getSeriesData(viewsStatistics));    // 获取到了浏览量的数据数组
        series.add(seriesMap);

        seriesMap = new HashMap<>();
        QueryWrapper<CommentsStatistics> wrapper2 = new QueryWrapper<>();
        wrapper2.select("ifnull(sum(one_day),0) as oneDay", "ifnull(sum(two_day),0) as twoDay", "ifnull(sum(three_day),0) as threeDay",
                "ifnull(sum(four_day),0) as fourDay", "ifnull(sum(five_day),0) as fiveDay", "ifnull(sum(six_day),0) as sixDay", "ifnull(sum(seven_day),0) as sevenDay")
                .eq("uid", uid);
        CommentsStatistics commentsStatistics = commentsStatisticsMapper.selectOne(wrapper2);
        seriesMap.put("name", "评论数");
        seriesMap.put("data", getSeriesData(commentsStatistics));    // 获取到了评论数的数据数组
        series.add(seriesMap);

        seriesMap = new HashMap<>();
        QueryWrapper<ThumbsUpStatistics> wrapper3 = new QueryWrapper<>();
        wrapper3.select("ifnull(sum(one_day),0) as oneDay", "ifnull(sum(two_day),0) as twoDay", "ifnull(sum(three_day),0) as threeDay",
                "ifnull(sum(four_day),0) as fourDay", "ifnull(sum(five_day),0) as fiveDay", "ifnull(sum(six_day),0) as sixDay", "ifnull(sum(seven_day),0) as sevenDay")
                .eq("uid", uid);
        ThumbsUpStatistics thumbsUpStatistics = thumbsUpStatisticsMapper.selectOne(wrapper3);
        seriesMap.put("name", "点赞量");
        seriesMap.put("data", getSeriesData(thumbsUpStatistics));    // 获取到了评论数的数据数组
        series.add(seriesMap);

        map.put("series", series);
        map.put("xAxis", xAxis);
        return map;
    }

    @Cacheable(value = {"Report"}, key = "#root.methodName+'['+#uid+']'")
    public Map<String, Object> getReport2(Long uid) throws Exception {
        map = new HashMap<>();  // 相当于option
        series = new ArrayList<>(); // 相当于series
        Map<String, Object> legend = new HashMap<>();   // 相当于legend
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).last("limit 4");  // 最多查出4条
        Integer count = blogDao.selectCount(wrapper);
        wrapper.select("title", "views", "thumbs", "blog_id").orderByDesc("views");
        List<Blog> blogList = blogDao.selectList(wrapper);
        String[] legendData = getLegendData(count, blogList);
        legend.put("data", legendData);

        for (Blog blog : blogList) {
            seriesMap = new HashMap<>();
            seriesMap.put("name", blog.getTitle());
            seriesMap.put("type", "bar");
            seriesMap.put("data", getSeriesData2(blog));
            series.add(seriesMap);
        }

        map.put("series", series);
        map.put("legend", legend);
        return map;
    }

    public String[] getDateArray() {
        String[] dateArray = new String[7];
        j = 0;
        for (int i = 6; i >= 0; i--) {
            SimpleDateFormat preDF = new SimpleDateFormat("MM-dd");
            Date date = new Date();
            String s = preDF.format(new Date(date.getTime() - (long) 24 * i * 60 * 60 * 1000));
            dateArray[j++] = s;
        }
        return dateArray;
    }

    public String[] getLegendData(Integer total, List<Blog> blogList) {
        j = 0;
        String[] data = new String[total];
        for (Blog blog : blogList) {
            data[j++] = blog.getTitle();
        }
        return data;
    }

    /**
     * 获取单个对象中与日期相关属性值的数组
     *
     * @param obj
     * @return Integer[]
     * @throws Exception
     */
    public Integer[] getSeriesData(Object obj) throws Exception {
        j = 0;
        Integer[] data = new Integer[7];
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 4; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            data[j++] = (Integer) f.get(obj);
        }
        return data;
    }


    public Integer[] getSeriesData2(Blog blog) {
        Integer[] data = new Integer[3];
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blog.getBlogId());
        Integer count = commentDao.selectCount(wrapper);    // 获取每篇博客的评论条数
        data[0] = blog.getThumbs();
        data[1] = count;
        data[2] = blog.getViews();
        return data;
    }
}
