# Leaves Plugin Template

[![许可证](https://img.shields.io/github/license/LeavesMC/leaves-plugin-template)](LICENSE)

快速创建 Leaves 插件的开发模板

**:warning: 目前仅支持 [Gradle](https://gradle.org/)**

## 特性

- 支持在构建脚本中声明插件元数据
- 支持 [Mixin](https://github.com/FabricMC/Mixin)，[Mixin Extras](https://github.com/LlamaLad7/MixinExtras)，[AccessWidener](https://github.com/FabricMC/access-widener)
- 支持快速启动开发服务器

## 依赖

此模板需要 JDK 21 或更高版本

## 快速开始

1. **克隆本模板：**

   ```bash
   git clone https://github.com/LeavesMC/leaves-plugin-template.git
   ```

2. **修改项目**

   完成项目中的所有 `// TODO`， 修改 `src/mixin/resources` 中的 `leaves-template-plugin.accesswidener` 与 
   `leaves-template-plugin.mixins.json`，使其符合你的项目需求

3. **编写插件 ：**

   在 `src/main` 模块下编写你的插件代码
   在 `src/mixin` 模块下编写你的 Mixin 代码

4. **构建插件：**

   ```bash
   ./gradlew build
   ```

   构建成功后，插件 JAR 文件会生成在 `build/libs` 目录下。

5. **部署插件：**

   将生成的 `.jar` 文件放入支持 Leaves 插件的服务器的 `plugins` 文件夹，并重启服务器。

## 常见问题

**什么是 Mixin？**

参考 [Fabric 教程：Mixin 介绍](https://wiki.fabricmc.net/zh_cn:tutorial:mixin_introduction)

**什么是 AccessWidener？**

参考 [Fabric 教程：访问加宽器](https://wiki.fabricmc.net/zh_cn:tutorial:accesswideners)

**为什么 Mixin 不起作用？**

Leavesclip 默认禁用了 Mixin 功能。你需要在服务器启动脚本中加入 `-Dleavesclip.enable.mixin=true`

开发服务器中已经默认启用 Mixin

## 贡献

欢迎通过 Issue 或 Pull Request 参与贡献！

## 许可证

本项目在 [MIT License](LICENSE) 下发布。