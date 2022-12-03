package com.example.doantotnghiep.Repository

import android.annotation.SuppressLint
import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.Exception
import com.example.doantotnghiep.Model.ResponseProduct as ResponseProduct1

class Repository {

//    lateinit var  = MutableLiveData()
    var listUsers = mutableListOf<User>()
    fun updateRegisFirebase(user: User) {

        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(user.id!!)
        ref.setValue(user)
    }
    fun removeProductCart( idUser: String, livedata: MutableLiveData<Boolean>) {
        val refer = FirebaseDatabase.getInstance()
            .getReference("Carts/${idUser}")
        refer.removeValue().addOnSuccessListener {
            livedata.postValue(true)
        }.addOnFailureListener {
            livedata.postValue(false)
        }
    }
    suspend fun addProductCart (idUser: String,totalProduct: Int , idProduct: String, cart: Cart) {

        val refer = FirebaseDatabase.getInstance()
            .getReference("Carts/${idUser}/${idProduct}")
        val result = refer.get().await()
        var numberChoice = 1

        if (result.hasChildren()) {
            Log.d( "addProductCart: ","VAO")
            var cart = result.getValue(Cart::class.java)
            cart?.apply {

                this.totalPrice += totalProduct
                this.numberChoice += numberChoice

                Log.d( "addProductCart: ",this.toString())
                val ref = FirebaseDatabase.getInstance()
                    .getReference("Carts/${idUser}/${idProduct}")
                try {
                    ref.setValue(this).await()
                }catch (e : Exception) {
                }
            }
        }else {
            Log.d( "addProductCart: ","CAO")
            cart.numberChoice += numberChoice
            cart.totalPrice   += totalProduct
            val ref = FirebaseDatabase.getInstance()
                .getReference("Carts/${idUser}/${idProduct}")
            try {
                ref.setValue(cart).await()
            }catch (e : Exception) {
            }
        }

        //Log.d( "addProductCart: ",cart.toString())


    }

    suspend fun getListCart(idUser : String ) : List<Cart> {
        val ref = FirebaseDatabase.getInstance().
                getReference("Carts").child(idUser)

        val res : List<Cart> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Cart::class.java)!!
        }
        return res
    }
    suspend fun getMyFavoriteProduct(idUser : String ) : List<MyFavoriteProduct> {
        val ref = FirebaseDatabase.getInstance()
            .getReference("MyFavoriteProduct").child(idUser)

        val res : List<MyFavoriteProduct> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(MyFavoriteProduct::class.java)!!
        }

        return res

    }
    fun checkLoginAccount(uid : String , livedata: MutableLiveData<User>) {
        val ref = FirebaseDatabase.getInstance().getReference("Accounts").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0 != null){
                    val user : User? = p0.getValue(User::class.java)
                    livedata.postValue(user)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                livedata.postValue(null)
            }

        })
    }

    suspend fun addProductToFirebase( id: String ,product: Product)  {
        val ref = FirebaseDatabase.getInstance().getReference("Products").child(id)
        try{
            ref.setValue(product).await()
        }catch (e: Exception){
        }
    }
    suspend fun getListUsersCoroutine() : List<User> {
        val ref = FirebaseDatabase.getInstance().getReference("Accounts")
        val res : List<User> = ref.get().await().children.map {
            dataSnapshot ->   dataSnapshot.getValue(User::class.java)!!
        }
        return res
    }
    suspend fun addCategory(cat : Category) {
        val ref = FirebaseDatabase.getInstance().getReference("Category").child(cat.id!!)
        ref.setValue(cat).await()
    }
    suspend fun getListCategoryFirebase () : List<Category>{
        val ref = FirebaseDatabase.getInstance().getReference("Category")
        val res : List<Category> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Category::class.java)!!
        }
        return res
    }
    suspend fun checkExistCategoryFirebase(idCategory : String ) : Boolean {
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        val res : List<Product> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Product::class.java) !!
        }
        val tmp = res.size
//        Log.d(Constanst.log
//            , "${tmp} san pham ")
        if ( tmp == 0 ){
            Log.d(Constanst.log
                , " Vao false ")
            return false
        }
