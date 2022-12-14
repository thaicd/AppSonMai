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
import com.example.doantotnghiep.Adapter.CartAdapter
import com.example.doantotnghiep.Customer.Fragment.HomeFragment.Companion.listUser
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Model.OrderDetails
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.CartViewModel
import com.example.doantotnghiep.ViewModel.OrderViewModel
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.FragmentCartBinding
import com.example.doantotnghiep.interface_item.ItemChange
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
class CartFragment : Fragment() , IClickItem, ItemChange {
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
    lateinit var viewModelUser : UserViewModel
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
        flag = 0;
        // Inflate the layout for this fragment
        activity?.apply { loadingCart = CustomProgressBar(this)
            //loadingCart.showProgressBar(this)
        }
        viewModelOrder   = ViewModelProvider(this)[OrderViewModel::class.java]
        viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        viewBinding = FragmentCartBinding.inflate(LayoutInflater.from(context))
        viewModelCart = ViewModelProvider(this)[CartViewModel::class.java]
        context?.let {
            adapterCart = CartAdapter(listCart,it, this,this)
        }
        viewBinding.progressBar.visibility = View.VISIBLE

        viewBinding?.apply {
            this.recyclerviewCart.apply {
                hasFixedSize()
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = adapterCart
              //  addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
            }
        }
        activity?.apply {
            viewModelCart.liveDataListCart.observe(this, Observer {
                //var tempListCart = mutableListOf<Cart>()
                //listCart.clear()
                listCart.clear()
                total = 0F
                numberProduct = 0
                Log.d("IT: ", "${it.toString()}")
                //Log.d("liveDataListCart ", " ${listCart} ")
                if (it == null ) {
                    viewBinding.labelData.text="Cart is empty"
                    viewBinding.progressBar.visibility = View.INVISIBLE
                }else if (it.size == 0) {
                    viewBinding.labelData.text="Cart is empty"
                    viewBinding.progressBar.visibility = View.INVISIBLE

                }else {
                    viewBinding.labelData.visibility = View.GONE
                    viewBinding.progressBar.visibility = View.INVISIBLE
                    viewBinding.btnCompleteOrder.visibility = View.VISIBLE


                        // Get List Cart
                    Log.d("FLAG", "${flag}: ")
                    if (flag == 0) {
                        listCart.clear()
                        if (it != null && it.size > 0) {
                            Log.d("ỈT SIZE = ", "${it.size} ")
                            for (data in it) {
                                Log.d("listUserSIZE = ", " ${listUser.toString()} ")
                                for (data1 in listUser) {
                                    if (data1.role_id == 1 && data.idShop == data1.id) {
                                        listCart.add(data)
                                    }
                                }
                                total = total + data.totalPrice
                            }
                            Log.d("listCart: ", "${listCart.toString()}")
                            adapterCart.notifyDataSetChanged()
                        }

                        viewBinding.btnCompleteOrder.text = "Completed : ${total.toLong()} VND"
                    } else {
                        listCart.clear()
                        viewBinding.btnCompleteOrder.visibility = View.GONE
                        viewBinding.labelData.visibility = View.VISIBLE
                        viewBinding.labelData.text = "Cart is empty"
                    }
                }
                viewBinding.shimmerLayout.stopShimmer()
                viewBinding.shimmerLayout.visibility = View.GONE
            })
            viewModelCart.getListCartProduct1(uid);

        }
        viewBinding.btnCompleteOrder.setOnClickListener {
            flag = 1;
            viewBinding.progressBar.visibility = View.VISIBLE
            var idOrder = System.currentTimeMillis()
            var user = ShareReference.getUser()
            var timeStamp = System.currentTimeMillis()
            var totalPrice = 0
            var totalNumber = 0
            Log.d("list_cart", "${listCart}")
            for (data in listCart){
                totalPrice += data.totalPrice
                totalNumber += data.numberChoice
//                Log.d("totalPrice = ", " ${data.totalPrice} : ${data.numberChoice} ")
                var order_details = OrderDetails(idOrder.toString(),data.numberChoice,data.totalPrice,user.id!!,data.prod,timeStamp.toString())
                activity?.apply {
                    viewModelOrder.addOrdersDetailProduct(user.id!!,idOrder.toString(),data.prod!!,order_details)
                        .observe(this, Observer {
                            Log.d( "viewModelOrder: ", "Get order_details")
                        })
                    var order = Order(idOrder.toString(),user.id!!, data.idShop,data.nameShop,null,totalPrice.toInt(),totalNumber,1,System.currentTimeMillis().toString())
                    activity?.apply {
//                Log.d("Product = ", data.prod.toString())
                        viewModelOrder.addOrdersProduct(user.id!!, idOrder.toString().trim(),order,0).
                        observe(this,
                            Observer {
                                Log.d(Constanst.log, "addOrdersProduct: finshed ")
                            })
                    }
                }
           }
            Log.d("totalPrice = ", " ${totalPrice} : ${totalNumber} ")


            viewModelCart.removeCartProduct(user.id!!)
            viewBinding.btnCompleteOrder.visibility = View.INVISIBLE
            viewBinding.labelData.visibility = View.VISIBLE
            viewBinding.labelData.text="Cart is empty"
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
        var flag = 0
        var hashMap = hashMapOf<String, MutableList<Cart>>()
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

    override fun changeItem(text: String) {
        activity?.apply {
            //Toasty.info(this, text, Toasty.LENGTH_SHORT).show()
            val character = text[0]
            val number = text.substring(1, text.length).toFloat()

            when(character) {
                '+' -> total += number
                '-' -> total -= number
                else -> total -= 0
            }
            Log.d("TOTAL = ", " ${total} ")
            if (total != 0F ) {
                viewBinding.btnCompleteOrder.text = "Completed : ${total.toLong()} VND"
            }else {
                viewBinding.btnCompleteOrder.visibility = View.INVISIBLE
                viewBinding.labelData.visibility = View.VISIBLE
                viewBinding.labelData.text="Cart is empty"
            }
        }
    }
}