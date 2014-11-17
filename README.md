# Material-ish Progress

A material style progress wheel compatible with 2.3

I needed to keep a consistent (or as close as possible) look in an app across all Android Versions. The progress wheel is quite cool in Lollipop, and pretty horrible on Gingerbread.

So I created this. This view uses [Progress Wheel](https://github.com/Todd-Davies/ProgressWheel) as a base, but has been almost completely rewritten (the original view uses handlers for updating the wheel).

This is how it looks:

![spinning wheel](spinningwheel.gif)

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

For making the wheel indeterminate, just call the ```spin()``` method. If you set a progress value, the whell will stop spinning.

You have two methods for setting the progress:

```progressWheel.setProgress(float value)```

Sets the value, and the wheel will smoothly animate to that value. The speed of the animation is defined by the spinSpeed (can be set with ```setSpinSpeed```, which number is the number of full turns per second)

```progressWheel.setProgressImmediate(float value)```

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

Sets the value, and the wheel will instantly move to that value
