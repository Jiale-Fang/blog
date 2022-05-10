package pers.fjl.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.fjl.common.utils.JsonLongSerializer;

import javax.validation.constraints.NotNull;

/**
 * 用户禁用状态
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户禁用状态")
public class UserDisableVO {

    /**
     * 用户id
     */
    @JsonSerialize(using = JsonLongSerializer.class )
    @NotNull(message = "用户id不能为空")
    private Long uid;

    /**
     * 用户禁用状态
     */
    @NotNull(message = "用户禁用状态不能为空")
    private Integer status;

}
