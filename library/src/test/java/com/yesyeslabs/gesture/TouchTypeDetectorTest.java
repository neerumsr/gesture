package com.yesyeslabs.gesture;

import android.content.Context;
import android.view.MotionEvent;

import com.yesyeslabs.gesture.TouchTypeDetector;
import com.yesyeslabs.gesture.TouchTypeDetector.TouchTypListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.yesyeslabs.gesture.TouchTypeDetector.SCROLL_DIR_DOWN;
import static com.yesyeslabs.gesture.TouchTypeDetector.SCROLL_DIR_LEFT;
import static com.yesyeslabs.gesture.TouchTypeDetector.SCROLL_DIR_RIGHT;
import static com.yesyeslabs.gesture.TouchTypeDetector.SCROLL_DIR_UP;
import static com.yesyeslabs.gesture.TouchTypeDetector.SWIPE_DIR_DOWN;
import static com.yesyeslabs.gesture.TouchTypeDetector.SWIPE_DIR_LEFT;
import static com.yesyeslabs.gesture.TouchTypeDetector.SWIPE_DIR_RIGHT;
import static com.yesyeslabs.gesture.TouchTypeDetector.SWIPE_DIR_UP;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricTestRunner.class) public class TouchTypeDetectorTest {

  private TouchTypListener mockListener;
  private TouchTypeDetector testTouchTypeDetector;

  @Before public void setUp() {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mockListener = mock(TouchTypListener.class);
    testTouchTypeDetector = new TouchTypeDetector(context, mockListener);
  }

  @Test public void detectOnDoubleTap() {
    testTouchTypeDetector.gestureListener.onDoubleTap(null);
    verify(mockListener, only()).onDoubleTap();
  }

  @Test public void detectOnDown() {
    testTouchTypeDetector.gestureListener.onDown(null);
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectOnLongPress() {
    testTouchTypeDetector.gestureListener.onLongPress(null);
    verify(mockListener, only()).onLongPress();
  }

  @Test public void detectOnSwipeRight() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 100, 50, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 300, 50, 0);
    testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
    verify(mockListener, only()).onSwipe(SWIPE_DIR_RIGHT);
  }

  @Test public void detectOnSwipeLeft() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 300, 50, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 100, 60, 0);
    testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
    verify(mockListener, only()).onSwipe(SWIPE_DIR_LEFT);
  }

  @Test public void detectOnSwipeLeftWithPowerBottomSwipe() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 300, 160, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 100, 50, 0);
    testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
    verify(mockListener, only()).onSwipe(SWIPE_DIR_LEFT);
  }

  @Test public void ignoreOnSwipeTop() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 50, 500, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 160, 50, 0);
    testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
    verify(mockListener, only()).onSwipe(SWIPE_DIR_UP);
  }

  @Test public void ignoreOnSwipeBottom() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 160, 50, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 50, 500, 0);
    testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
    verify(mockListener, only()).onSwipe(SWIPE_DIR_DOWN);
  }

  @Test public void detectSwipeLeftAndDownForTwoLeftAndDownSwipes() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 300, 50, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 100, 160, 0);
    testTouchTypeDetector.gestureListener.onFling(ev1, ev2, 201, 201);
    MotionEvent ev3 = MotionEvent.obtain(10, 10, 0, 160, 50, 0);
    MotionEvent ev4 = MotionEvent.obtain(10, 10, 0, 50, 500, 0);
    testTouchTypeDetector.gestureListener.onFling(ev3, ev4, 201, 201);
    verify(mockListener, times(1)).onSwipe(SWIPE_DIR_LEFT);
    verify(mockListener, times(1)).onSwipe(SWIPE_DIR_DOWN);
  }

  @Test public void detectOnScrollUp() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 200, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 50, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verify(mockListener, only()).onScroll(SCROLL_DIR_UP);
  }

  @Test public void detectNothingForSlightlyScrollUp() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 2, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 1, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectOnScrollRight() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 50, 0, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 200, 0, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verify(mockListener, only()).onScroll(SCROLL_DIR_RIGHT);
  }

  @Test public void detectNothingForSlightlyScrollRight() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 2, 0, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 1, 0, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectOnScrollDown() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 50, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 200, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verify(mockListener, only()).onScroll(SCROLL_DIR_DOWN);
  }

  @Test public void detectNothingForSlightlyScrollDown() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 0, 1, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 0, 2, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectOnScrollLeft() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 200, 0, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 50, 0, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verify(mockListener, only()).onScroll(SCROLL_DIR_LEFT);
  }

  @Test public void detectNothingForSlightlyScrollLeft() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 1, 0, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 2, 0, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectNoScrollWhenEventCoorsAreEqual() {
    MotionEvent ev1 = MotionEvent.obtain(10, 10, 0, 50, 2, 0);
    MotionEvent ev2 = MotionEvent.obtain(10, 10, 0, 50, 2, 0);
    testTouchTypeDetector.gestureListener.onScroll(ev1, ev2, 0, 0);
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectOnSingleTapConfirmed() {
    testTouchTypeDetector.gestureListener.onSingleTapConfirmed(null);
    verify(mockListener, only()).onSingleTap();
  }

  @Test public void detectOnSingleTapUp() {
    testTouchTypeDetector.gestureListener.onSingleTapUp(null);
    verifyNoMoreInteractions(mockListener);
  }
}
