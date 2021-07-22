适配 5.0-10.0
支持背景，字体，状态栏、导航栏

1. Application#onCreate 中调用 SkinManager.init()
2. 要修改皮肤时调用 SkinManager.changeSkin()，传入皮肤包路径
