# LightPaintAndroid
## for Android 5.0 and above
LightPaintAndroid - is a small library that allows you to embed LightPaintView and the control panel for It. 

# Preview

## Screenshots
![Image](https://github.com/RybakovSemyon/LightPaintAndroid/blob/master/SCREENSHOTS/scr-common.png?raw=true)


# Include in your project
## Using Maven

```maven
<dependency>
  <groupId>com.github.rybakovsemyon</groupId>
  <artifactId>lightpaintandroid</artifactId>
  <version>0.1.1</version>
  <type>pom</type>
</dependency>
```

## Using gradle
```gradle
dependencies {
    implementation 'com.github.rybakovsemyon:lightpaintandroid:0.1.1'
}
```

## How to use
### 1. Add to your xml file this
```xml
    <rybakovsemyon.lightpaintview.LightPaintView
        android:id="@+id/paintView"
        android:layout_width="300dp"
        android:layout_height="300dp"/>
```

```xml
   <rybakovsemyon.lightpaintview.PaintPanel
        android:id="@+id/paintPanel"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

### 2. Find views inside your java classes and attach LightPaintView to PaintPanel
```java
public class MainActivity extends AppCompatActivity {

    private LightPaintView mLightPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLightPaintView = findViewById(R.id.paintView);
        PaintPanel paintPanel = findViewById(R.id.paintPanel);
        paintPanel.attachPaintView(mLightPaintView);
    }

}
```

### 3. If you want to preserve the state of your art when you rotate the screen or change the configuration, you must call the getLightPaintViewState() method and put it (for example) into your ViewModel/Presenter or Singleton(anti pattern, don't do that)/Repository class
```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLightPaintView = findViewById(R.id.paintView);
        PaintPanel paintPanel = findViewById(R.id.paintPanel);
        paintPanel.attachPaintView(mLightPaintView);
        mLightPaintView.setLightPaintViewState(ExampleSaver.getInstance().lightPaintViewState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ExampleSaver.getInstance().lightPaintViewState = mLightPaintView.getLightPaintViewState();
    }
```

# Developed By

* Semyon Rybakov
  * [Github page](https://github.com/RybakovSemyon) - <rybakov.semyon@gmail.com>

# License

    Copyright 2018 Semyon Rybakov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
