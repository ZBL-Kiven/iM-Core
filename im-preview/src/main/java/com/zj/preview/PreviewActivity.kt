package com.zj.preview

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zj.preview.images.BannerViewPager
import com.zj.preview.images.OnPageChange
import com.zj.preview.player.SimpleVideoEventListener
import com.zj.preview.player.VideoView
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zj.preview.images.image.TouchScaleImageView
import com.zj.preview.images.transformer.TransitionEffect
import com.zj.preview.mod.ConversationFileInfo
import java.util.ArrayList

/**
 * @author ZJJ on 2019.10.24
 * */
class PreviewActivity : AppCompatActivity() {

    companion object {
        private const val LAUNCH_PATH = "path"
        private const val LAUNCH_PREVIEW_SOURCE = "preview_source"
        fun start(context: Context, cur: ConversationFileInfo, data: ArrayList<ConversationFileInfo>? = null) {
            val i = Intent(context, PreviewActivity::class.java).apply {
                putExtra(LAUNCH_PATH, cur)
                putExtra(LAUNCH_PREVIEW_SOURCE, data)
            }
            context.startActivity(i)
        }
    }

    private var previewData: ArrayList<ConversationFileInfo>? = null

    private var previewBanner: BannerViewPager<ConversationFileInfo>? = null
    private var seekBar: SeekBar? = null
    private var tvStart: TextView? = null
    private var tvEnd: TextView? = null
    private var videoToolBar: View? = null
    private var curContainerView: FrameLayout? = null
    private var curFileData: ConversationFileInfo? = null

    private var mVideoView: VideoView? = null
        get() {
            if (field == null) field = VideoView(this)
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        initView()
        initData()
        initListener()
    }

    private fun getContentView(): Int {
        return R.layout.conversation_preview_image_activity
    }

    private fun initView() {
        previewBanner = findViewById(R.id.conversation_preview_vp)
        seekBar = findViewById(R.id.conversation_preview_sb)
        tvStart = findViewById(R.id.conversation_preview_tv_start)
        tvEnd = findViewById(R.id.conversation_preview_tv_end)
        videoToolBar = findViewById(R.id.conversation_preview_tools_bar)
    }

    @Suppress("UNCHECKED_CAST")
    private fun initData() {
        curFileData = intent.getSerializableExtra(LAUNCH_PATH) as? ConversationFileInfo?
        previewData = intent.getSerializableExtra(LAUNCH_PREVIEW_SOURCE) as? ArrayList<ConversationFileInfo>?
        mVideoView?.autoPlay(true)
        mVideoView?.setEventListener(videoEventListener)
        mVideoView?.overrideSeekBar(seekBar)
        initPreviewBanner()
        setPreviewData()
    }

    private fun initListener() {
        mVideoView?.setOnClickListener { v ->
            if (curFileData?.isVideo() == false) return@setOnClickListener
            (v as? VideoView)?.let {
                val curPath = curFileData
                when {
                    curPath == null -> it.stop()
                    it.isPlaying() -> {
                        it.pause()
                    }
                    else -> it.playOrResume(curPath)
                }
            }
        }
    }

    private fun setPreviewData() {
        curFileData?.let { cur ->
            if (previewData.isNullOrEmpty()) {
                if (previewData == null) previewData = arrayListOf()
                previewData?.add(cur)
            }
            previewBanner?.setData(previewData, (previewData?.indexOfFirst { it == curFileData }) ?: 0)
        } ?: finish()
    }

    private fun initPreviewBanner() {
        previewBanner?.init(R.layout.conversation_preview_item_base, 0, TransitionEffect.Zoom, object : OnPageChange<ConversationFileInfo> {
            override fun onBindData(data: ConversationFileInfo?, view: View) {
                data?.let {
                    val iv = view.findViewById<TouchScaleImageView>(R.id.preview_base_iv_img)
                    val vPlay = view.findViewById<ImageView>(R.id.preview_base_btn_video_play)
                    val flContainer = view.findViewById<FrameLayout>(R.id.preview_base_fl_video_container)
                    initViewsWithPagerItemInit(flContainer, data, vPlay, iv)
                }
            }

            override fun onFocusChange(v: View?, data: ConversationFileInfo?, focus: Boolean) {
                if (v != null && data != null) {
                    val iv = v.findViewById<TouchScaleImageView>(R.id.preview_base_iv_img)
                    if (!focus && data.isVideo()) {
                        iv.visibility = View.VISIBLE
                    }
                    previewBanner?.setAllowUserScrollable {
                        if (!focus) {
                            true
                        } else {
                            val ivCanScroll = iv.canScroll(1) && iv.canScroll(-1)
                            data.isVideo() || !ivCanScroll
                        }
                    }
                    onCurDataChanged(v, data, focus)
                }
            }

            override fun onScrollStateChanged(interval: Float, state: Int) {

            }
        })
    }

