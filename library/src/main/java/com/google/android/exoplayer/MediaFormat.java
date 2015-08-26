/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer;

import com.google.android.exoplayer.util.Util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines the format of an elementary media stream.
 */
public final class MediaFormat {

  public static final int NO_VALUE = -1;

  public final String mimeType;
  public final int maxInputSize;

  /**
   * The duration in microseconds, or {@link C#UNKNOWN_TIME_US} if the duration is unknown, or
   * {@link C#MATCH_LONGEST_US} if the duration should match the duration of the longest track whose
   * duration is known.
   */
  public final long durationUs;

  public final int width;
  public final int height;
  public final int rotationDegrees;
  public final float pixelWidthHeightRatio;

  public final int channelCount;
  public final int sampleRate;

  public final String language;

  public final List<byte[]> initializationData;

  public final int maxWidth;
  public final int maxHeight;

  // Lazy-initialized hashcode.
  private int hashCode;
  // Possibly-lazy-initialized framework media format.
  private android.media.MediaFormat frameworkMediaFormat;

  public MediaFormat copyWithMaxVideoDimensions(int maxWidth, int maxHeight) {
    return new MediaFormat(mimeType, maxInputSize, durationUs, width, height,
            rotationDegrees, pixelWidthHeightRatio, channelCount, sampleRate, language, initializationData, maxWidth, maxHeight);
  }

  public static MediaFormat createVideoFormat(String mimeType, int maxInputSize, int width,
      int height, List<byte[]> initializationData) {
    return createVideoFormat(
            mimeType, maxInputSize, C.UNKNOWN_TIME_US, width, height, NO_VALUE, initializationData);
  }

  public static MediaFormat createVideoFormat(String mimeType, int maxInputSize, long durationUs,
      int width, int height, int rotationDegrees, List<byte[]> initializationData) {
    return createVideoFormat(
            mimeType, maxInputSize, durationUs, width, height, rotationDegrees, NO_VALUE,
            initializationData);
  }

  public static MediaFormat createVideoFormat(String mimeType, int maxInputSize, long durationUs,
      int width, int height, int rotationDegrees, float pixelWidthHeightRatio, List<byte[]> initializationData) {
    return new MediaFormat(mimeType, maxInputSize, durationUs, width, height, rotationDegrees, 
         pixelWidthHeightRatio, NO_VALUE, NO_VALUE, null, initializationData, NO_VALUE, NO_VALUE);
  }

  public static MediaFormat createAudioFormat(String mimeType, int maxInputSize, int channelCount,
      int sampleRate, List<byte[]> initializationData) {
    return createAudioFormat(
        mimeType, maxInputSize, C.UNKNOWN_TIME_US, channelCount, sampleRate, initializationData);
  }

  public static MediaFormat createAudioFormat(String mimeType, int maxInputSize, long durationUs,
      int channelCount, int sampleRate, List<byte[]> initializationData) {
    return new MediaFormat(mimeType, maxInputSize, durationUs, NO_VALUE, NO_VALUE, NO_VALUE,
        NO_VALUE, channelCount, sampleRate, null, initializationData, NO_VALUE, NO_VALUE);
  }

  public static MediaFormat createTextFormat(String mimeType, String language) {
    return createTextFormat(mimeType, language, C.UNKNOWN_TIME_US);
  }

  public static MediaFormat createTextFormat(String mimeType, String language, long durationUs) {
    return new MediaFormat(mimeType, NO_VALUE, durationUs, NO_VALUE, NO_VALUE, NO_VALUE,
        NO_VALUE, NO_VALUE, NO_VALUE, language, null, NO_VALUE, NO_VALUE);
  }

  public static MediaFormat createFormatForMimeType(String mimeType) {
    return createFormatForMimeType(mimeType, C.UNKNOWN_TIME_US);
  }

  public static MediaFormat createFormatForMimeType(String mimeType, long durationUs) {
    return new MediaFormat(mimeType, NO_VALUE, durationUs, NO_VALUE, NO_VALUE, NO_VALUE, 
        NO_VALUE, NO_VALUE, NO_VALUE, null, null, NO_VALUE, NO_VALUE);
  }

  /* package */ MediaFormat(String mimeType, int maxInputSize, long durationUs, int width,
      int height, int rotationDegrees, float pixelWidthHeightRatio, int channelCount, 
      int sampleRate, String language, List<byte[]> initializationData, int maxWidth, 
      int maxHeight) {
    this.mimeType = mimeType;
    this.maxInputSize = maxInputSize;
    this.durationUs = durationUs;
    this.width = width;
    this.height = height;
    this.rotationDegrees = rotationDegrees;
    this.pixelWidthHeightRatio = pixelWidthHeightRatio;
    this.channelCount = channelCount;
    this.sampleRate = sampleRate;
    this.language = language;
    this.initializationData = initializationData == null ? Collections.<byte[]>emptyList()
        : initializationData;
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
  }

