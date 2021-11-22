package com.fsquiroz.aplazo.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Error")
public class ExceptionDTO {

    @ApiModelProperty(example = "2019-01-25T12:57:01.999")
    private Instant timestamp;

    @ApiModelProperty(example = "404")
    private int status;

    @ApiModelProperty(example = "Not found")
    private String error;

    @ApiModelProperty(value = "Response status message")
    private String message;

    @ApiModelProperty(example = "/calculate")
    private String path;

    @ApiModelProperty(value = "Error meta information")
    private Map<String, Object> meta;
}
