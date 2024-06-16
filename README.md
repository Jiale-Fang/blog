
English | [简体中文](README_ZH_CN.md)
# Blogosphere Nexus
A Blog Communication and Sharing Platform

**Website:** [www.tcefrep.site](http://www.tcefrep.site)

## Frontend Technologies
- **Framework**: Vue.js
- **UI Libraries**: Element UI and Semantic UI
- **Other Technologies**: `vue-baberrage` for bullet comments, `ECharts` for charts and statistics

## Backend Technologies
- **Frameworks**: Spring Framework, MyBatis-Plus
- **Storage**: MySQL, AWS S3
- **Middleware**: Redis (caching), RabbitMQ (message queue), Elasticsearch (global search)

## Key Features

- **Blog Forum Functionality**: Supports blog posting, commenting, and management.
- **Online Chatroom**: Provides real-time group and private chat using WebSocket protocol.
- **Third-Party Login**: Integrates OAuth2.0 for third-party logins through platforms like GitHub, Google, etc.
- **Music Box**: Utilizes NetEase Cloud Music API for music recommendations, playlist management, and the latest music and MV playback.
- **Identity and Access Management**: Uses JWT and RBAC (Role-Based Access Control) for user authentication and authorization.
- **Global Search**: Implements Elasticsearch for content search, with data synchronization between MySQL and Elasticsearch through LogStash.
- **Message Queue**: Uses RabbitMQ for asynchronous message sending and processing.
- **Caching**: Uses Redis as the caching solution to enhance system performance.

## Source Code

- **Frontend Source Code**
  - Admin System Backend ([admin.tcefrep.site](http://admin.tcefrep.site)): [GitHub - blog-admin-vue](https://github.com/jiale-fang/blog-admin-vue)
  - Blog Frontend: [GitHub - blog-vue](https://github.com/jiale-fang/blog-vue)

- **Backend Source Code**
  - [GitHub - blog](https://github.com/jiale-fang/blog)

## Additional Resources

- Project Notes and Documentation: [Cloud Notes](https://note.youdao.com/s/bX2WxUmz)
- Detailed Project Article: [Blog Article](https://blog.csdn.net/Dlihctcefrep/article/details/113591543)

>&ensp;If this article helped you, **please give it a like and star the repository**. Feel free to point out any issues in the comments, and thank you for your support!
