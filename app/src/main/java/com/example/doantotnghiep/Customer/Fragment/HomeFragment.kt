package com.example.doantotnghiep.Customer.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.CategoryAdapter
import com.example.doantotnghiep.Customer.CustomerActivity.Companion.mCategory
import com.example.doantotnghiep.Customer.ListProductForCustomerActivity
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.CategoryViewModel
import com.example.doantotnghiep.databinding.ActivityCustomerBinding
import com.example.doantotnghiep.databinding.FragmentHomeBinding
import es.dmoral.toasty.Toasty

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
    lateinit var viewModelCate : CategoryViewModel
    lateinit var progress : CustomProgressBar
    var mListCategory : MutableList<Category> = mutableListOf()
    lateinit var viewBinding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewBinding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        viewBinding.loadingCategory.visibility = View.VISIBLE
        activity?.apply {
            actionBar?.apply {
                title = "List Category"
//                setDisplayHomeAsUpEnabled(true)
            }
        }
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
        viewBinding.recyclerType?.apply {
            adapter = adapterCate
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
        }
//        mListCategory.add(Category(System.currentTimeMillis().toString(),"Category 1"))
//        adapterCate.notifyDataSetChanged()
        viewBinding.shimmerLayoutCategory.startShimmer()
        return viewBinding.root
    }
    override fun getPosition(index: Int) {
        context?.let { Toasty.info(it,"position : ${index}" , Toasty.LENGTH_SHORT ).show() }
        Log.d(Constanst.log, "${index}: ")
        viewModelCate.checkExistCategory(mListCategory[index].id!!).observe(this, Observer {
            if (it == true ) {
                val intent = Intent(activity, ListProductForCustomerActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("object", mListCategory[index])
                mCategory = mListCategory[index]
                intent.putExtras(bundle)
                startActivity(intent)
                context?.let { it1 -> Toasty.info(it1, " Có sản phẩm", Toasty.LENGTH_SHORT).show() }
            }else {
                context?.let { it1 -> Toasty.info(it1, "Không có sản phẩm", Toasty.LENGTH_SHORT).show() }
            }
        })
    }

    companion object {
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
}