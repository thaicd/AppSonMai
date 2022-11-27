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
import com.example.doantotnghiep.Adapter.OrdersAdapter
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.OrderViewModel
import com.example.doantotnghiep.databinding.FragmentOrderBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() , IClickItem {
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
    lateinit var binding : FragmentOrderBinding
    lateinit var viewModelOrders : OrderViewModel
    lateinit var ordersAdapter: OrdersAdapter
    var uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.apply {
            ordersAdapter = OrdersAdapter(this,this@OrderFragment)
            viewModelOrders = ViewModelProvider(this)[OrderViewModel::class.java]
            viewModelOrders.getListOrders(uid).observe(this, Observer {
                if (it != null && it.size > 0) {
                    Log.d("orders", it.toString())
                    ordersAdapter.differ.submitList(it)
                    ordersAdapter.notifyDataSetChanged()
                }
            })
        }
        binding = FragmentOrderBinding.inflate(LayoutInflater.from(context))
        binding?.apply {
            this.recyclerviewOrder?.apply {
                hasFixedSize()
                adapter = ordersAdapter
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
            }
        }





        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getPosition(index: Int) {

    }
}