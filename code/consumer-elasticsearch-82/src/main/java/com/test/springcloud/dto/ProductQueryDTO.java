package com.test.springcloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductQueryDTO {

    @ApiModelProperty("页码")
    private int page = 1;

    @ApiModelProperty("页数")
    private int size = 5;

    @ApiModelProperty("category")
    private String category;

    @ApiModelProperty("标题")
    private String title;

//    @ApiModelProperty("排序字段")
//    private List<Sort> sorts;
}