    private fun initViewsWithPagerItemInit(container: ViewGroup, data: ConversationFileInfo, play: View, iv: TouchScaleImageView) {
        iv.setScaleEnabled(!data.isVideo())
        iv.doubleTapEnabled = !data.isVideo()
        iv.setDoubleTapListener(null)
        Glide.with(this).load(data.imagePath).override(iv.measuredWidth, iv.measuredHeight).into(iv)
        if (iv.visibility != View.VISIBLE) showOrHideView(iv, true, floatArrayOf(0.0f, 1.0f), Constance.ANIMATE_DURATION)
        play.setOnClickListener(null)
        play.isSelected = false
        play.visibility = if (data.isVideo()) View.VISIBLE else View.GONE
        if (container.childCount > 0) container.removeAllViews()
    }

    private fun onCurDataChanged(v: View, data: ConversationFileInfo, focus: Boolean) {
        if (!focus) {
            val flContainer = v.findViewById<FrameLayout>(R.id.preview_base_fl_video_container)
            if (flContainer.childCount > 0) {
                val vv = flContainer.getChildAt(0) as? VideoView
                vv?.postDelayed({ vv.stop() }, 100)
            }
        } else {
            curContainerView = v as? FrameLayout
            curFileData = data
            initDataWithPagerSelected()
        }
    }

    private fun initDataWithPagerSelected() {
        curFileData?.let { data ->
            if (data.isVideo()) {
                videoToolBar?.visibility = View.VISIBLE
                tvStart?.text = getString(R.string.conversation_preview_video_str_default_time)
                tvEnd?.text = getDuration(data.duration)
                initVideoView()
            } else {
                videoToolBar?.visibility = View.GONE
            }
        }
    }

    private fun initVideoView() {
        curFileData?.let { data ->
            val vPlay = curContainerView?.findViewById<ImageView>(R.id.preview_base_btn_video_play)
            vPlay?.setOnClickListener {
                mVideoView?.playOrResume(data)
            }
        }
    }

    private val videoEventListener = object : SimpleVideoEventListener() {
        override fun onPlay(path: String): Boolean {
            val iv = curContainerView?.findViewById<TouchScaleImageView>(R.id.preview_base_iv_img)
            val vPlay = curContainerView?.findViewById<ImageView>(R.id.preview_base_btn_video_play)
            val flContainer = curContainerView?.findViewById<FrameLayout>(R.id.preview_base_fl_video_container)
            showOrHidePlayBtn(vPlay, false)
            val parent = mVideoView?.parent
            when {
                parent == flContainer -> {
                }
                parent != null -> (parent as? ViewGroup)?.removeAllViews()
                else -> flContainer?.addView(mVideoView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            }
            iv?.postDelayed({ if (mVideoView?.isPlaying() == true) iv.visibility = View.GONE }, 100)
            return true
        }

        override fun onPrepare(path: String, videoSize: Long): Boolean {
            tvEnd?.text = getDuration(videoSize)
            return true
        }

        override fun onSeekChanged(seek: Int, fromUser: Boolean, videoSize: Long): Boolean {
            val startProgress = videoSize / 100f * seek
            tvStart?.text = getDuration(startProgress.toLong())
            return true
        }

        override fun onCompleting(path: String): Boolean {
            val iv = curContainerView?.findViewById<TouchScaleImageView>(R.id.preview_base_iv_img)
            iv?.visibility = View.VISIBLE
            return true
        }

        override fun onCompleted(path: String): Boolean {
            val flContainer = curContainerView?.findViewById<FrameLayout>(R.id.preview_base_fl_video_container)
            flContainer?.removeAllViews()
            return false
        }

        override fun onPause(path: String): Boolean {
            val vPlay = curContainerView?.findViewById<ImageView>(R.id.preview_base_btn_video_play)
            showOrHidePlayBtn(vPlay, true)
            return true
        }

        override fun onStop(path: String): Boolean {
            val iv = curContainerView?.findViewById<TouchScaleImageView>(R.id.preview_base_iv_img)
            val flContainer = curContainerView?.findViewById<FrameLayout>(R.id.preview_base_fl_video_container)
            iv?.visibility = View.VISIBLE
            flContainer?.removeAllViews()
            return true
        }
    }

    fun showOrHidePlayBtn(v: View?, isShow: Boolean) {
        v?.isSelected = !isShow
        val start = if (isShow) 0.0f else 1.0f
        val end = if (isShow) 1.0f else 0.0f
        showOrHideView(v, isShow, floatArrayOf(start, end), Constance.ANIMATE_DURATION)
    }

    override fun onDestroy() {
        mVideoView?.release()
        curFileData = null
        super.onDestroy()
    }

    private fun showOrHideView(v: View?, isShow: Boolean, range: FloatArray, duration: Long) {
        v?.let {
            synchronized(it) {
                if (it.animation != null && ((it.visibility == View.VISIBLE && isShow) || (it.visibility != View.VISIBLE && !isShow))) return@let
                it.alpha = range[0]
                if (isShow) it.visibility = View.VISIBLE
                if (it.animation != null) it.clearAnimation()
                if (duration > 0) {
                    it.animate()?.alpha(range[1])?.setDuration(duration)?.withEndAction {
                        it.alpha = range[1]
                        it.visibility = if (isShow) View.VISIBLE else View.GONE
                    }?.start()
                } else {
                    it.alpha = range[1]
                    it.visibility = if (isShow) View.VISIBLE else View.GONE
                }
            }
        }
    }
}