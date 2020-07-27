package com.rfchina.wallet.server.model.ext;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class ReportDownloadVo {

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "唯一码")
	private String uniqueCode;

	@ApiModelProperty(value = "文件名")
	private String fileName;

	@ApiModelProperty(value = "下载地址")
	private String location;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "文件状态：0未生成，1生成中，2已生成，3已过期")
	private Byte status;

}
