package com.example.doantotnghiep.Repository

import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
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