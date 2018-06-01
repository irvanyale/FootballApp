package com.irvanyale.app.footballapp.main.team

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.model.Player
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class TeamPlayersAdapter (private val players: List<Player>, private val onItemClickCallback: OnItemClickCallback)
    : RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(PlayerUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindItem(players[position], onItemClickCallback)
    }
}

class PlayerUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {

            linearLayout {
                id = R.id.lnly_match
                padding = dip(8)
                orientation = LinearLayout.HORIZONTAL
                weightSum = 1f
                lparams(width = matchParent, height = wrapContent)

                imageView {
                    id = R.id.img_pl_pic
                }.lparams{
                    height = dip(50)
                    width = dip(0)
                    rightMargin = dip(10)
                    weight = 0.2f
                }

                textView {
                    id = R.id.txt_pl_name
                    textSize = 14f
                    textColor = Color.BLACK

                }.lparams(width = dip(0), height = wrapContent){
                    rightMargin = dip(20)
                    weight = 0.6f
                    gravity = Gravity.CENTER_VERTICAL
                }.gravity = Gravity.CENTER_VERTICAL

                textView {
                    id = R.id.txt_pl_position
                    textSize = 14f
                    textColor = Color.BLACK

                }.lparams(width = dip(0), height = wrapContent){
                    weight = 0.2f
                    gravity = Gravity.CENTER_VERTICAL
                }.gravity = Gravity.CENTER_VERTICAL
            }
        }
    }
}

class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val lnlyMatch: LinearLayout = view.find(R.id.lnly_match)
    private val plPic: ImageView = view.find(R.id.img_pl_pic)
    private val plName: TextView = view.find(R.id.txt_pl_name)
    private val plPosition: TextView = view.find(R.id.txt_pl_position)

    fun bindItem(player : Player, onItemClickCallback: OnItemClickCallback){

        if (player.playerCutout != null && !TextUtils.isEmpty(player.playerCutout)){
            Picasso.get().load(player.playerCutout).fit().into(plPic)
        }
        plName.text = player.playerName ?: "-"
        plPosition.text = player.playerPosition ?: "-"

        lnlyMatch.onClick {
            onItemClickCallback.onItemClicked(player)
        }
    }
}

interface OnItemClickCallback{
    fun onItemClicked(player: Player)
}