package com.zj.list.refresh.interfaces;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.annotation.*;
import com.zj.list.refresh.constance.RefreshState;
import com.zj.list.refresh.listeners.OnLoadMoreListener;
import com.zj.list.refresh.listeners.OnMultiPurposeListener;
import com.zj.list.refresh.listeners.OnRefreshListener;
import com.zj.list.refresh.listeners.OnRefreshLoadMoreListener;

/**
 * 刷新布局
 * Created by ZJJ  on 2017/5/26.
 */
@SuppressWarnings("unused")
public interface RefreshLayoutIn {

    /**
     * 设置 Footer 高度
     *
     * @param dp 虚拟像素（px需要调用px2dp转换）
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setFooterHeight(float dp);

    /**
     * 设置 Header 高度
     *
     * @param dp 虚拟像素（px需要调用px2dp转换）
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setHeaderHeight(float dp);

    /**
     * 设置 Header 起始偏移量
     *
     * @param dp 虚拟像素（px需要调用px2dp转换）
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setHeaderInsetStart(float dp);

    /**
     * 设置 Footer 起始偏移量
     *
     * @param dp 虚拟像素（px需要调用px2dp转换）
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setFooterInsetStart(float dp);

    /**
     * 显示拖动高度/真实拖动高度 比率（默认0.5，阻尼效果）
     *
     * @param rate 显示拖动高度/真实拖动高度 比率
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setDragRate(@FloatRange(from = 0, to = 1) float rate);

    /**
     * 设置下拉最大高度和Header高度的比率（将会影响可以下拉的最大高度）
     *
     * @param rate 下拉最大高度和Header高度的比率
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setHeaderMaxDragRate(@FloatRange(from = 1, to = 100) float rate);

    /**
     * 设置上拉最大高度和Footer高度的比率（将会影响可以上拉的最大高度）
     *
     * @param rate 上拉最大高度和Footer高度的比率
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setFooterMaxDragRate(@FloatRange(from = 1, to = 100) float rate);

    /**
     * 设置 触发刷新距离 与 HeaderHeight 的比率
     *
     * @param rate 触发刷新距离 与 HeaderHeight 的比率
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setHeaderTriggerRate(@FloatRange(from = 0, to = 1.0) float rate);

    /**
     * 设置 触发加载距离 与 FooterHeight 的比率
     *
     * @param rate 触发加载距离 与 FooterHeight 的比率
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setFooterTriggerRate(@FloatRange(from = 0, to = 1.0) float rate);

    /**
     * 设置回弹显示插值器 [放手时回弹动画,结束时收缩动画]
     *
     * @param interpolator 动画插值器
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setReboundInterpolator(@NonNull Interpolator interpolator);

    /**
     * 设置回弹动画时长 [放手时回弹动画,结束时收缩动画]
     *
     * @param duration 时长
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setReboundDuration(int duration);

    /**
     * 设置指定的Footer
     *
     * @param footer 刷新尾巴
     */
    void setRefreshFooter(@NonNull RefreshFooter footer);

    /**
     * 设置指定的Footer
     *
     * @param footer 刷新尾巴
     * @param width  宽度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @param height 高度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setRefreshFooter(@NonNull RefreshFooter footer, int width, int height);

    /**
     * 设置指定的Header
     *
     * @param header 刷新头
     */
    void setRefreshHeader(@NonNull RefreshHeader header);

    /**
     * 设置指定的Header
     *
     * @param header 刷新头
     * @param width  宽度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @param height 高度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setRefreshHeader(@NonNull RefreshHeader header, int width, int height);

    /**
     * 设置指定的 Content
     *
     * @param content 内容视图
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setRefreshContent(@NonNull View content);

    /**
     * 设置指定的 Content
     *
     * @param content 内容视图
     * @param width   宽度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @param height  高度 可以使用 MATCH_PARENT, WRAP_CONTENT
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setRefreshContent(@NonNull View content, int width, int height);


    /**
     * 设置是否启用上拉加载更多（默认启用）
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableLoadMore(boolean enabled);

    /**
     * 是否启用下拉刷新（默认启用）
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableRefresh(boolean enabled);

    /**
     * 设置是否监听列表在滚动到底部时触发加载事件（默认true）
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableAutoLoadMore(boolean enabled);

    /**
     * 设置是否启在下拉Header的同时下拉内容
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableHeaderTranslationContent(boolean enabled);

    /**
     * 设置是否启在上拉Footer的同时上拉内容
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableFooterTranslationContent(boolean enabled);

    /**
     * 设置是否启用越界回弹
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableOverScrollBounce(boolean enabled);

    /**
     * 设置是否开启纯滚动模式
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnablePureScrollMode(boolean enabled);

    /**
     * 设置是否在加载更多完成之后滚动内容显示新数据
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableScrollContentWhenLoaded(boolean enabled);

    /**
     * 是否在刷新完成之后滚动内容显示新数据
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableScrollContentWhenRefreshed(boolean enabled);

    /**
     * 设置在内容不满一页的时候，是否可以上拉加载更多
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableLoadMoreWhenContentNotFull(boolean enabled);

    /**
     * 设置是否启用越界拖动（仿苹果效果）
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableOverScrollDrag(boolean enabled);

    /**
     * 设置是否在全部加载结束之后Footer跟随内容
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableFooterFollowWhenLoadFinished(boolean enabled);

    /**
     * 设置是否 当 Header FixedBehind 时候是否剪裁遮挡 Header
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableClipHeaderWhenFixedBehind(boolean enabled);

    /**
     * 设置是否 当 Footer FixedBehind 时候是否剪裁遮挡 Footer
     *
     * @param enabled 是否启用
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setEnableClipFooterWhenFixedBehind(boolean enabled);

    /**
     * 设置是会否启用嵌套滚动功能（默认关闭+智能开启）
     *
     * @param enabled 是否启用
     */
    void setEnableNestedScroll(boolean enabled);

