package com.example.doantotnghiep.Customer.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.CartAdapter
import com.example.doantotnghiep.Customer.CustomerActivity.Companion.mProduct
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.CartViewModel
import com.example.doantotnghiep.ViewModel.OrderViewModel
import com.example.doantotnghiep.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() , IClickItem {
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
    lateinit var viewBinding : FragmentCartBinding
    lateinit var adapterCart : CartAdapter
    lateinit var viewModelCart : CartViewModel
    lateinit var viewModelOrder     :OrderViewModel
    lateinit var loadingCart   : CustomProgressBar
    var numberProduct : Int = 0
    var total : Float = 0F
    var listCart = mutableListOf<Cart>()
    var uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.apply { loadingCart = CustomProgressBar(this)
            //loadingCart.showProgressBar(this)
        }
        viewModelOrder   = ViewModelProvider(this)[OrderViewModel::class.java]
        viewBinding = FragmentCartBinding.inflate(LayoutInflater.from(context))
        viewModelCart = ViewModelProvider(this)[CartViewModel::class.java]
        context?.let {
            adapterCart = CartAdapter(listCart,it, this)
        }
        viewBinding.progressBar.visibility = View.VISIBLE

        viewBinding?.apply {
            this.recyclerviewCart.apply {
                hasFixedSize()
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = adapterCart
                addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
            }
        }
        activity?.apply {
            viewModelCart.getListCartProduct(uid).observe(this, Observer {
                if (it == null ) {
                    viewBinding.labelData.text = "Not Data"
                    viewBinding.progressBar.visibility = View.INVISIBLE
                }else if (it.size == 0) {
                    viewBinding.labelData.text = "Not Data"
                    viewBinding.progressBar.visibility = View.INVISIBLE

                }else {
                    viewBinding.labelData.visibility = View.GONE
                    viewBinding.progressBar.visibility = View.INVISIBLE
                    it?.apply {
                        listCart.addAll(this)
                        adapterCart.notifyDataSetChanged()
                        numberProduct += this.size
                        for (data in listCart) {
                            total = total + data.totalPrice
                        }
                        viewBinding.btnCompleteOrder.text = "Completed : ${total} VND"
                    }
                }
                viewBinding.shimmerLayout.stopShimmer()
                viewBinding.shimmerLayout.visibility = View.GONE
                //loadingCart.dismissDialog()
            })

        }
        viewBinding.btnCompleteOrder.setOnClickListener {
            viewBinding.progressBar.visibility = View.VISIBLE
            var idOrder = System.currentTimeMillis()
            Log.d("ID_ORDER", idOrder.toString())
            var user = ShareReference.getUser()
            for (data in listCart){
                var order = Order(idOrder.toString(),user.id!!,null,total.toInt(),numberProduct,1,System.currentTimeMillis().toString())
                activity?.apply {
                    //Log.d("Product = ", mProduct.toString())
                    viewModelOrder.addOrdersProduct(user.id!!, idOrder.toString().trim(), data.prod!!.id!!,order,data.prod!!.price!!.toInt()).
                    observe(this,
                        Observer {
                            Log.d(Constanst.log, "addOrdersProduct: finshed ")
                        })

                }
           }
            viewModelCart.removeCartProduct(user.id!!)

        }
        activity?.apply {

            viewModelCart.getLiveDataCart1().observe(this, Observer {
                viewBinding.progressBar.visibility = View.INVISIBLE
                listCart.clear()
                adapterCart.notifyDataSetChanged()
                Log.d("getLiveDataCart1", it.toString())
                if (it == true){

                }else{

                }
            })
        }

        viewBinding.shimmerLayout.startShimmer()

        return viewBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getPosition(index: Int) {

    }
}