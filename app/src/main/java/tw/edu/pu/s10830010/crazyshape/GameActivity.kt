package tw.edu.pu.s10830010.crazyshape

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import org.tensorflow.lite.support.image.TensorImage
import tw.edu.pu.s10830010.crazyshape.ml.Shapes
class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        btnBack.setEnabled(false)

        val intent = getIntent()
        val number:Int = intent.getIntExtra("number",0)
        val shapes:String? = intent.getStringExtra("shapes")

        when(number){
            0 -> txvMsg.text="畫圓形"
            1 -> txvMsg.text="畫方形"
            2 -> txvMsg.text="畫三角形"
            3 -> txvMsg.text="畫星星"
        }

        btnBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                finish()
            }
        })
//。。。
        btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                handv.path.reset()
                handv.invalidate()
            }
        })
        fun classifyDrawing(bitmap: Bitmap) {
            val model = Shapes.newInstance(this)

            // Creates inputs for reference.
            val image = TensorImage.fromBitmap(bitmap)

            // Runs model inference and gets result.
            //val outputs = model.process(image)
            //val probability = outputs.probabilityAsCategoryList

            val outputs = model.process(image)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score } // 排序，高匹配率優先
                }.take(1)  //取最高的1個
            var Result: String = ""
            var FlagDraw: Int = 0
            when (outputs[0].label) {
                "circle" -> {
                    Result = "圓形"
                    FlagDraw = 1
                }
                "square" -> {
                    Result = "方形"
                    FlagDraw = 2
                }
                "star" -> {
                    Result = "星形"
                    FlagDraw = 3
                }
                "triangle" -> {
                    Result = "三角形"
                    FlagDraw = 4
                }
            }

//
            //
            ////

            // Releases model resources if no longer used.
            model.close()
            if(outputs[0].label!=shapes)
                Toast.makeText(this, "答錯了", Toast.LENGTH_SHORT).show()

            else if(outputs[0].label==shapes)
                btnBack.setEnabled(true)
                Toast.makeText(this, "答對了", Toast.LENGTH_SHORT).show()
        }
        handv.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent): Boolean {
                var xPos = event.getX()
                var yPos = event.getY()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> handv.path.moveTo(xPos, yPos)
                    MotionEvent.ACTION_MOVE -> handv.path.lineTo(xPos, yPos)
                    MotionEvent.ACTION_UP -> {
                        //將handv轉成Bitmap
                        val b = Bitmap.createBitmap(
                            handv.measuredWidth, handv.measuredHeight,
                            Bitmap.Config.ARGB_8888
                        )
                        val c = Canvas(b)
                        handv.draw(c)
                        classifyDrawing(b)
                    }
                }
                handv.invalidate()
                return true
            }
        })
    }



    }



