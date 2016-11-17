Location Api Test 
===
This is the sample of Location API. It use 2 way to get the location info. One is by Android’s Location API(`LocationManager`). Another is by `Google Play Service`.

Differnce
===
基本上使用 `LocationManager` & `Google Play Service` 都可以拿到經緯度資料
- LocationManager: 是 Android API 提供的方式，所以一定可以正常使用(不論是任何device or emulator)。你可以選擇使用 `GPS` 或是 `網路訊號` 來判斷，取決於你對精準度的需求。
- Google Play Service: Device 一定要有安裝 `Google Play` 才可以使用。Play Service提供的 Location API 做了很多優化，例如綜合各種硬體資料，提升精準度，以及精簡電源的損耗


Reference
===
Android API (LocationManager)
- http://cw1057.blogspot.tw/2011/11/android-locationmanager.html
- https://dotblogs.com.tw/ttmac/2015/11/28/114143

Google Play Service (Location)
- https://developer.android.com/training/location/index.html

其他參考
- https://developer.android.com/guide/topics/location/strategies.html


