# GitHub 发布指南

## 📦 如何将 Turbo IMS 发布到你的 GitHub

### 第一步：准备工作

#### 1. 检查当前 Git 状态
```bash
cd /Users/liyunlong/DEV/hackims/Ims
git status
```

#### 2. 移除原项目的 remote（如果存在）
```bash
# 查看现有的 remote
git remote -v

# 删除原项目的 remote
git remote remove origin
```

### 第二步：在 GitHub 上创建新仓库

1. 登录 [GitHub](https://github.com)
2. 点击右上角的 **"+"** → **"New repository"**
3. 填写仓库信息：
   - **Repository name**: `turbo-ims` (或你喜欢的名字)
   - **Description**: `Enhanced IMS Configuration Tool for Google Pixel Devices`
   - **Public** 或 **Private**: 选择公开或私有
   - ❌ **不要**勾选 "Initialize this repository with a README"
   - ❌ **不要**添加 .gitignore 或 license（我们已经有了）
4. 点击 **"Create repository"**

### 第三步：初始化本地仓库并推送

#### 选项 A：如果当前目录已经是 Git 仓库

```bash
# 1. 添加你的 GitHub 仓库为 remote
git remote add origin https://github.com/你的用户名/turbo-ims.git

# 2. 查看当前状态
git status

# 3. 添加所有改动的文件
git add .

# 4. 提交改动
git commit -m "Initial commit: Turbo IMS v3.0

- Enhanced UI with modern logo
- Automatic language detection
- Per-SIM configuration
- Network settings quick access
- Based on vvb2060/Ims"

# 5. 推送到 GitHub（首次推送需要设置上游分支）
git push -u origin master
# 或者如果你的主分支是 main
git push -u origin main
```

#### 选项 B：如果需要重新初始化 Git 仓库

```bash
# 1. 删除现有的 .git 文件夹（谨慎操作！）
rm -rf .git

# 2. 初始化新的 Git 仓库
git init

# 3. 添加所有文件
git add .

# 4. 首次提交
git commit -m "Initial commit: Turbo IMS v3.0

- Enhanced UI with modern logo
- Automatic language detection
- Per-SIM configuration
- Network settings quick access
- Based on vvb2060/Ims"

# 5. 重命名主分支（可选，GitHub 现在推荐用 main）
git branch -M main

# 6. 添加 remote
git remote add origin https://github.com/你的用户名/turbo-ims.git

# 7. 推送到 GitHub
git push -u origin main
```

### 第四步：创建 Release 发布版本

#### 1. 构建 Release 版本的 APK
```bash
# 清理并构建 release 版本
./gradlew clean assembleRelease

# APK 生成位置
ls -lh app/build/outputs/apk/release/
```

#### 2. 在 GitHub 上创建 Release

1. 进入你的 GitHub 仓库页面
2. 点击右侧的 **"Releases"** → **"Create a new release"**
3. 填写 Release 信息：

**Tag version**: `v3.0`

**Release title**: `Turbo IMS v3.0 - Initial Release`

**Description** (示例):
```markdown
## 🎉 Turbo IMS v3.0 - Initial Release

Enhanced fork of vvb2060/Ims with modern UI and improved user experience.

### ✨ New Features
- 🎨 Modern logo and redesigned interface
- 🌍 Automatic language detection (Chinese/English)
- 📡 Quick network settings access after configuration
- 🎯 Per-SIM card configuration support
- 🔄 Auto-return to app after applying settings

### 📥 Installation
1. Download `turbo-ims-v3.0.apk` below
2. Install on your Pixel device
3. Install and start Shizuku
4. Grant permissions and enjoy!

### 📋 Requirements
- Google Pixel device
- Android 14+
- Shizuku installed and running

### 🙏 Credits
Based on the excellent [IMS by vvb2060](https://github.com/vvb2060/Ims)

### ⚠️ Important Notes
- Android 16 QPR2 Beta 3+ users: Configuration is non-persistent
- Settings will reset after reboot on Beta builds
- Always test with your carrier before relying on IMS features
```

4. 上传 APK 文件：
   - 点击 **"Attach binaries"**
   - 上传 `app/build/outputs/apk/release/app-release.apk`
   - 重命名为 `turbo-ims-v3.0.apk`

5. 点击 **"Publish release"**

### 第五步：更新 README 中的链接

编辑 `README.md`，替换以下占位符：

```markdown
# 将 yourusername 替换为你的 GitHub 用户名
https://github.com/yourusername/turbo-ims/releases
↓
https://github.com/你的用户名/turbo-ims/releases
```

然后提交并推送更新：
```bash
git add README.md
git commit -m "docs: update GitHub links in README"
git push
```

### 第六步：添加 .gitignore（可选但推荐）

如果没有 `.gitignore` 文件，创建一个：

```bash
cat > .gitignore << 'EOF'
# Android
*.apk
*.ap_
*.aab
*.dex
*.class
bin/
gen/
out/
build/
.gradle/
local.properties
*.iml
.idea/
.DS_Store
captures/
.externalNativeBuild/
.cxx/

# Signing files
*.jks
*.keystore

# Misc
*.log
.navigation/
EOF

git add .gitignore
git commit -m "chore: add .gitignore"
git push
```

### 第七步：添加 LICENSE 文件（可选）

如果你想添加 Apache 2.0 许可证：

```bash
# 从 GitHub 下载 Apache 2.0 license
curl -o LICENSE https://www.apache.org/licenses/LICENSE-2.0.txt

git add LICENSE
git commit -m "docs: add Apache 2.0 license"
git push
```

## 🎯 完整命令速查（快速版）

```bash
# 1. 进入项目目录
cd /Users/liyunlong/DEV/hackims/Ims

# 2. 移除旧的 remote
git remote remove origin

# 3. 添加新的 remote（替换你的用户名）
git remote add origin https://github.com/你的用户名/turbo-ims.git

# 4. 添加所有文件
git add .

# 5. 提交
git commit -m "Initial commit: Turbo IMS v3.0"

# 6. 推送到 GitHub
git push -u origin main

# 7. 构建 release APK
./gradlew clean assembleRelease

# 8. 在 GitHub 网页上创建 Release 并上传 APK
```

## 📝 后续更新流程

当你以后有新的修改需要更新到 GitHub：

```bash
# 1. 查看改动
git status

# 2. 添加改动的文件
git add .

# 3. 提交改动
git commit -m "描述你的改动"

# 4. 推送到 GitHub
git push

# 如果有新版本需要发布：
# 5. 更新版本号（在 app/build.gradle.kts 中修改 versionCode 和 versionName）
# 6. 构建新的 release APK
./gradlew clean assembleRelease

# 7. 在 GitHub 上创建新的 Release，上传新的 APK
```

## 🔐 使用 SSH 方式（推荐，避免每次输入密码）

### 设置 SSH Key

```bash
# 1. 生成 SSH key（如果还没有）
ssh-keygen -t ed25519 -C "your_email@example.com"

# 2. 复制 SSH 公钥
cat ~/.ssh/id_ed25519.pub

# 3. 在 GitHub 设置中添加 SSH key
# Settings → SSH and GPG keys → New SSH key

# 4. 使用 SSH remote
git remote set-url origin git@github.com:你的用户名/turbo-ims.git
```

## ❓ 常见问题

### Q: 推送时提示权限错误
**A**: 确保你已登录正确的 GitHub 账户，或使用 SSH key

### Q: 提示 "fatal: 'origin' does not appear to be a git repository"
**A**: 检查 remote 是否正确添加：`git remote -v`

### Q: 如何撤销最后一次 commit？
**A**: `git reset --soft HEAD~1`（保留改动）或 `git reset --hard HEAD~1`（丢弃改动）

### Q: APK 文件太大无法上传到 GitHub Release
**A**: GitHub Release 单个文件限制 2GB，APK 通常不会超过。如果超过，考虑使用外部托管服务

## 📞 需要帮助？

如果在发布过程中遇到问题，可以：
1. 检查 GitHub 文档：https://docs.github.com
2. 查看 Git 文档：https://git-scm.com/doc
3. 在项目中创建 Issue

---

**祝你发布顺利！** 🚀
