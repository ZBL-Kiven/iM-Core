package com.zj.list.refresh.constance;

/**
 * How the top and bottom components change when dragged
 */

public enum SpinnerStyle {
    Translate,//parallelMovement  Features: HeaderView height does not change,
    Scale,//tensileDeformation    Features: OnDraw and Up (HeaderView height changes), OnDraw event will be triggered automatically
    FixedBehind,//pinnedBehind    Features: HeaderView height does not change,
    FixedFront,//fixedInFront     Features: HeaderView height does not change,
    MatchLayout//fillLayout       Features: The height of the HeaderView does not change, and the size is full of RefreshLayoutIn
}
