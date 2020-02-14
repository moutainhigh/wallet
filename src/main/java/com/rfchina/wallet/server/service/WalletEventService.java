package com.rfchina.wallet.service;

import com.rfchina.biztools.mq.PostMq;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletOwnerDao;
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
    public WalletEvent sendEventMq(String eventType, Long walletId){

        Wallet wallet = walletDao.selectByPrimaryKey(walletId);
        List<WalletOwner> walletOwners = walletOwnerDao.selectByWalletId(walletId);
        return WalletEvent.builder()
                .eventType(eventType)
                .walletId(walletId)
                .status(wallet.getStatus())
                .walletOwners(walletOwners)
                .build();
    }

    @PostMq(routingKey = MqConstant.WALLET_EVENT)
    public WalletEvent sendEventMq(String eventType, Long walletId, Byte status){

        List<WalletOwner> walletOwners = walletOwnerDao.selectByWalletId(walletId);
        return WalletEvent.builder()
                .eventType(eventType)
                .walletId(walletId)
                .status(status)
                .walletOwners(walletOwners)
                .build();
    }

    @PostMq(routingKey = MqConstant.WALLET_EVENT)
    public WalletEvent sendEventMq(String eventType, Long walletId, Byte status, List<WalletOwner> walletOwners ){

        return WalletEvent.builder()
                .eventType(eventType)
                .walletId(walletId)
                .status(status)
                .walletOwners(walletOwners)
                .build();
    }
}
