package com.zj.list.refresh.interfaces;

import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import com.zj.list.refresh.constance.SpinnerStyle;
import com.zj.list.refresh.listeners.OnStateChangedListener;


/**
 * ware of refresh internal
 */
public interface RefreshInternal extends OnStateChangedListener {
    /**
     * Get entity view
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    View getView();

    /**
     * Get transform method {@link SpinnerStyle} must return non-empty
     */
    @NonNull
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    SpinnerStyle getSpinnerStyle();

    /**
     * Set theme color
     * @param colors Corresponds to srlPrimaryColor srlAccentColor configured in Xml
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void setPrimaryColors(@ColorInt int... colors);

    /**
     * The size definition is completed (if the height does not change (code modification: setHeader), only called once, called in RefreshLayout # onMeasure)
     * @param kernel RefreshKernel
     * @param height HeaderHeight or FooterHeight
     * @param maxDragHeight Drag height
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight);
    /**
     * Finger drag and drop (will be called multiple times in a row, add isDragging and replace the previous onPulling, onReleasing)
     * @param isDragging true when finger is dragging false rebound animation
     * @param percent percentage of drop down  = offset/footerHeight (0 - percent - (footerHeight+maxDragHeight) / footerHeight )
     * @param offset pixel offset  0 - offset - (footerHeight+maxDragHeight)
     * @param height HeaderHeight or footer height
     * @param maxDragHeight Drag height
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight);

    /**
     * release ,called once will trigger loading
     * @param refreshLayoutIn RefreshLayoutIn
     * @param height  HeaderHeight or FooterHeight
     * @param maxDragHeight drag height
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void onReleased(@NonNull RefreshLayoutIn refreshLayoutIn, int height, int maxDragHeight);

    /**
     * @param refreshLayoutIn RefreshLayoutIn
     * @param height HeaderHeight or FooterHeight
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void onStartAnimator(@NonNull RefreshLayoutIn refreshLayoutIn, int height, int maxDragHeight);

    /**
     * @param refreshLayoutIn RefreshLayoutIn
     * @param success Whether the data was successfully refreshed or loaded
     * @return The time required to complete the animation. Returning to Integer.MAX_VALUE will cancel this completion event and continue to maintain the original state
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    int onFinish(@NonNull RefreshLayoutIn refreshLayoutIn, boolean success);

    /**
     * @param percentX The ratio of the horizontal coordinate of the finger to the screen when pulled down (0-percentX-1)
     * @param offsetX Offset of the finger's horizontal coordinates to the screen when pulled down (0-offsetX-LayoutWidth)
     * @param offsetMax maximum offset
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    void onHorizontalDrag(float percentX, int offsetX, int offsetMax);

    /**
     * Whether to support horizontal drag (will affect the call of onHorizontalDrag)
     * @return Dragging horizontally requires more time and resources, so return false if it is not supported
     */
    @RestrictTo({RestrictTo.Scope.LIBRARY, RestrictTo.Scope.LIBRARY_GROUP,RestrictTo.Scope.SUBCLASSES})
    boolean isSupportHorizontalDrag();
}
