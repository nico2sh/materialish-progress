# Material-ish Progress

A material style progress wheel compatible with 2.3

I needed to keep a consistent (or as close as possible) look in an app across all Android Versions. The progress wheel is quite cool in Lollipop, and pretty horrible on Gingerbread.

So I created this. This view uses [Progress Wheel](https://github.com/Todd-Davies/ProgressWheel) as a base, but has been almost completely rewritten (the original view uses handlers for updating the wheel).

This implementation tries to follow as close as possible the guidelines for the circular progress as [described here](http://www.google.com/design/spec/components/progress-activity.html#progress-activity-types-of-indicators).

This is how it looks in indeterminate mode (the spinSpeed here is 0.64 which is the default, look below how to change it):

![spinning wheel](spinningwheel.gif)

And in determinate mode (here the spinSpeed is set to 0.333):

![spinning wheel](spinningwheel_progress.gif)

You can also have a linear determinate progress mode if you don't like the animation.

## Download

You can copy the ProgressWheel.java (in the library module) and the attrs.xml content into your project. Or you can get the binaries from Maven central by adding in your build.gradle dependencies:

```compile 'com.pnikosis:materialish-progress:1.3'```

## Usage

You can create your own progress wheel in xml like this (remeber to add ```xmlns:wheel="http://schemas.android.com/apk/res-auto"```):

```xml
<com.pnikosis.materialishprogress.ProgressWheel
        android:id="@+id/progress_wheel"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        wheel:matProg_barColor="#5588FF"
        wheel:matProg_progressIndeterminate="true" />
```

Or in code:

```Java
ProgressWheel wheel = new ProgressWheel(context);
wheel.setBarColor(Color.BLUE);
...

```

### Callback

Use ```setCallback(ProgressCallback)``` to assign a callback that will be called each time the progress changes. This way you can update a value on the progress alongside with the progress animation, or execute an action once the progress reaches a certain value.

### Indeterminate wheel

For making the wheel indeterminate, just call the ```spin()``` method. If you set a progress value, the wheel will stop spinning.

You have two methods for setting the progress:

```progressWheel.setProgress(float value)```

Sets the value, and the wheel will smoothly animate to that value. The speed of the animation is defined by the spinSpeed (can be set with ```setSpinSpeed```, which number is the number of full turns per second)

```progressWheel.setInstantProgress(float value)```

Sets the value, and the wheel will instantly move to that value.

You can change other wheel properties such as the progress bar color, the wheel's background or the wheel's size and width.

### Fill radius

In case you want the spinning wheel to fill the whole layout instead of having a fixed size, you can use ```matProg_fillRadius```.

```xml
<com.pnikosis.materialishprogress.ProgressWheel
        android:id="@+id/progress_wheel"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        wheel:matProg_barColor="#5588FF"
        wheel:matProg_progressIndeterminate="true"
        wheel:matProg_fillRadius="true" />
```

This way, the wheel will be as big as the parent layout. Be warned though, if the parentlayout is not square, the wheel will become an oval since the wheel will always adapt to fill the parent view.

### Other options

In the xml definition, besides the ```fillRadius``` property, you can set:

* matProg_progressIndeterminate: boolean, if you want the wheel to spin right away.
* matProg_barColor: color, sets the small bar's color (the spinning bar in the indeterminate wheel, or the progress bar)
* matProg_barWidth: dimension, the width of the spinning bar
* matProg_rimColor: color, the wheel's border color
* matProg_rimWidth: dimension, the wheel's width (not the bar)
* matProg_spinSpeed: float, the base speed for the bar in indeterminate mode, and the animation speed when setting a value on progress. The speed is in full turns per second, this means that if you set speed as 1.0, means that the bar will take one second to do a full turn.
* matProg_barSpinCycleTime: integer, the time in milliseconds the indeterminate progress animation takes to complete (extending and shrinking the bar while spinning)
* matProg_circleRadius: dimension, the radius of the progress wheel, it will be ignored if you set fillRadius to true
* matProg_fillRadius: boolean, set to true if you want the progress wheel to fill the whole layout
* matProg_linearProgress: boolean, set to true if you want a linear animation on the determinate progress (instead of the interpolated default one).

## Versions

* 1.0 Initial release
* 1.1 The default speed is closer to the default progress wheel in Lollipop. Removed unused icons.
* 1.2 Interpolated determinate progress, a new option to set a linear progress (which was the default before)
* 1.3 Added a prefix to the attributes to avoid collisions, new callback called when the progress changes in the determinate wheel.

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