//        Log.d(Constanst.log
//            , "Chua Vao ")
        for (d in res ) {
//            Log.d(Constanst.log
//                , "Vao ")
//            Log.d(Constanst.log
//                , "${d} ")
            if(d?.idCategory.equals(idCategory)) return true
        }
        return false
    }
    suspend fun getListMyFavoriteProduct(idUser : String ) : List<MyFavoriteProduct> {

        val ref = FirebaseDatabase.getInstance().
                  getReference("MyProductFavorite").child(idUser)

        val res : List<MyFavoriteProduct> = ref.get().await().children.map {
            dataSnapshot ->  dataSnapshot.getValue(MyFavoriteProduct::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
     fun addRating( idProduct: String , rate : Rating ,  livedata: MutableLiveData<Float>) {
        var f = false
        val ref = FirebaseDatabase.getInstance().getReference("Ratings").child(idProduct).child(rate.idUser!!)
        ref.setValue(rate)
         val ref1 = FirebaseDatabase.getInstance().getReference("Ratings").child(idProduct)
         val res = ref1.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {

                 if (snapshot != null && snapshot.children.count() > 0) {
                     var tmpRating = (0).toFloat()
                     var number = 0
                     for (d in snapshot.children) {
                        tmpRating += d.getValue(Rating::class.java)?.rateNumber?.toFloat()!!
                        number+= 1
                     }
                    livedata.postValue(tmpRating/number)
                 }
             }

             override fun onCancelled(error: DatabaseError) {
             }

         })

    }
    fun getRating( idProduct: String, liveData: MutableLiveData<Float>) {
        val ref1 = FirebaseDatabase.getInstance().getReference("Ratings").child(idProduct)
        val res = ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot != null && snapshot.children.count() > 0) {
                    var tmpRating = (0).toFloat()
                    var number = 0
                    for (d in snapshot.children) {
                        tmpRating += d.getValue(Rating::class.java)?.rateNumber?.toFloat()!!
                        number+= 1
                    }
                    liveData.postValue(tmpRating/number)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    suspend fun addFavoriteProduct(idUser: String , idProduct: String, prod : Product) : Boolean {
        var f = false
        Log.d(Constanst.log, "addFavoriteProduct: before ")

        val ref = FirebaseDatabase.getInstance().getReference("MyProductFavorite").child(idUser)
            .child(idProduct)
        Log.d(Constanst.log, "addFavoriteProduct: before 1")
        try {
            val value = MyFavoriteProduct(System.currentTimeMillis().toString(), prod, 1)
            ref.setValue(value).await()
            Log.d(Constanst.log, "addFavoriteProduct: before 2")

            f = true
        }catch (e : Exception) {
            f = false
        }
        Log.d(Constanst.log, "addFavoriteProduct: $f")
        return f

    }
    suspend fun removeFavoriteProduct(idUser: String, idProduct: String) : Boolean{
        var f = false
        val ref = FirebaseDatabase.getInstance().getReference("MyProductFavorite").child(idUser)
            .child(idProduct)
        try {
            ref.removeValue().await()
            f = true
        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun getStatusFavoriteProduct(idUser: String, idProduct: String) : Int {
        val ref = FirebaseDatabase.getInstance().getReference("MyProductFavorite").child(idUser)
            .child(idProduct)

        val res = ref.get().await().getValue(MyFavoriteProduct::class.java)


        if (res == null ) return 0
        Log.d(Constanst.log, "status: ${res} ")
        return res.likes

    }
    suspend fun addComment(idProduct : String , idComment : String, comment : Comment)  :Int {
        var f = 0 ;
        val ref = FirebaseDatabase.getInstance().getReference("Comments")
            .child(idProduct).child(idComment)

        try {
            val res = ref.setValue(comment).await() ;
            f = 1;
        }catch (e : Exception) {
            f = 0;
        }
        Log.d(Constanst.log, "add comment : ${f} ")

        return f ;
    }
    suspend fun getCommentProduct(idProduct : String ) : List<Comment> {
        val ref = FirebaseDatabase.getInstance().getReference("Comments").child(idProduct)

        val res : List<Comment> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Comment::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
    suspend fun getCartProduct(idUser : String, idProduct : String) : List<Cart> {
        val ref = FirebaseDatabase.getInstance().getReference("Carts")
                                  .child(idUser)
        val res : List<Cart> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Cart::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
    suspend fun addOrders(idUser: String, idOrder : String, idProduct: String, order : Order, price : Int) {
        // debugURL = "Orders/lcEQWgtRiqfM4ijc8T3aliUs3Uk2/1669518274463/1664620687209"
        //              "Orders/lcEQWgtRiqfM4ijc8T3aliUs3Uk2/1669519313505/1664620687209"
        var url = "Orders/${idUser.toString().trim()}/${idOrder.toString().trim()}/${idProduct.toString().trim()}"
        val refer = FirebaseDatabase.getInstance()
            .getReference(url)
        Log.d( "URL: ", url.toString())
        val result = refer.get().await()

        if (result != null) {
            var order1 = result.getValue(Order::class.java)
            Log.d( "order1: ", order1.toString())
            if (order1 == null){
                try {
                    refer.setValue(order).await()
                }catch (e : Exception){

                }
            }else {

                order1?.apply {
                    this.totalPrice += price
                    this.numberOptions += 1
                    Log.d( "order1 after: ", this.numberOptions.toString()+ " " + this.totalPrice.toString())
                    try {
                        FirebaseDatabase.getInstance()
                            .getReference(url).child("totalPrice").
                            setValue(this.totalPrice)
                            .addOnSuccessListener {
                                Log.d("order1_update", "successfully: ")
                            }.addOnFailureListener {
                                Log.d("order1_update", "${it.message.toString()}: ")
                            }.await()
                        refer.child("numberOptions").setValue(this.numberOptions).await()
                        Log.d("ORDER_1","ORDER 1 to firebase")
                    } catch (e: Exception) {
                    }
                }
            }
        }else {
            try {
                refer.setValue(order).await()
            }catch (e : Exception){

            }
        }
    }
    suspend fun getListOrders(idUser : String) : List<Order> {
        val ref = FirebaseDatabase.getInstance().
                                   getReference("Orders").
                                   child(idUser)
        val res : List<String> = ref.get().await().children .map {
                dataSnapshot -> dataSnapshot.key!!
        }
        Log.d("listkeyorders", res.toString())
        var listOrders = mutableListOf<Order>()
        for (data in res) {
            val res : List<Order> = ref.child(data).get().await().children.map {
                dataSnapshot -> dataSnapshot.getValue(Order::class.java)!!
            }
            listOrders.addAll(res)
        }
        Log.d(Constanst.log, "res :  ${listOrders.size}" )
        return  listOrders ;
    }
    suspend fun  getListCommentProduct(id : String ): List<Comment> {
        val ref = FirebaseDatabase.getInstance().getReference("Comments").child(id)
        var res : List<Comment> = mutableListOf()
            res =  ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Comment::class.java)!!
        }
        return  res ;
    }
    suspend fun getListProductCoroutine(): List<Product> {
        Log.d(Constanst.log, "List Product")
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        val res: List<Product> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Product::class.java)!!
        }
        Log.d(Constanst.log, "${res}: ")
        return res
    }
    suspend fun removeProduct(id: String) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(id)
        var f = true
        try {
            refs.removeValue().await()

        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun addProduct(p : Product) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(p.id!!)
        var f = true
        try {
            refs.setValue(p).await()

        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun editProduct(id : String, p : Product) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(id)
        var f = true
        try {
            refs.setValue(p).await()

        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun editRatingProduct(idProduct: String, rate : Double) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(idProduct).child("rate")
        var f = true
        try {
            refs.setValue(rate).await()
        } catch (e: Exception) {
            f = false
        }
        return f
    }
    suspend fun editNumberProduct(idProduct: String, number: Int) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(idProduct).child("number")
        var f = true
        try {
            refs.setValue(number).await()
        } catch (e: Exception) {
            f = false
        }
        return f
    }
    suspend fun getNumberProduct(idProduct: String) : Int{
        val refs = FirebaseDatabase.getInstance().getReference("Products/${idProduct}")
        var res = refs.get().await()
        if (res == null ) return  0
//        res?.apply {
            val product = res.getValue(Product::class.java)
            if (product == null ) return  0
            return product.number!!
//        }
    }
    fun getListUsers(liveData : MutableLiveData<MutableList<User>>) {
//        listUsers.clear()
        val ref = FirebaseDatabase.getInstance().getReference("Accounts")
        ref.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0 != null && p0.children != null) {
                    listUsers.clear()
                    for(data in p0.children) {
                        val user : User? = data.getValue(User::class.java)
                        listUsers.add(user!!)
                    }
                    liveData.postValue(listUsers)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                liveData.postValue(mutableListOf())
            }

        })
    }

    fun RegisterUser(user : User, regisLiveData : MutableLiveData<FirebaseUser>) {

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {
//                    Log.d("onComplete: ", "VAO + ${p0.isSuccessful}  ${p0.exception}")
                    if (p0.isSuccessful) {
                        if (firebaseAuth.currentUser != null) {
                            user.id = firebaseAuth.currentUser!!.uid
                            updateRegisFirebase(user)
//                            Log.d("onComplete: ", "VAO 1")
                            ShareReference.putUser(user)
                            regisLiveData.postValue(firebaseAuth.currentUser)
                        }
                    }
                }

            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Log.d("onFailure: ", "fail")
                    regisLiveData.postValue(null)
                }
            })
    }
    fun Login(strUser : String , strPass : String , livedata : MutableLiveData<FirebaseUser>) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(strUser, strPass)
            .addOnSuccessListener  (object : OnSuccessListener<AuthResult>{
                override fun onSuccess(p0: AuthResult?) {
                    livedata.postValue(FirebaseAuth.getInstance().currentUser)
                }

            })
            .addOnFailureListener(object : OnFailureListener{
                override fun onFailure(p0: Exception) {
                    livedata.postValue(null)
                }

            })
    }
    companion object {
        var instance : Repository ? = null

        fun getRepository() : Repository {
            if (instance == null ) {
                instance = Repository()
            }
            return instance!!
        }

    }

}