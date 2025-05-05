# leaves-plugin-template

[![许可证](https://img.shields.io/github/license/LeavesMC/leaves-plugin-template)](LICENSE)

Leaves Plugin 的开发模板。

> 仅支持 [Gradle](https://gradle.org/) 

## 特性

- 构建脚本中声明插件 Meta 文件
- 支持 Mixin，Mixin Extra，AccessWidener
- 支持一键启动开发服务器

## 前提条件

为了使用此模板，你需要拥有Gradle，JDK21或更高版本。如果你使用 IntelliJ IDEA，那么它会帮你安装这些。

## 快速开始

1. **克隆本模板：**

   ```bash
   git clone https://github.com/LeavesMC/leaves-plugin-template.git
   ```

2. **按照指引修改项目**

   完成项目中的所有 `// TODO`， 修改 `src/mixin/resources` 中的 `leaves-template-plugin.accesswidener` 与 
   `leaves-template-plugin.mixins.json`，使其符合你的项目需求

3. **开发你的插件逻辑：**

   在 `src/main` 模块下编写你的插件代码
   在 `src/mixin` 模块下编写你的 Mixin 代码

4. **构建插件：**

   ```bash
   ./gradlew build
   ```

   构建成功后，插件 JAR 文件会生成在 `build/libs` 目录下。

5. **部署插件：**

   将生成的 `.jar` 文件放入 Leaves 服务器的 `plugins` 文件夹，并重启服务器。

## 贡献

欢迎通过 Issue 或 Pull Request 参与贡献！

## 许可证

本项目在 [MIT License](LICENSE) 下发布。