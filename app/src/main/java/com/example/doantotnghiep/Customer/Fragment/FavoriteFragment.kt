package com.example.doantotnghiep.Customer.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.FavoriteAdapter
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.MyFavoriteProduct
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.FragmentFavoriteBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() , IClickItem {
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
    lateinit var viewBinding : FragmentFavoriteBinding
    lateinit var adapterFavorite : FavoriteAdapter
    lateinit var viewModelFavorite : ProductViewModel
    lateinit var viewModelUser     : UserViewModel
    lateinit var loadingFavorite  : CustomProgressBar
    var mListFavoriteProduct : MutableList<MyFavoriteProduct> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        viewBinding = FragmentFavoriteBinding.inflate(LayoutInflater.from(context))
        activity?.apply {
            loadingFavorite = CustomProgressBar(this)
            //loadingFavorite.showProgressBar(this)
        }
        viewBinding.loadingData.visibility = View.VISIBLE


        context?.let {

            adapterFavorite = FavoriteAdapter(mListFavoriteProduct,it, this)

        }
        viewModelFavorite = ViewModelProvider(this)[ProductViewModel::class.java]


        val user = ShareReference.getUser()
        activity?.let {
            it.actionBar?.apply {
                title = "Favorite Product"
            }
            viewModelUser.getListShop();
            viewModelUser.getLiveDataListUser1().observe(it, Observer {
                if (it != null && it.size > 0 ) {
                    mListFavoriteProduct.clear()
                    for (data in it ) {
                        Log.d( "data: ", "${data.toString()}")
                        viewModelFavorite.getMyFavoriteProduct(data.id!!,user.id!!).observe(requireActivity(), Observer {
                            if(it != null && it.size > 0) {
                                Log.d( "viewModelFavorite1_2: ","${it.toString()}")
                                viewBinding.labelData.visibility = View.GONE
                                viewBinding.skimmerLayout.stopShimmer()
                                viewBinding.skimmerLayout.visibility = View.GONE
                                //adapterFavorite.differ.submitList(mListFavorite)
                                mListFavoriteProduct.addAll(it)
                                adapterFavorite.notifyDataSetChanged()
                            }
                        })
                    }
                    Log.d( "mListFavorite: ", "${mListFavoriteProduct.toString()}")
                    if (mListFavoriteProduct.size <= 0) {
                        viewBinding.labelData.visibility = View.VISIBLE
                        viewBinding.skimmerLayout.stopShimmer()
                        viewBinding.skimmerLayout.visibility = View.GONE
                    }
                    viewBinding.loadingData.visibility = View.INVISIBLE
                }
            })

        }
        viewBinding?.apply {
            this.recyclerviewFavorite?.apply {
                this.adapter = adapterFavorite
                this.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                hasFixedSize()
//                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        }
        viewBinding.skimmerLayout.startShimmer()
        return viewBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getPosition(index: Int) {

    }
}