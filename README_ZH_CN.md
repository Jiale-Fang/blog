# 一个博客交流分享平台

**网址：**[www.tcefrep.site](http://www.tcefrep.site)

## 前端技术
- **框架**：Vue.js
- **UI 框架**：Element UI 和 Semantic UI
- **其他技术**：vue-baberrage 实现弹幕效果，ECharts 实现统计图表

## 后端技术
- **框架**：Spring Framework, MyBatis-Plus
- **存储**：MySQL, AWS S3
- **中间件**：Redis（缓存），RabbitMQ（消息队列），Elasticsearch（全局搜索）

## 主要功能

- **博客论坛功能**：支持博客发布、评论和管理功能。
- **在线聊天室**：基于 WebSocket 协议，提供实时的群聊和私聊功能。
- **第三方登录**：集成 OAuth2.0 协议，实现通过第三方平台（如 GitHub、Google 等）的登录。
- **音乐盒功能**：通过调用网易云音乐 API，实现音乐推荐、歌单管理、最新音乐和 MV 的播放功能。
- **身份和访问管理**：使用 JWT 和 RBAC（基于角色的访问控制）实现用户认证和权限管理。
- **全局搜索**：采用 Elasticsearch 进行内容搜索，通过 LogStash 实现 MySQL 和 Elasticsearch 之间的数据增量同步。
- **消息队列**：使用 RabbitMQ 实现消息的异步发送和处理。
- **缓存**：使用 Redis 作为缓存解决方案，提高系统性能。


## 源码地址

- **前端源码**
  - 管理员系统后台页面（[admin.tcefrep.site](http://admin.tcefrep.site)）：[GitHub - blog-admin-vue](https://github.com/jiale-fang/blog-admin-vue)
  - 博客前端页面：[GitHub - blog-vue](https://github.com/jiale-fang/blog-vue)

- **后端源码**
  - [GitHub - blog](https://github.com/jiale-fang/blog)

## 其他资料

- 项目笔记和开发文档：[云笔记](https://note.youdao.com/s/bX2WxUmz)
- 详细介绍该项目的文章：https://blog.csdn.net/Dlihctcefrep/article/details/113591543

>&ensp;如果这篇文章对你有帮助，**麻烦点个赞，并star一下仓库**，有问题请在评论处指出，感谢各位支持！
