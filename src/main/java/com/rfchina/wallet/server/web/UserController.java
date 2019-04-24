package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api
@RestController
public class UserController {

    @ApiOperation("查询钱包信息（企业or个人）")
    @PostMapping(UrlConstant.WALLET_USER_LIST)
    public ResponseValue userList(){
        return null;
    }

}
