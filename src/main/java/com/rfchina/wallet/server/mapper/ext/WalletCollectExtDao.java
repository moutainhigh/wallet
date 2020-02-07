package com.rfchina.wallet.server.mapper.ext;

import com.rfchina.wallet.domain.mapper.WalletCollectMapper;
import com.rfchina.wallet.domain.model.WalletCollect;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WalletCollectExtDao extends WalletCollectMapper {

    @Select({
            "select * from rf_wallet_collect",
            "where order_id = #{orderId}"
    })
    @ResultMap({"com.rfchina.wallet.domain.mapper.WalletCollectMapper.BaseResultMap"})
    WalletCollect selectByOrderId(@Param("orderId") Long orderId);

    @Update({
            "update rf_wallet_collect  ",
            "set refund_limit = refund_limit - #{amount}",
            "where collect_order_id = #{collectOrderId}"
    })
    int reduceRefundLimit(@Param("collectOrderId") Long collectOrderId, @Param("amount") Long amount);
}