    /**
     * 设置是否开启在刷新时候禁止操作内容视图
     *
     * @param disable 是否禁止
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setDisableContentWhenRefresh(boolean disable);

    /**
     * 设置是否开启在加载时候禁止操作内容视图
     *
     * @param disable 是否禁止
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setDisableContentWhenLoading(boolean disable);

    /**
     * 单独设置刷新监听器
     *
     * @param listener 刷新监听器
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setOnRefreshListener(OnRefreshListener listener);

    /**
     * 单独设置加载监听器
     *
     * @param listener 加载监听器
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setOnLoadMoreListener(OnLoadMoreListener listener);

    /**
     * 同时设置刷新和加载监听器
     *
     * @param listener 刷新加载监听器
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setOnRefreshLoadMoreListener(OnRefreshLoadMoreListener listener);

    /**
     * 设置多功能监听器
     *
     * @param listener 建议使用 {@link com.zj.list.refresh.listeners.SimpleMultiPurposeListener}
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setOnMultiPurposeListener(OnMultiPurposeListener listener);

    /**
     * 设置滚动边界判断器
     *
     * @param boundary 建议使用 {@link com.zj.list.refresh.impl.ScrollBoundaryDeciderAdapter}
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setScrollBoundaryDecider(ScrollBoundaryDecider boundary);

    /**
     * 设置主题颜色
     *
     * @param primaryColors 主题颜色
     */
    void setPrimaryColors(@ColorInt int... primaryColors);

    /**
     * 设置主题颜色
     *
     * @param primaryColorId 主题颜色ID
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn setPrimaryColorsId(@ColorRes int... primaryColorId);

    /**
     * 完成刷新
     *
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn refreshCompleted();

    /**
     * 完成刷新
     *
     * @param delayed 开始延时
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn refreshCompleted(int delayed);

    /**
     * 完成加载
     *
     * @param success 数据是否成功刷新 （会影响到上次更新时间的改变）
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn refreshCompleted(boolean success);

    /**
     * 完成刷新
     *
     * @param delayed 开始延时
     * @param success 数据是否成功刷新 （会影响到上次更新时间的改变）
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn refreshCompleted(int delayed, boolean success);

    /**
     * 完成加载
     *
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn loadMoreCompleted();

    /**
     * 完成加载
     *
     * @param delayed 开始延时
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn loadMoreCompleted(int delayed);

    /**
     * 完成加载
     *
     * @param success 数据是否成功
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn loadMoreCompleted(boolean success);

    /**
     * 完成加载
     *
     * @param delayed    开始延时
     * @param success    数据是否成功
     * @param noMoreData 是否有更多数据
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn loadMoreCompleted(int delayed, boolean success, boolean noMoreData);

    /**
     * 完成加载并标记没有更多数据
     *
     * @return RefreshLayoutIn
     */
    RefreshLayoutIn loadMoreCompletedWithNoMoreData();

    /**
     * 恢复没有更多数据的原始状态
     *
     * @param noMoreData 是否有更多数据
     */
    void setNoMoreData(boolean noMoreData);

    /**
     * 获取当前 Header
     *
     * @return RefreshLayoutIn
     */
    @Nullable
    RefreshHeader getRefreshHeader();

    /**
     * 获取当前 Footer
     *
     * @return RefreshLayoutIn
     */
    @Nullable
    RefreshFooter getRefreshFooter();

    /**
     * 获取当前状态
     *
     * @return RefreshLayoutIn
     */
    RefreshState getState();

    /**
     * 获取实体布局视图
     *
     * @return ViewGroup
     */
    ViewGroup getLayout();

    /**
     * 自动刷新
     *
     * @return 是否成功（状态不符合会失败）
     */
    boolean autoRefresh();

    /**
     * 自动刷新
     *
     * @param delayed  开始延时
     * @param duration 拖拽动画持续时间
     * @param dragRate 拉拽的高度比率（要求 ≥ 1 ）
     * @return 是否成功（状态不符合会失败）
     */
    boolean autoRefresh(int delayed, int duration, float dragRate);

    /**
     * 自动加载
     *
     * @return 是否成功（状态不符合会失败）
     */
    boolean autoLoadMore();

    /**
     * 自动加载
     *
     * @param delayed  开始延时
     * @param duration 拖拽动画持续时间
     * @param dragRate 拉拽的高度比率（要求 ≥ 1 ）
     * @return 是否成功（状态不符合会失败）
     */
    boolean autoLoadMore(int delayed, int duration, float dragRate);

    /**
     * 是否启用下拉刷新
     */
    boolean isEnableRefresh();

    /**
     * 是否启用加载更多
     */
    boolean isEnableLoadMore();
}
