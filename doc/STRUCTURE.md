```mermaid
graph LR
         F-->G[其他区块链节点]
         G-->F
subgraph 发卡行机房
         A[集成了区块链跨境钱包SDK的发卡行server] --https-->D[发卡行前置server]
         D --https-->A
         D --https-->F[发卡行区块链节点]
         F --https-->D
         end
```
