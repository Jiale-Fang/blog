package pers.fjl.server.search.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pers.fjl.common.po.Blog;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostMqIndexMessage implements Serializable {

    public final static String CREATE_OR_UPDATE = "create_update";
    public final static String REMOVE = "remove";

    private Long blogId;
    private String type;
    private Blog blog;
    private List<Long> blogIdList;

    public PostMqIndexMessage(Long blogId, String type) {
        this.blogId = blogId;
        this.type = type;
    }
}
