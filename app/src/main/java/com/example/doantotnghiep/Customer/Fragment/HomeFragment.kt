package com.example.doantotnghiep.Customer.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.doantotnghiep.Adapter.CategoryAdapter
import com.example.doantotnghiep.Adapter.ImageSliderAdapter
import com.example.doantotnghiep.Adapter.ProductAdapter
import com.example.doantotnghiep.Customer.DetailProductActivity
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.FragmentHomeBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() , IClickItem {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var adapterCate : CategoryAdapter
    lateinit var adapterViewPager : ImageSliderAdapter
    lateinit var viewModelProduct : ProductViewModel
    lateinit var viewModeUser     : UserViewModel
    lateinit var adapterP : ProductAdapter
    var listProductTemp = mutableListOf<Product>()
    var mlistImage = mutableListOf<Int>()
    var mHandler = Handler(Looper.getMainLooper())
    var mRunable = Runnable {
        var current = viewBinding.viewPager2.currentItem
        if (current + 1 == mlistImage.size - 1) {
            viewBinding.viewPager2.setCurrentItem(0)
        }else {
            viewBinding.viewPager2.setCurrentItem(current + 1)
        }
    }
    var listProduct = mutableListOf<Product>()
    var mListProduct = mutableListOf<Product>()
   // var mListCategory : MutableList<Category> = mutableListOf()
    lateinit var viewBinding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewBinding = FragmentHomeBinding.inflate(LayoutInflater.from(context),container,false)
        mlistImage.add(R.drawable.banan)
        mlistImage.add(R.drawable.backgroundheader1)
        mlistImage.add(R.drawable.banantrue)
        mlistImage.add(R.drawable.woman)
        mlistImage.add(R.drawable.worker)

        viewBinding.loadingProduct.visibility = View.VISIBLE
        activity?.apply {
            adapterViewPager = ImageSliderAdapter(mlistImage,this)
        }
        viewBinding.viewPager2.adapter = adapterViewPager
        viewBinding.indicatorView.setViewPager(viewBinding.viewPager2)

        viewBinding.viewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mHandler.removeCallbacks(mRunable)
                mHandler.postDelayed(mRunable, 2000)
            }
        })
       context.let {
           adapterP = ProductAdapter(mListProduct,it!!,this)
       }

        viewModelProduct = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModeUser     = ViewModelProvider(this)[UserViewModel::class.java]
        viewModeUser.getListShop()
        activity?.apply {
            viewModeUser.getLiveDataListUser1().observe(this, Observer {
                Log.d( "viewModeUser: ", "${it.toString()}")

                if (it!= null && it.size > 0) {
                    mListProduct.clear()
                    listUser.clear()
                    for (data in it ) {
                        if (data.role_id == 1) {
                            listUser.add(data)
                            listProductTemp.clear()
                            viewModelProduct.responseLiveData(data.id!!).observe(this, Observer {
                                it?.apply {
                                    Log.d( "viewModelProduct: ", "${this.toString()}")
                                    listProduct.clear()
                                    for (p in it ) {
                                        listProduct.add(p)
                                    }
                                    if (listProduct.size > 0) {
                                        mListProduct.addAll(listProduct)
                                        listProductTemp.addAll(listProduct)
                                        adapterP.notifyDataSetChanged()
                                        viewBinding.layoutShimmer.stopShimmer()
                                        viewBinding.layoutShimmer.visibility = View.GONE
                                    }else {
                                        viewBinding.labelData.text = "Not Production"
                                    }
                                    viewBinding.loadingProduct.visibility = View.INVISIBLE
                                }
                            })
                        }
                    }
                }

            })


            viewBinding.recyclerProduct?.apply {
                adapter = adapterP
                layoutManager = GridLayoutManager(activity,2)
                hasFixedSize()
//                addItemDecoration(GridSpacingItemDecoration(2,3, false))
            }
            viewBinding.edtSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        Log.d( "onEditorAction: ",viewBinding.edtSearch.text.toString())
                        var strSearch = viewBinding.edtSearch.text.toString()
                        var listP = mutableListOf<Product>()
                        for (data in listProduct) {
                            if (data.nameProduct!!.contains(strSearch)) {
                                listP.add(data)
                            }
                        }
                        listProduct.clear()
                        if (listP.size == 0) {
                            listProduct.addAll(listProductTemp)
                        }else {
                            listProduct.addAll(listP)
                        }
                        Log.d( "onEditorAction: ","list= ${listProduct.size}")
                        adapterP.notifyDataSetChanged()
                        return true
                    }
                    hideInputKeyboard()
                    return false
                }
            })

        }



        /*
        Tải dữ liệu của category
        viewModelCate = ViewModelProvider(this)[CategoryViewModel::class.java]
        adapterCate = context?.let { CategoryAdapter(mListCategory, it, this) }!!

        activity?.let {
            viewModelCate.getListCategory().observe(it, Observer {
                it?.apply {
                    mListCategory.clear()
                    mListCategory.addAll(it)
                    adapterCate.notifyDataSetChanged()
                    viewBinding.shimmerLayoutCategory.stopShimmer()
                    viewBinding.shimmerLayoutCategory.visibility = View.GONE
                    viewBinding.loadingCategory.visibility = View.GONE
                }
            })
        }
//        viewBinding.recyclerType?.apply {
//            adapter = adapterCate
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            hasFixedSize()
//        }

         */

        viewBinding.layoutShimmer.startShimmer()
        return viewBinding.root
    }
    companion object {
        var listUser : MutableList<User>  = mutableListOf();
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun hideInputKeyboard() {
        val view: View = activity?.getCurrentFocus()!!
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    override fun getPosition(index: Int) {
        activity?.apply {
           // Toasty.info(this,"pos = $index",Toasty.LENGTH_SHORT).show()
            val intent = Intent(this, DetailProductActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("product", mListProduct.get(index))
            intent.putExtras(bundle)
            startActivity(intent)
        }

    }
}