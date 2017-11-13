# EasyPermission
一款非常简洁，依赖很少的权限请求框架。不依赖第三方类库，如RxJava，不依赖运行时或编译时注解。核心代码只有5个类,并且适配了小米 AppOps 权限。内部还封装了一些View框架，EasyActivity,EasyFrament，如果不需要可以仅仅将权限部分一直到自己的代码中。**无须在Activity和Fragment发起请求和接收返回**



## Dependencies

* Gradle

  `compile 'com.xcheng:easyview:1.6.4'`

##UseAge

* **Request single permission**

  ```
  EasyPermission.with(activty)
                  .permissions(Manifest.permission.CAMERA)
                  .request(12, new OnRequestCallback() {
                      @Override
                      public void onAllowed() {
                          // request allowed
                      }

                      @Override
                      public void onRefused(DeniedResult deniedResult) {
                          // request denied
                      }
                  });
  ```

  ​

  ​

* **Request multi permission**

  ```
   EasyPermission.with(activty)
                  .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  .request(12, new OnRequestCallback() {
                      @Override
                      public void onAllowed() {
                          // request allowed
                      }

                      @Override
                      public void onRefused(DeniedResult deniedResult) {
                          // request denied
                      }
                  });
  ```

  ​

  ​

  ​



