  public MediaFormat copyWithMaxVideoDimension(int maxWidth, int maxHeight) {
    return new MediaFormat(mimeType, maxInputSize, durationUs, width, height, rotationDegrees,
        pixelWidthHeightRatio, channelCount, sampleRate, language, initializationData, maxWidth, maxHeight);
  }

  /**
   * @return A {@link MediaFormat} representation of this format.
   */
  @SuppressLint("InlinedApi")
  @TargetApi(16)
  public final android.media.MediaFormat getFrameworkMediaFormatV16() {
    if (frameworkMediaFormat == null) {
      android.media.MediaFormat format = new android.media.MediaFormat();
      format.setString(android.media.MediaFormat.KEY_MIME, mimeType);
      maybeSetStringV16(format, android.media.MediaFormat.KEY_LANGUAGE, language);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_MAX_INPUT_SIZE, maxInputSize);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_WIDTH, width);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_HEIGHT, height);
      maybeSetIntegerV16(format, "rotation-degrees", rotationDegrees);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_MAX_WIDTH, maxWidth);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_MAX_HEIGHT, maxHeight);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_CHANNEL_COUNT, channelCount);
      maybeSetIntegerV16(format, android.media.MediaFormat.KEY_SAMPLE_RATE, sampleRate);
      for (int i = 0; i < initializationData.size(); i++) {
        format.setByteBuffer("csd-" + i, ByteBuffer.wrap(initializationData.get(i)));
      }
      if (durationUs != C.UNKNOWN_TIME_US) {
        format.setLong(android.media.MediaFormat.KEY_DURATION, durationUs);
      }
      frameworkMediaFormat = format;
    }
    return frameworkMediaFormat;
  }

  @Override
  public String toString() {
    return "MediaFormat(" + mimeType + ", " + maxInputSize + ", " + width + ", " + height + ", "
        + rotationDegrees + ", " + pixelWidthHeightRatio + ", " + channelCount + ", " + sampleRate + ", " + language + ", "
        + durationUs + ", " + maxWidth + ", " + maxHeight + ")";
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      int result = 17;
      result = 31 * result + (mimeType == null ? 0 : mimeType.hashCode());
      result = 31 * result + maxInputSize;
      result = 31 * result + width;
      result = 31 * result + height;
      result = 31 * result + rotationDegrees;
      result = 31 * result + Float.floatToRawIntBits(pixelWidthHeightRatio);
      result = 31 * result + (int) durationUs;
      result = 31 * result + maxWidth;
      result = 31 * result + maxHeight;
      result = 31 * result + channelCount;
      result = 31 * result + sampleRate;
      result = 31 * result + (language == null ? 0 : language.hashCode());
      for (int i = 0; i < initializationData.size(); i++) {
        result = 31 * result + Arrays.hashCode(initializationData.get(i));
      }
      hashCode = result;
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return equalsInternal((MediaFormat) obj, false);
  }

  public boolean equals(MediaFormat other, boolean ignoreMaxDimensions) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    return equalsInternal(other, ignoreMaxDimensions);
  }

  private boolean equalsInternal(MediaFormat other, boolean ignoreMaxDimensions) {
    if (maxInputSize != other.maxInputSize || width != other.width || height != other.height
        || rotationDegrees != other.rotationDegrees
        || pixelWidthHeightRatio != other.pixelWidthHeightRatio
        || (!ignoreMaxDimensions && (maxWidth != other.maxWidth || maxHeight != other.maxHeight))
        || channelCount != other.channelCount || sampleRate != other.sampleRate
        || !Util.areEqual(language, other.language) || !Util.areEqual(mimeType, other.mimeType)
        || initializationData.size() != other.initializationData.size()) {
      return false;
    }
    for (int i = 0; i < initializationData.size(); i++) {
      if (!Arrays.equals(initializationData.get(i), other.initializationData.get(i))) {
        return false;
      }
    }
    return true;
  }

  @TargetApi(16)
  private static final void maybeSetStringV16(android.media.MediaFormat format, String key,
      String value) {
    if (value != null) {
      format.setString(key, value);
    }
  }

  @TargetApi(16)
  private static final void maybeSetIntegerV16(android.media.MediaFormat format, String key,
      int value) {
    if (value != NO_VALUE) {
      format.setInteger(key, value);
    }
  }

}
