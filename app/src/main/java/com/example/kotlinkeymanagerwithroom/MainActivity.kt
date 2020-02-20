package com.example.kotlinkeymanagerwithroom

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinkeymanagerwithroom.database.AccountEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AccountListAdapter.Interaction {

    private val LOG_TAG = MainActivity::class.java.simpleName
    private lateinit var viewModel: AccountViewModel
    private lateinit var adapter: AccountListAdapter
    companion object{



        const val INPUT_REQUEST_CODE :Int = 400
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(Constant.NAME_TYPE, Constant.VALUE_TYPE_INSERT)
            startActivityForResult(intent, INPUT_REQUEST_CODE)
        }


        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.allAccounts.observe(this, Observer { accounts ->

            for(item in accounts){

                Log.d(LOG_TAG, "account id   - " + item.accountId)
                Log.d(LOG_TAG, "account name - " + item.name)
                Log.d(LOG_TAG, item.link)
                Log.d(LOG_TAG, item.comment)
                Log.d(LOG_TAG, item.image)
                Log.d(LOG_TAG, item.userId)
                Log.d(LOG_TAG, item.pwd)
                Log.d(LOG_TAG, "--------------------")

            }

            setRecyclerViewData(accounts.toList())
        })

        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = AccountListAdapter(this)


    }

    fun setRecyclerViewData(list : List<AccountEntity>){

        adapter.submitList(list)
        recyclerview.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == INPUT_REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK){

                val account : AccountEntity = data?.getParcelableExtra("account")!!
                if(data?.getStringExtra(Constant.NAME_TYPE) == Constant.VALUE_TYPE_INSERT)
                    viewModel.insert(account)
                else
                    viewModel.update(account)
            }
        }
    }

    override fun onItemSelected(position: Int, item: AccountEntity) {

        val intent = Intent(this, InputActivity::class.java)
        intent.putExtra(Constant.NAME_TYPE, Constant.VALUE_TYPE_UPDATE)
        intent.putExtra(Constant.NAME_ACCOUNT, item)
        startActivityForResult(intent, INPUT_REQUEST_CODE)
    }
}
