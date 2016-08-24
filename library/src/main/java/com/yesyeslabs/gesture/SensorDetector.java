/*
 * Copyright (C) 2016 Siva Mupparaju
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yesyeslabs.gesture;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * The type Sensor detector.
 */
public abstract class SensorDetector implements SensorEventListener {

  private final int[] sensorTypes;

  public SensorDetector(int... sensorTypes) {
    this.sensorTypes = sensorTypes;
  }

  @Override public void onSensorChanged(SensorEvent sensorEvent) {
    if (isSensorEventBelongsToPluggedTypes(sensorEvent)) {
      onSensorEvent(sensorEvent);
    }
  }

  protected void onSensorEvent(SensorEvent sensorEvent) {

  }

  @Override public void onAccuracyChanged(Sensor sensor, int i) {
  }

  int[] getSensorTypes() {
    return sensorTypes;
  }

  private boolean isSensorEventBelongsToPluggedTypes(SensorEvent sensorEvent) {
    for (int sensorType : sensorTypes) {
      if (sensorEvent.sensor.getType() == sensorType) {
        return true;
      }
    }

    return false;
  }
}
