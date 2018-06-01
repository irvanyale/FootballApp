package com.irvanyale.app.footballapp.main.team

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import com.irvanyale.app.footballapp.R
import android.widget.*
import com.google.gson.Gson
import com.irvanyale.app.footballapp.main.TeamsView
import com.irvanyale.app.footballapp.model.Team
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.presenter.TeamsPresenter
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamFragment : Fragment(), AnkoComponent<Context>, TeamsView {

    private var teams: MutableList<Team> = mutableListOf()
    private lateinit var presenter: TeamsPresenter
    private lateinit var adapter: TeamsAdapter
    private lateinit var spinner: Spinner
    private lateinit var listEvent: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var leagueName: String
    private lateinit var toolbar: Toolbar
    private lateinit var strQuery: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        spinner.adapter = spinnerAdapter

        adapter = TeamsAdapter(teams) {
            ctx.startActivity<TeamDetailActivity>("id" to "${it.teamId}")
        }
        listEvent.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()

        presenter = TeamsPresenter(this, request, gson)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = spinner.selectedItem.toString()
                presenter.getTeamList(leagueName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        strQuery = ""
        swipeRefresh.onRefresh {
            presenter.getTeamList(leagueName)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_search, menu)

        if (menu != null){
            val searchItem = menu.findItem(R.id.action_search)
            if (searchItem != null){
                val searchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!TextUtils.isEmpty(query)) presenter.getTeamListBySearch(query)
                        strQuery = query.toString()
                        spinner.visibility = View.GONE
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })

                searchView.setOnCloseListener {
                    spinner.visibility = View.VISIBLE
                    true
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {

        coordinatorLayout {
            lparams(width = matchParent, height = matchParent)
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {

                toolbar = themedToolbar {
                    R.style.ThemeOverlay_AppCompat_Light
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                }.lparams(width = matchParent, height = wrapContent) {
                    scrollFlags = 0
                }

            }.lparams(width = matchParent, height = wrapContent)

            linearLayout {
                lparams(width = matchParent, height = matchParent)
                orientation = LinearLayout.VERTICAL
                topPadding = dip(16)
                leftPadding = dip(16)
                rightPadding = dip(16)

                spinner = spinner ()

                view {
                    background = ContextCompat.getDrawable(ctx, R.drawable.shadow)
                }.lparams(width = matchParent, height = dip(1))

                swipeRefresh = swipeRefreshLayout {
                    setColorSchemeResources(R.color.colorAccent,
                            android.R.color.holo_green_light,
                            android.R.color.holo_orange_light,
                            android.R.color.holo_red_light)

                    relativeLayout{
                        lparams (width = matchParent, height = wrapContent)

                        listEvent = recyclerView {
                            lparams (width = matchParent, height = wrapContent)
                            layoutManager = LinearLayoutManager(ctx)
                        }
                    }
                }
            }.lparams(width = matchParent, height = matchParent){
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showTeamList(data: List<Team>?) {
        swipeRefresh.isRefreshing = false
        teams.clear()
        if (data != null && data.isNotEmpty()){
            teams.addAll(data)
        }
        adapter.notifyDataSetChanged()
    }
}