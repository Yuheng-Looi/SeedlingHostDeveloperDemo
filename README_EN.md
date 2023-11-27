Before developing a project, developers should pay attention to the following, especially item 6, 7 and 8.

1. The environment of this project has been configured. Developers can carry out secondary development based on the demo project. Of course, you can also choose to create a project for development.

2. Build this project, and put the apk to a device, then click the "Start Intent" button on the "SeedlingHostDeveloperDemo" APP, you can see the card at the launcher, assistantscreen, aod, lockscreen, notification center and statusbar.

3. The main files of this project are described below:
   * Button click events are defined in the "MainActivity" class. Card intents are sent using the SeedlingTool.sendSeedling method, and the SeedlingTool.updateData method is used to update data.
   * The "CardLifeCycleProvider" class is used to receive the life cycle of the card.
   * The "SharedPreferencesUtil" class is the tool class of the card.
   * The card authorization code is configured in the "AndroidManifest.xml" file

4. Do not modify the package name of this project. The package name of the new project must also be fixed to "com.seedling.demo.hostdeveloperdemo".

5. The source code of the upk of this project is located in the upkSource directory. You can directly import the source code into Pantanal DevStudio and compile to generate the upk file of the card.

6. If you are developing on a new project, put the SDK file "seedling-support-external-2.0.3.aar" in your libs directory. Then please add the following code to the dependencies of the app's "build.gradle" file:
   ```
     api fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
   ```

7. Whether you are developing on this project or creating a new project, you need to use the signature file we provide.
   * The signature file is stored in the keystore folder of this project, and it's password is 000000;
   * If you are developing on a new project, please create a keystore folder in the root directory of your project, and copy the "host_demo.jks" of this project to the keystore folder. Then please add the following code to the app's "build.gradle" file:
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
   If you want to customize the path, modify the path parameter of the file in the storeFile accordingly.


8. If you want to customize the UPK, configure as follows:
   * card-config.json:
   ```
      "host":{
         "packageName": "com.seedling.demo.hostdeveloperdemo",
         "componentName": "com.seedling.demo.hostdeveloperdemo.CardLifeCycleProvider",
         "minHostVersion": 1
      }
   ```
   "packageName" cannot be changed. While the "componentName" can be customized, but it should be consistent with the authorities of the provider that binds the card lifecycle in the "AndroidManifest.xml" file.
   
   * config.json:
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
   The id in the identifier must be "268452000". The action in intent must be "pantanal.intent.business.app.system.HOST_CARD_DEMO", and the domain must be "business_app_system".

9. For details, please refer to the document "Card development process for the Maker Competition.pdf".