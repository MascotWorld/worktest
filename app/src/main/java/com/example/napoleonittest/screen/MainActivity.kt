package com.example.napoleonittest.screen

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.napoleonittest.Injection
import com.example.napoleonittest.R
import com.example.napoleonittest.adapters.BannerAdapter
import com.example.napoleonittest.adapters.ItemDecoration
import com.example.napoleonittest.adapters.OfferAdapter
import com.example.napoleonittest.adapters.SwipeController
import com.example.napoleonittest.data.BannerOfferViewModel
import com.example.napoleonittest.data.ViewModelFactory
import com.example.napoleonittest.models.Banner
import com.example.napoleonittest.models.Offer
import com.example.napoleonittest.net.Controller
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    val disposables = CompositeDisposable()

    private lateinit var viewModelFactory: ViewModelFactory

    private val bannerOfferViewModel: BannerOfferViewModel by viewModels { viewModelFactory }


    //view
    lateinit var rvBanners: RecyclerView
    lateinit var rvOffers: RecyclerView
    lateinit var info: ImageView
    lateinit var tabLayout: TabLayout
    lateinit var refresh: SwipeRefreshLayout

    //data
    var offers = ArrayList<Any>()
    var banners = ArrayList<Banner>()

    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
        viewModelFactory = Injection.provideViewModelFactory(this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.layout_main_activity)
        initViews()
        initSearchSettings()
        getFromDB()
        loadFromNet()

        refresh.setOnRefreshListener {
            getFromDB()
            loadFromNet()

        }
    }


    private fun initViews() {

        rvBanners = findViewById(R.id.rvBanners)
        rvOffers = findViewById(R.id.rvOffers)
        initAdapters()
        info = findViewById(R.id.ivInfo)
        tabLayout = findViewById(R.id.tabLayout)
        refresh = findViewById(R.id.refresh)

        val info = findViewById<ImageView>(R.id.ivInfo)
        info.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }


    }

    fun initSearchSettings(){

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val cvSearch = findViewById<CardView>(R.id.cvSearch)
        val ivSearch = findViewById<ImageView>(R.id.ivSearch)
        val ivMic = findViewById<ImageView>(R.id.ivMic)

    }


    private fun initAdapters() {

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvBanners)
        val llmBanners = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvBanners.layoutManager = llmBanners
        rvBanners.isNestedScrollingEnabled = false
        rvBanners.adapter = BannerAdapter(banners)


        val llmOffers = LinearLayoutManager(this)
        rvOffers.layoutManager = llmOffers
        rvOffers.setHasFixedSize(true)
        rvOffers.isNestedScrollingEnabled = true
        rvOffers.adapter = OfferAdapter(offers)
        val swipeController = SwipeController(this,300f)
        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(rvOffers)

        rvOffers.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
               swipeController.onDraw(c)
            }
        })


    }

    private fun setLoading(work: Boolean) {
        refresh.isRefreshing = work
    }

    private fun getFromDB() {

        bannerOfferViewModel.getBanner().zipWith(
            bannerOfferViewModel.getOffer(),
            BiFunction<List<Banner>, List<Offer>, Pair<List<Banner>, List<Offer>>> { t1, t2 ->
                Pair(t1, t2)
            }).map {
            val products = it.second.filter { it.type == "product" }
            val items = it.second.filter { it.type == "item" }
            val result = mutableListOf<Any>().apply {
                add(getString(R.string.promotion_header))
                addAll(products)
                add(getString(R.string.discount_header))
                addAll(items)
            }
            return@map Pair(it.first, result)


        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                update(it)
            }, {}, { setLoading(false) }, { setLoading(true) }).connect()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }


    private fun loadFromNet() {
        val controller = Controller().start()
        controller
            .getBanners()
            .zipWith(
                controller.getOffers(),
                BiFunction<List<Banner>, List<Offer>, Pair<List<Banner>, List<Offer>>> { t1, t2 ->
                    Pair(t1, t2)

                })

            .map {
                if (it.first.isNotEmpty())
                    bannerOfferViewModel.saveBanner(it.first)
                if (it.second.isNotEmpty())
                    bannerOfferViewModel.saveOffer(it.second)
                return@map it
            }
            .map {
                val products = it.second.filter { it.type == "product" }
                val items = it.second.filter { it.type == "item" }
                val result = mutableListOf<Any>().apply {
                    add("Акции")
                    addAll(products)
                    add("Скидки")
                    addAll(items)
                }
                return@map Pair(it.first, result)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                update(it)
            }, { setLoading(false) }, { setLoading(false) }, { setLoading(true) }).connect()

    }

    private fun update(it: Pair<List<Banner>, List<Any>>) {
        banners.clear()
        banners.addAll(it.first)
        offers.clear()
        offers.addAll(it.second)

        rvBanners.adapter?.let {
            it.notifyDataSetChanged()
        }


        rvOffers.adapter?.let {
            it.notifyDataSetChanged()
        }

    }


    fun Disposable.connect() {
        disposables.add(this)
    }
}