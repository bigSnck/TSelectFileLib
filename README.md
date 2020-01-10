### 添加到Android studio<br>
##### Step1: 在根build.gradle中添加仓库：<br>

```
allprojects {
	repositories {
		
		maven { url 'https://jitpack.io' }
	}
}
```
##### 注意:maven { url "https://jitpack.io" }一定要放到 allprojects 里面不然更新不下来
#### Step2: 在工程中添加依赖：<br>
```
dependencies {
  implementation 'com.github.bigSnck:TSelectFileLib:1.0.0'
}
```

### 具体使用Demo<br>
#### 普通拍照<br>
##### 代码：<br>
```
new TSelectFile().from(this)
                 .setOperationType(OperationType.TakePhoto)//三种模式：OperationType.TakePhoto：拍照 OperationType.TakeVideo：拍视频 OperationType.TakeSelect 选择图片
                 .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))
                 .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {//拍摄的时候返回的结果
                        Log.i("AA", "选中" + fileEntity.toString());
                        mTvResultShow.setText(fileEntity.getOriginalPath());
                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {

                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
```
#### 普通录像 <br>
##### 代码 <br>
```
new TSelectFile().from(this)
                 .setOperationType(OperationType.TakeVideo)
                 .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))
                 .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {//拍摄的时候返回的结果
                        Log.i("AA", "选中" + fileEntity.toString());
                        mTvResultShow.setText(fileEntity.getOriginalPath());
                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {

                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });

```
#### 选择图片 <br>
 ##### 代码 <br>
 ``` 
 new TSelectFile().from(this)
                  .setOperationType(OperationType.TakeSelect)//设置选择
                  .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))//设置路径
                  //.setSingle()//设置单选(只选一张)
                  .setIsShowCamra(true)//是否显示相机 true:显示 false:不显示
                  .setSelectMax(9)//最多选择多少张
                  .setSelectedStyle(SelectedStyleType.COMMON) //普通样式 SelectedStyleType.COMMON: 数字SelectedStyleType.NUMBER
                  .setSeletctFileType(FileType.IMAGE)
                  .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {

                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {//选择的时候返回结果
                        mTvResultShow.setText(resultList.toString());
                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
 ```
 ##### 效果图<br>
 <img src="https://github.com/bigSnck/TSelectFileLib/blob/master/image/image2.jpg" width="300" height="500"/> 
  #### 选择视频<br>
  ##### 代码 <br>
   ``` 
 new TSelectFile()
                .from(this)
                .setOperationType(OperationType.TakeSelect)//设置选择
                .setCaptureStrategy(new CaptureStrategy(true, "com.yt.tselectfilelibrary.fileprovider", "test"))//设置路径
                //.setSingle()//设置单选(只选一张)
                .setIsShowCamra(false)//是否显示相机 true:显示 false:不显示
                .setSelectMax(9)//最多选择多少张
                .setSelectedStyle(SelectedStyleType.NUMBER) //普通样式 SelectedStyleType.COMMON: 数字样式：SelectedStyleType.NUMBER
                .setSeletctFileType(FileType.VIDEO)//FileType.ALL暂时不支持同时选择
                .creat(new OnResultCallback() {
                    @Override
                    public void successSingleResult(SelectFileEntity fileEntity) {

                    }

                    @Override
                    public void successResult(List<SelectFileEntity> resultList) {
                        mTvResultShow.setText(resultList.toString());
                    }

                    @Override
                    public void errorResult(Exception es) {

                    }
                });
   ``` 
##### 效果图<br>
<img src="https://github.com/bigSnck/TSelectFileLib/blob/master/image/image2.jpg" width="300" height="500"/> 
  #### 更多用法可以查看源码 谢谢！！ <br>
