package pers.fjl.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.fjl.common.po.Comment;
import pers.fjl.server.dao.CommentDao;
import pers.fjl.server.service.CommentService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-28
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

}
