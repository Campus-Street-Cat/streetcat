package com.example.streetcat.activity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.streetcat.R
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity(){

    internal lateinit var btn_switch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        btn_switch.setOnClickListener {
            if(btn_switch.isChecked)
            {
                Toast.makeText(this@AlarmActivity, "알람을 켭니다.",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this@AlarmActivity, "알람을 끕니다.",Toast.LENGTH_SHORT).show()
            }
        }

        //contextmenu 등록
        registerForContextMenu(tv_alarmmenu)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
            super.onCreateContextMenu(menu, v, menuInfo)
            //길게 누른 view의 id로 분기
            when(v?.id)
            {
                R.id.tv_alarmmenu->{
                    menu?.setHeaderTitle("메뉴")
                    menuInflater.inflate(R.menu.menu1,menu)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        //메뉴의 id값으로 분기

        when(item.itemId)
        {
            R.id.text_item1 ->{
                //메뉴1로 가는 코드
            }
            R.id.text_item2 ->{
                //메뉴2로 가는 코드
            }
        }
        return super.onContextItemSelected(item)
    }
}