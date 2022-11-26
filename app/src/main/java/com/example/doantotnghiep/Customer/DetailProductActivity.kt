package com.example.doantotnghiep.Customer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.CommentAdapter
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Model.Comment
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Model.Rating
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.Repository.Time
import com.example.doantotnghiep.ViewModel.CommentViewModel
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.ViewModel.RatingViewModel
import com.example.doantotnghiep.databinding.ActivityDetailProductBinding
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.text.SimpleDateFormat
import java.util.*

class DetailProductActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityDetailProductBinding
    lateinit var adapterComment : CommentAdapter
    lateinit var viewModelComment : CommentViewModel
    lateinit var viewModelProduct: ProductViewModel
    lateinit var viewModelRating  : RatingViewModel
    lateinit var mProduct : Product
    lateinit var user : User
    var flag = 1 ;
    var listComment = mutableListOf<Comment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_product)
        viewBinding = ActivityDetailProductBinding.inflate(LayoutInflater.from(this))
        viewModelComment = ViewModelProvider(this)[CommentViewModel::class.java]
        viewModelProduct = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModelRating  = ViewModelProvider(this)[RatingViewModel::class.java]
        setContentView(viewBinding.root)

        user = ShareReference.getUser()
        val bundle = intent.extras

        if (bundle != null ) {
            mProduct = bundle.getSerializable("product") as Product


            Picasso.with(this).load(mProduct.imgUrl).into(viewBinding.imageProduct)
            viewBinding.itemName.text = mProduct.nameProduct
            viewBinding.itemPrice.text = mProduct.price.toString()
            viewBinding.itemDescription.text = mProduct.description.toString()
            viewModelProduct.getStatusFavorite(user.id!!,mProduct.id!!).observe(this, Observer {
                Log.d(Constanst.log, "status []: ${it} ")
                if (it == 1) {
                    flag = 0
                    viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                }else{
                    flag = 1
                    viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            })
        }

        viewModelRating.liveDataRating.observe(this, Observer {
            Log.d(Constanst.log, "rate: ${it}")
        })
        viewBinding.btnCart.setOnClickListener {

        }
        viewBinding.btnRating.setOnClickListener {
            showDialog()
        }
        viewBinding.btnFavorite.setOnClickListener {
//            Toasty.info(this,"Clicked $flag - ${user} - ${mProduct}", Toasty.LENGTH_SHORT).show()
            Log.d(Constanst.log, "Clicked $flag - ${user} - ${mProduct}")
            if (flag == 1) {
                Log.d(Constanst.log, "$flag: True ")
                viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                Log.d(Constanst.log, "$flag: True 1")
                viewModelProduct.addMyFavoriteProduct(user.id!!,mProduct.id!!, mProduct).observe(this, Observer {
                    Log.d(Constanst.log, "addFavoriteProduct: finshed ")

                })
                Log.d(Constanst.log, "$flag: True 2")
                flag = 0
            }else {
                viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                viewModelProduct.removeMyFavoriteProduct(user.id!!, mProduct.id!!).observe(this, Observer {
                    Log.d(Constanst.log, "addFavoriteProduct: finshed ")
                })
                flag = 1
            }
            viewModelProduct.addMyFavoriteProduct(user.id!!,mProduct.id!!,mProduct)
        }
        viewBinding.itemCall.setOnClickListener {
            _ ->
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
        }


        adapterComment = CommentAdapter(listComment);
        viewBinding.itemListComment?.apply {
            adapter = adapterComment
            hasFixedSize()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        viewModelComment = ViewModelProvider(this)[CommentViewModel::class.java]

        viewModelComment.getListComment(mProduct.id!!).observe(this, Observer {

            it?.apply {
                Log.d(Constanst.log, "list comment : ${this} ")

                listComment.clear()
                listComment.addAll(this)

                adapterComment.notifyDataSetChanged()

            }
        })

    }
    fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_rating)

        val window = dialog.window
        if (window == null ) return

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)
        dialog.setCancelable(true)
        val edtFeedback = dialog.findViewById<EditText>(R.id.edt_feedback)
        val btnCancel = dialog.findViewById<TextView>(R.id.btn_cancel)
        val btnSend = dialog.findViewById<TextView>(R.id.btn_send)
        val rate = dialog.findViewById<MaterialRatingBar>(R.id.item_rate)

        val feedback = edtFeedback.text.toString().trim()
        var numberRate : Float = (0).toFloat()
        rate.setOnRatingChangeListener { ratingBar, rating ->
            numberRate = rating
        }
        btnSend.setOnClickListener {
            val rate = Rating(System.currentTimeMillis().toString(), numberRate, user.id!!)
            viewModelRating.addGetRating(mProduct.id!!, rate)
            if(feedback.length > 0 ) {
                val time = Time.convertTimstampToDate()
                val comment =
                    Comment(System.currentTimeMillis().toString(), user.name, feedback, time)
                viewModelComment.addComment(mProduct.id!!, comment.idComment!!, comment)
                    .observe(this, Observer {

                    })
            }
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()



    }
}