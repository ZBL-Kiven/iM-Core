package com.zj.imcore.gui.login

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zj.base.utils.DPUtils
import com.zj.base.view.BaseTitleView
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.teams.TeamInfo
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.ui.main.MainActivity
import com.zj.imcore.utils.img.transactions.RoundCorner
import com.zj.list.adapters.BaseAdapterDataSet
import com.zj.list.holders.BaseViewHolder
import com.zj.list.views.EmptyRecyclerView
import com.zj.loading.BaseLoadingView

class TeamsActivity : FCActivity() {

    companion object {
        fun startAct(ctx: Context) {
            ctx.startActivity(Intent(ctx, TeamsActivity::class.java))
        }
    }

    override fun getContentId(): Int {
        return R.layout.app_act_teams_content
    }

    private var titleView: BaseTitleView? = null
    private var gv: EmptyRecyclerView<TeamInfo>? = null
    private var blv: BaseLoadingView? = null

    override fun initView() {
        titleView = find(R.id.app_act_teams_title)
        gv = find(R.id.app_act_teams_rv)
        blv = find(R.id.app_act_teams_blv)
    }

    override fun initData() {
        val teams = TeamManager.getTeams()
        if (teams.isEmpty()) {
            blv?.setMode(BaseLoadingView.DisplayMode.NO_DATA, getString(R.string.app_act_teams_no_one), false)
        }
        gv?.setData(R.layout.app_act_teams_item_content, false, TeamManager.getTeams(), object : BaseAdapterDataSet<TeamInfo>() {
            override fun initData(holder: BaseViewHolder?, position: Int, module: TeamInfo?) {
                holder?.getView<ImageView>(R.id.app_act_teams_item_iv)?.let {
                    val radius = DPUtils.dp2px(ChatOption.avatarRadius) * 1.0f
                    val transformer = RoundCorner(this@TeamsActivity, radius, radius, radius, radius)
                    Glide.with(this@TeamsActivity).load(module?.member?.avatar).transform(transformer).error(R.mipmap.app_contact_avatar_default).into(it)
                }
                holder?.setText(R.id.app_act_teams_item_tv_name, module?.name)
                holder?.setText(R.id.app_act_teams_item_tv_description, module?.description)
            }

            override fun onItemClick(position: Int, v: View?, m: TeamInfo?) {
                val id = m?.id
                if (id.isNullOrEmpty()) {
                    FCApplication.showToast(R.string.app_act_teams_error)
                } else {
                    TeamManager.changeCurTeam(id)
                    startMainAct()
                }
            }
        })
    }

    override fun initListener() {
        titleView?.setLeftClickListener {
            this@TeamsActivity.startActivity(Intent(this@TeamsActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun startMainAct() {
        this@TeamsActivity.startActivity(Intent(this@TeamsActivity, MainActivity::class.java))
        finish()
    }
}