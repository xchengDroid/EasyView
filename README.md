# EasyPermission
一款非常简洁，依赖很少的权限请求框架。不依赖第三方类库，如RxJava，不依赖运行时或编译时注解。核心代码只有5个类,并且适配了小米 AppOps 权限。内部还封装了一些View框架，EasyActivity,EasyFrament，如果不需要可以仅仅将权限部分一直到自己的代码中。**无须在Activity和Fragment发起请求和接收返回**



## Dependencies

* Gradle

  `compile 'com.xcheng:easyview:1.6.4'`

##UseAge

* **请求单个权限**

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

* **请求多个权限**

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

* **处理权限回调**

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
                          if (deniedResult.allNeverAsked) {

                          } else {

                          }
                      }
                  });
  ```

* **DeniedResult解析**

  ```
  public final class DeniedResult {
      // 所有被拒绝的权限
      public final List<String> deniedPerms;
      // 被拒绝但未勾选不再询问的权限
      public final List<String> showRationalePerms;
      // 被拒绝且勾选不再询问的权限
      public final List<String> neverAskedPerms;
      // 是否全部权限都是被拒绝且不再询问
      public final boolean allNeverAsked;

      DeniedResult(@NonNull List<String> deniedPerms, @NonNull List<String> showRationalePerms) {
          this.deniedPerms = deniedPerms;
          this.showRationalePerms = showRationalePerms;
          this.allNeverAsked = showRationalePerms.isEmpty();
          //处理被完全拒绝的权限
          neverAskedPerms = new ArrayList<>();
          for (String permission : deniedPerms) {
              //rationales 不包含 deniedPermissions 的就是完全被拒绝的
              if (!showRationalePerms.contains(permission)) {
                  neverAskedPerms.add(permission);
              }
          }
      }
  }
  ```

  EasyPermisson大大简化了android的权限请求流程，增强了代码的可读性和连续性。



















