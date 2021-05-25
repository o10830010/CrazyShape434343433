package tw.edu.pu.s10830010.crazyshape

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import kotlinx.android.synthetic.main.activity_main.*

@GlideModule
public final class MyAppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generates()
        val img: ImageView = findViewById(R.id.imageTitle)
        GlideApp.with(this)
            .load(R.drawable.cover)
            .override(800,600)
            .into(img)

        Toast.makeText(baseContext, "作者：馬文婷", Toast.LENGTH_LONG).show()
//
        imgNext.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                startActivity(intent)
                return true
            }
        })

        imgNext.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                generates()
            }

        })
    }

    fun generates(){
        val i:Int = (0..3).random()
        val img2: ImageView = findViewById(R.id.imgNext)
        when(i){
            0 -> GlideApp.with(this)
                .load(R.drawable.circle)
                .override(800,600)
                .into(img2)

            1 -> GlideApp.with(this)
                .load(R.drawable.square)
                .override(800,600)
                .into(img2)

            2 -> GlideApp.with(this)
                .load(R.drawable.triangle)
                .override(800,600)
                .into(img2)

            3 -> GlideApp.with(this)
                .load(R.drawable.star)
                .override(800,600)
                .into(img2)
        }
        intent = Intent(this@MainActivity, GameActivity::class.java).apply {
            putExtra("number",i)
            when(i){
                0 -> putExtra("shapes","circle")
                1 -> putExtra("shapes","square")
                2 -> putExtra("shapes","triangle")
                3 -> putExtra("shapes","star")
            }
        }
    }
}