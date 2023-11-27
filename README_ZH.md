开发者在进行项目开发之前，请先了解以下几项内容，尤其注意第6、7、8点。

1. 本项目的环境已经配置好了；开发者可以只关注upk的开发和卡片数据的更新，在本项目的基础上直接进行二次开发；也可以选择创建项目进行开发

2. 将本项目编译并下载在手机，点击“SeedlingHostDeveloperDemo” APP上的“启动意图”按钮，即可在负一屏、桌面、状态栏、通知栏、息屏和锁屏六个入口看到卡片。

3. 项目的主要文件说明如下：
   * “MainActivity”：该类中定义了按钮的点击事件，通过SeedlingTool.sendSeedling方法发送卡片意图，SeedlingTool.updateData方法对数据进行更新
   * “CardLifeCycleProvider”：该类用来接收卡片的生命周期
   * “AndroidManifest.xml”：文件除了配置Android组件，还配置了绑定卡片生命周期的provider和卡片授权码

4. 请勿修改本项目的包名；新建项目的包名也必须固定为"com.seedling.demo.hostdeveloperdemo"。

5. 本项目的upk源码位于upkSource目录下，直接该项目导入Pantanal DevStudio即可生成卡片的upk文件。

6. 如果是使用新建项目，请将SDK文件“seedling-support-external-2.0.3.aar”放在你的libs目录下，并在app的"build.gradle"文件的dependencies中添加以下代码：
   ```
     api fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
   ```

7. 不论是在本项目上开发，还是新建项目进行开发，都需要使用我们提供的签名文件。
   * 签名文件存放在项目的keystore文件夹中，密码是000000；
   * 如果是新建项目，请在新项目的根目录下创建一个keystore文件夹，并将本项目的“host_demo.jks”复制到keystore文件夹下，同时，在app的"build.gradle"文件的android中添加以下代码：
      ```
           android{
                 ......
                 signingConfigs {
                     debug {
                         storeFile file('../keystore/host_demo.jks')
                         keyAlias 'host'
                         keyPassword '000000'
                         storePassword '000000'
                     }
                     release {
                         storeFile file('../keystore/host_demo.jks')
                         keyAlias 'host'
                         keyPassword '000000'
                         storePassword '000000'
                     }
                 }
                 ......
           }
     ```
   * 如果希望自定义路径，请给storeFile中的file的路径参数做相应的修改；其余部分不允许更改。

8. 如果要自定义upk，请做如下配置
  * card-config.json中
    ```
        "host":{
           "packageName": "com.seedling.demo.hostdeveloperdemo",
           "componentName": "com.seedling.demo.hostdeveloperdemo.CardLifeCycleProvider",
           "minHostVersion": 1
        }
    ```
   "packageName"不可自定义，必须为"com.seedling.demo.hostdeveloperdemo"；
   "componentName"可以自定义，但是请与“AndroidManifest.xml”文件中绑定卡片生命周期的provider的authorities保持一致

  * config.json中
   ```
      "identifier": {
        "url": "test_host_demo_1",
        "id": "268452000",
        "type": "seedling",
        "name": "SeedlingDemo",   
        "version": "1.0.2",       
        "versionCode": 1000002   
      }
      
      "intent": {
        "action": ["pantanal.intent.business.app.system.HOST_CARD_DEMO"],
        "domain": "business_app_system"
      }
   ```
   identifier中id字段必须为“268452000”；intent中的action字段必须为"pantanal.intent.business.app.system.HOST_CARD_DEMO"，domain字段必须为"business_app_system"

9. 详情请参考文档“创客大赛泛在卡片开发流程.pdf”



