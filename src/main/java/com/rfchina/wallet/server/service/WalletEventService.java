package com.rfchina.wallet.server.service;

import com.rfchina.biztools.mq.PostMq;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletOwnerDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletOwner;
import com.rfchina.wallet.domain.model.ext.WalletEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalletEventService {

    @Autowired
    private WalletOwnerDao walletOwnerDao;

    @Autowired
    private WalletDao walletDao;

    @PostMq(routingKey = MqConstant.WALLET_EVENT)
    public WalletEvent sendEventMq(EnumDef.WalletEventType eventType, Long walletId) {

        Wallet wallet = walletDao.selectByPrimaryKey(walletId);
        List<WalletOwner> walletOwners = walletOwnerDao.selectByWalletId(walletId);
        return WalletEvent.builder()
                .eventType(eventType.getValue())
                .walletId(walletId)
                .level(wallet.getLevel())
                .status(wallet.getStatus())
                .walletOwners(walletOwners)
                .build();
    }

    @PostMq(routingKey = MqConstant.WALLET_EVENT)
    public WalletEvent sendEventMq(EnumDef.WalletEventType eventType, Long walletId,Byte level, Byte status) {

        List<WalletOwner> walletOwners = walletOwnerDao.selectByWalletId(walletId);
        return WalletEvent.builder()
                .eventType(eventType.getValue())
                .walletId(walletId)
                .level(level)
                .status(status)
                .walletOwners(walletOwners)
                .build();
    }

    @PostMq(routingKey = MqConstant.WALLET_EVENT)
    public WalletEvent sendEventMq(EnumDef.WalletEventType eventType, Long walletId,Byte level
            , Byte status, List<WalletOwner> walletOwners) {

        return WalletEvent.builder()
                .eventType(eventType.getValue())
                .walletId(walletId)
                .level(level)
                .status(status)
                .walletOwners(walletOwners)
                .build();
    }
}
