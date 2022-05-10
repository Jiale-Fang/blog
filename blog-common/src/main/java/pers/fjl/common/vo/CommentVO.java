package pers.fjl.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.fjl.common.po.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentVO extends Comment implements Serializable {

    private String nickname;    //自己的昵称

    private String avatar;

    private String title;

    private List<CommentVO> children;

    private String replyNickname;   //回复的人的昵称
}
