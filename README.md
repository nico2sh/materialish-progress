# Material-ish Progress

A material style progress wheel compatible with 2.3

I needed to keep a consistent (or as close as possible) look in an app across all Android Versions. The progress wheel is quite cool in Lollipop, and pretty horrible on Gingerbread.

So I created this. This view uses [Progress Wheel](https://github.com/Todd-Davies/ProgressWheel) as a base, but has been almost completely rewritten (the original view uses handlers for updating the wheel).

This is how it looks:

![spinning wheel](spinningwheel.gif)

## Download

You can copy the ProgressWheel.java (in the library module) and the attrs.xml content into your project. Or you can get the binaries from Maven central by adding in your build.gradle dependencies:

```compile 'com.pnikosis:materialish-progress:1.0'```

## Usage

You can create your own progress wheel in xml like this (remeber to add ```xmlns:wheel="http://schemas.android.com/apk/res-auto"```):

```xml
<com.pnikosis.materialishprogress.ProgressWheel
        android:id="@+id/progress_wheel"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        wheel:barColor="#5588FF"
        wheel:progressIndeterminate="true" />
```

Or in code:

```Java
ProgressWheel wheel = new ProgressWheel(context);
wheel.setBarColor(Color.BLUE);
...

```

### Indeterminate wheel

For making the wheel indeterminate, just call the ```spin()``` method. If you set a progress value, the wheel will stop spinning.

You have two methods for setting the progress:

```progressWheel.setProgress(float value)```

Sets the value, and the wheel will smoothly animate to that value. The speed of the animation is defined by the spinSpeed (can be set with ```setSpinSpeed```, which number is the number of full turns per second)

```progressWheel.setProgressImmediate(float value)```

Sets the value, and the wheel will instantly move to that value.

You can change other wheel properties such as the progress bar color, the wheel's background or the wheel's size and width.

### Fill radius

In case you want the spinning wheel to fill the whole layout instead of having a fixed size, you can use ```fillRadius```.

```xml
<com.pnikosis.materialishprogress.ProgressWheel
        android:id="@+id/progress_wheel"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        wheel:barColor="#5588FF"
        wheel:progressIndeterminate="true"
        wheel:fillRadius="true" />
```

This way, the wheel will be as big as the parent layout. Be warned though, if the parentlayout is not square, the wheel will become an oval since the wheel will always adapt to fill the parent view.

### Other options

In the xml definition, besides the ```fillRadius``` property, you can set:

* progressIndeterminate: boolean, if you want the wheel to spin right away.
* barColor: color, sets the small bar's color (the spinning bar in the indeterminate wheel, or the progress bar)
* barWidth: dimension, the width of the spinning bar
* rimColor: color, the wheel's border color
* rimWidth: dimension, the wheel's width (not the bar)
* spinSpeed: float, the base speed for the bar in indeterminate mode, and the animation speed when setting a value on progress. The speed is in full turns per second, this means that if you set speed as 1.0, means that the bar will take one second to do a full turn.
* barSpinCycleTime: integer, the time in milliseconds the indeterminate progress animation takes to complete (extending and shrinking the bar while spinning)
* circleRadius: dimension, the radius of the progress wheel, it will be ignored if you set fillRadius to true
* fillRadius: boolean, set to true if you want the progress wheel to fill the whole layout

License
-------

    Copyright 2014 Nico Hormazábal

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
