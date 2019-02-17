
<html>
<head>
<style>
*
{
   font-family: 仿宋;
}

@media screen {
  div.divFooter {
    display: none;
  }
}
@media print {
  div.divFooter {
    position: fixed;
    bottom: 0;
  }
}
</style>
<!--区块链跨境钱包发卡行前置SDK使用文档-->
</head>
<body>
</body>
</html>


## 撤销流程
撤销由商户主动发起，例如被扫的时候，商户的扫码枪扫描了用户的支付授权码，转发支付授权码给WeBank后，如果30秒以内没有收到WeBank的发货通知，会主动调用WeBank提供的撤销接口。

* 清算行清算的时候发现是撤销的订单直接不清算，WeBank二清的时候发现是撤销的订单也不清算给商户，所以境外合作行可以全款推给用户


```mermaid

%% 撤销（冲正）
sequenceDiagram
    participant 聚合支付服务商后台系统
    participant WeBank后台系统
    participant 区块链节点
    participant BOCHK后台系统

    Note over 聚合支付服务商后台系统,WeBank后台系统: 使用WeBank订单号，接口保证幂等
    聚合支付服务商后台系统->>WeBank后台系统: 调用撤销接口
    WeBank后台系统->>区块链节点: 撤销上链  
    Note over WeBank后台系统,区块链节点: 只能同步上链，不能异步上链
    区块链节点-->>WeBank后台系统: 撤销成功
    alt 撤销失败
        区块链节点-->>WeBank后台系统: 返回
    else 撤销超时
        区块链节点-->>WeBank后台系统: 返回
    end
    WeBank后台系统->>BOCHK后台系统: 通知撤销成功（带上BOCHK订单号）
    opt 查询确认
        BOCHK后台系统->>区块链节点: 查询链上订单是否已经撤销
    end
    BOCHK后台系统-->>WeBank后台系统: 返回
    Note over BOCHK后台系统,BOCHK后台系统: 保证退款不会重复处理
    BOCHK后台系统->>用户: 退款给用户（撤销没有汇率损失）
    WeBank后台系统-->>聚合支付服务商后台系统: 返回
    Note over 聚合支付服务商后台系统,WeBank后台系统: 如果一直到发生新的场切了，还是撤销失败，如何处理？是否能转为退货？
    聚合支付服务商后台系统-->>WeBank后台系统: 一直重试直到撤销成功(或重入撤销已成功)为止

```

## 退货流程
由用户触发，找到商户要求退货，聚合支付服务商再调用WeBank提供的退货接口。
注：在场切之前发起的退货，会自动标识为撤销，用户没有任何损失。

* 如果是退货：港中银可以在收到退货通知的时候，直接把钱退给用户，
* 如果是撤销：


```mermaid
%% 退货
sequenceDiagram
    participant 聚合支付服务商后台系统
    participant WeBank后台系统
    participant 区块链节点
    participant BOCHK后台系统

    Note over 聚合支付服务商后台系统,WeBank后台系统: 使用WeBank订单号+聚合支付服务商生成新的退货订单号，接口保证幂等
    聚合支付服务商后台系统->>WeBank后台系统: 调用退货接口
    WeBank后台系统->>区块链节点: 退货上链接口
    Note over WeBank后台系统,区块链节点: 最好同步上链，也可以异步上链
    区块链节点-->>WeBank后台系统: 退货成功 or 撤销成功
    alt 失败
        区块链节点-->>WeBank后台系统: 返回
    else 超时
        区块链节点-->>WeBank后台系统: 返回
    end
    WeBank后台系统->>BOCHK后台系统: 通知退货成功或撤销成功（带上BOCHK订单号）
    opt 查询确认
        BOCHK后台系统->>区块链节点: 查询链上订单是否已经退货成功或撤销成功
    end
    BOCHK后台系统-->>WeBank后台系统: 返回
    Note over BOCHK后台系统,用户: 保证退款不会重复处理（or 保证总退款金额不超过原支付金额）
    BOCHK后台系统->>用户: 退款给用户（如果是撤销，是没有汇率损失的）
    WeBank后台系统-->>聚合支付服务商后台系统: 返回
    Note over WeBank后台系统,聚合支付服务商后台系统: 退货失败或者超时怎么处理？


```
