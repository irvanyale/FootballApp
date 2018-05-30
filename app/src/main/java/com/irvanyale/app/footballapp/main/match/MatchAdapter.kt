package com.irvanyale.app.footballapp.main.match

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.model.Match
import com.irvanyale.app.footballapp.utils.toSimpleString
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class MatchAdapter (private val matchs: List<Match>, private val onItemClickCallback: OnItemClickCallback)
    : RecyclerView.Adapter<MatchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(MatchUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = matchs.size

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bindItem(matchs[position], onItemClickCallback)
    }
}

class MatchUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {

            linearLayout {
                id = R.id.lnly_match
                orientation = LinearLayout.VERTICAL
                padding = dip(6)
                lparams(width = matchParent, height = wrapContent)

                textView {
                    id = R.id.txt_date_match
                    textSize = 14f
                    textColor = Color.MAGENTA

                }.lparams(width = matchParent, height = wrapContent){
                    bottomMargin = dip(20)
                }.gravity = Gravity.CENTER_HORIZONTAL

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    weightSum = 1f

                    relativeLayout {
                        padding = dip(4)

                        textView {
                            id = R.id.txt_home_team
                            textSize = 16f
                            textColor = Color.BLACK

                        }.lparams(width = wrapContent, height = wrapContent){
                            alignParentLeft()
                            leftOf(R.id.txt_home_score)
                            gravity = Gravity.CENTER_VERTICAL
                        }.gravity = Gravity.END

                        textView {
                            id = R.id.txt_home_score
                            textSize = 20f
                            textColor = Color.BLACK

                        }.lparams(width = wrapContent, height = wrapContent){
                            leftMargin = dip(26)
                            alignParentRight()
                            gravity = Gravity.CENTER
                        }.gravity = Gravity.CENTER

                    }.lparams(width = dip(0), height = wrapContent){
                        weight = 0.45f
                        gravity = RelativeLayout.CENTER_VERTICAL
                    }

                    textView {
                        text = ctx.getString(R.string.v)
                        textSize = 18f
                        textColor = Color.BLACK
                        padding = dip(4)

                    }.lparams(width = dip(0), height = matchParent){
                        weight = 0.1f
                        gravity = Gravity.CENTER
                    }.gravity = Gravity.CENTER

                    relativeLayout {
                        padding = dip(4)

                        textView {
                            id = R.id.txt_away_team
                            textSize = 16f
                            textColor = Color.BLACK

                        }.lparams(width = wrapContent, height = wrapContent){
                            alignParentRight()
                            rightOf(R.id.txt_away_score)
                            gravity = Gravity.CENTER_VERTICAL
                        }.gravity = Gravity.START

                        textView {
                            id = R.id.txt_away_score
                            textSize = 20f
                            textColor = Color.BLACK

                        }.lparams(width = wrapContent, height = wrapContent){
                            rightMargin = dip(26)
                            alignParentLeft()
                            gravity = Gravity.CENTER
                        }.gravity = Gravity.CENTER

                    }.lparams(width = dip(0), height = wrapContent){
                        weight = 0.45f
                        gravity = RelativeLayout.CENTER_VERTICAL
                    }
                }
            }
        }
    }
}

class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val lnlyMatch: LinearLayout = view.find(R.id.lnly_match)
    private val date: TextView = view.find(R.id.txt_date_match)
    private val homeTeam: TextView = view.find(R.id.txt_home_team)
    private val homeScore: TextView = view.find(R.id.txt_home_score)
    private val awayTeam: TextView = view.find(R.id.txt_away_team)
    private val awayScore: TextView = view.find(R.id.txt_away_score)

    fun bindItem(match : Match, onItemClickCallback: OnItemClickCallback){

        var formatedDate = "-"

        if (match.date != null){
            val dates: Date = SimpleDateFormat("yyyy-MM-dd").parse(match.date)
            formatedDate = toSimpleString(dates) ?: "-"
        }

        date.text = formatedDate
        homeTeam.text = match.homeTeam?: "-"
        homeScore.text = match.homeScore?: "-"
        awayTeam.text = match.awayTeam?: "-"
        awayScore.text = match.awayScore?: "-"

        lnlyMatch.onClick {
            onItemClickCallback.onItemClicked(match)
        }
    }
}

interface OnItemClickCallback{
    fun onItemClicked(match: Match)
}