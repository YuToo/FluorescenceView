# FluorescenceView
自定义荧光粒子效果控件
---

### 运行效果
![](GIF.gif)

### 使用方式
#### xml中引用
```xml
<xyz.yutoo.fluorescenceview.FluorescenceView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@mipmap/bg_conversation"
    tools:context="xyz.yutoo.fluorescenceview.MainActivity">


    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello world"
        android:layout_gravity="center"
        android:textColor="@android:color/white"/>

</xyz.yutoo.fluorescenceview.FluorescenceView>
```

<font color="red">1. 本控件继承自FrameLayout，因此使用方式和FrameLayout一致</font>


##### 暂未提供对外方法，可在Fluorescence控件中修改相关配置，基本都是有注释的