package com.example.kotlinkeymanagerwithroom

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.view.View
import com.example.kotlinkeymanagerwithroom.database.AccountEntity
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AppCompatActivity() {

    private lateinit var type : String
    private lateinit var account: AccountEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        type = intent.getStringExtra(Constant.NAME_TYPE)
        if(type == Constant.VALUE_TYPE_UPDATE){

            account = intent.getParcelableExtra(Constant.NAME_ACCOUNT)
            editName.setText(account.name)
            editUserId.setText(account.userId)
            editLink.setText(account.link)
            editPassword.setText(account.pwd)

            saveButton.text = "Update"

            deleteButton.visibility = View.VISIBLE;

        }else{

            saveButton.text = "Save"
            deleteButton.visibility = View.GONE;
        }

        saveButton.setOnClickListener {

            val intent = Intent()
            if(type == Constant.VALUE_TYPE_INSERT) {
                var newAccount = AccountEntity(
                    accountId = null,
                    name = editName.text.toString(),
                    userId = editUserId.text.toString(),
                    pwd = editPassword.text.toString(),
                    link = editLink.text.toString(),
                    comment = "",
                    image = ""
                )

                intent.putExtra(Constant.NAME_ACCOUNT, newAccount)

            }else{

                account.name = editName.text.toString()
                account.userId = editUserId.text.toString()
                account.pwd = editPassword.text.toString()
                account.link = editLink.text.toString()
                intent.putExtra(Constant.NAME_ACCOUNT, account)
            }

            intent.putExtra(Constant.NAME_TYPE, type)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

        deleteButton.setOnClickListener {

            val intent = Intent()
            account.name = editName.text.toString()
            account.userId = editUserId.text.toString()
            account.pwd = editPassword.text.toString()
            account.link = editLink.text.toString()
            intent.putExtra(Constant.NAME_ACCOUNT, account)

            intent.putExtra(Constant.NAME_TYPE, Constant.VALUE_TYPE_DELETE)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

    }
}
