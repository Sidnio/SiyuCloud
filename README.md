# SiyuCloud 私域云



### 项目模块


- app                   // 应用入口、生命周期管理、配置初始化
- ui                       // 各设备的 UI 适配与交互逻辑
  - TV
  - Phone
  - Tablet           
- media                // 聚焦内容类型的业务逻辑
  - video              // 视频播放逻辑、控制器、UI 组件
  - audio              // 音频播放、后台服务、通知栏控制
  - image              // 图片浏览、缩放、缓存策略
- core                    // 底层能力抽象
  - network            // 媒体资源加载、NAS/局域网支持
  - storage            // 本地缓存、数据库、文件系统访问
- utils                        // 通用工具模块
  - common             // 日志、权限、配置项 调试工具、性能分析、日志查看器  设备能力抽象（屏幕、系统信息、权限）
  - extensions         // Kotlin 扩展函数、UI 快捷封装


​       

  

### 项目包命名

```
com.sidnio.siyucloud.app                 // 应用入口、配置初始化
com.sidnio.siyucloud.ui.tv               // TV UI
com.sidnio.siyucloud.ui.phone            // 手机 UI
com.sidnio.siyucloud.ui.tablet           // 平板 UI

com.sidnio.siyucloud.media.video         // 视频播放逻辑
com.sidnio.siyucloud.media.audio         // 音频播放逻辑
com.sidnio.siyucloud.media.image         // 图片浏览逻辑

com.sidnio.siyucloud.core.player         // 播放器接口封装
com.sidnio.siyucloud.core.decoder        // 解码器支持
com.sidnio.siyucloud.core.network        // 网络加载、NAS 支持
com.sidnio.siyucloud.core.storage        // 本地缓存与文件系统

com.sidnio.siyucloud.utils.common        // 通用工具类
com.sidnio.siyucloud.utils.extensions    // Kotlin 扩展函数
```

