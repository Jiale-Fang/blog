package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.dto.BlogBackDTO;
import pers.fjl.common.dto.BlogStatisticsDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.vo.BlogVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Repository
public interface BlogDao extends BaseMapper<Blog> {

    /**
     * 获取对应用户的博客列表(多表查询)
     *
     * @param
     * @return List
     */
    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id AND b.uid = #{uid} LIMIT #{start},#{pageSize} ")
    List<BlogVO> getAllBlogs(Long uid, Integer start, Integer pageSize);

    /**
     * 获取博客列表(多表查询)
     *
     * @param
     * @return List
     */
    List<BlogVO> findHomePage(@Param("queryPageBean") QueryPageBean queryPageBean);

    @Select("        SELECT DATE_FORMAT( create_time, \"%Y-%m-%d\" ) AS date, COUNT( 1 ) AS count\n" +
            "        FROM blog " +
            "        GROUP BY date\n" +
            "        ORDER BY date DESC")
    /**
    * 文章统计
    *
    * @return {@link List< BlogStatisticsDTO >} 文章统计结果
    */
    List<BlogStatisticsDTO> listArticleStatistics();

    /**
     * 获取后台博客列表
     * @param queryPageBean 分页实体
     * @return list
     */
    List<BlogBackDTO> adminBlogPage(@Param("queryPageBean")QueryPageBean queryPageBean);

    /**
     * 获取收藏夹的分页数据
     * @param queryPageBean 分页实体
     * @param uid 用户id
     * @return
     */
    List<BlogVO> findFavoritesPage(@Param("queryPageBean")  QueryPageBean queryPageBean, @Param("uid") Long uid);

    /**
     * 获取管理后台对应博文数量
     * @param queryPageBean 分页实体
     * @return 用户id
     */
    Integer adminBlogPageCount(@Param("queryPageBean")QueryPageBean queryPageBean);
}
