package com.irvanyale.app.footballapp.main.team

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx

class TeamOverviewFragment : Fragment(), AnkoComponent<Context> {

    private lateinit var teamOverviewTxtview: TextView
    private lateinit var teamOverview: String

    companion object {
        const val TEAM_OVERVIEW = "TEAM_OVERVIEW"

        fun newInstance(teamOverview : String) : TeamOverviewFragment {
            val bindData = Bundle()
            bindData.putString(TEAM_OVERVIEW, teamOverview)

            val fragment = TeamOverviewFragment()
            fragment.arguments = bindData
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bindData = arguments
        teamOverview = bindData?.getString(TEAM_OVERVIEW) ?: "-"

        teamOverviewTxtview.text = teamOverview
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(16)

            teamOverviewTxtview = textView {
                textSize = sp(8).toFloat()
            }.lparams(width = matchParent, height = wrapContent)
        }
    }
}