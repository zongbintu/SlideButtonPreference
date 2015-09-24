# SlideButtonPreference
SlideButtonPreference init

两种效果，一种是像IPHONE开和关是两咱颜色的滑动，一种是开关背景相同，只是slidebar的位置不同
交易图
![](https://github.com/2Tu/SlideButtonPreference/blob/master/screenshots/SlideButtonPreference.gif)

    <com.tu.preference.SlideButtonPreference
        android:id="@+id/slide_btn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:background_off="@drawable/slidebutton_bg_off"
        app:background_on="@drawable/slidebutton_bg_on"
        app:slidebar="@drawable/slidebutton"
        app:key="sbp"
        app:on="true"/>

    <com.tu.preference.SlideButtonPreference
        android:id="@+id/switch_btn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:background_off="@drawable/switch_background"
        app:slidebar="@drawable/slide_button"
        app:key="sbp_switch"
        app:on="true"/>
