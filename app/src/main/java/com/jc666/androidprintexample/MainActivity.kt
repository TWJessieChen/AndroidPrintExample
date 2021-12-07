package com.jc666.androidprintexample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.File

class MainActivity : AppCompatActivity() {

    private var btn_open_print_cover: Button? = null

    private var btn_open_print_other: Button? = null

    private var printManager: PrintManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printManager = getSystemService(PRINT_SERVICE) as PrintManager

        btn_open_print_cover = findViewById(R.id.btn_open_print_cover)
        btn_open_print_other = findViewById(R.id.btn_open_print_other)

        btn_open_print_cover!!.setOnClickListener {
            printCover()
        }

        btn_open_print_other!!.setOnClickListener {
            printOther()
        }

    }

    fun printCover() {
//        val inputStream: InputStream = assets.open("cover.pdf")
        val filePath =  getFileFromAssets(this@MainActivity, "cover.pdf").absolutePath
        val printJob = print(
            "Cover PDF",
            PdfDocumentAdapter(applicationContext, filePath),
            PrintAttributes.Builder().build()
        )
    }

    fun printOther() {
//        val inputStream: InputStream = assets.open("cover.pdf")
        val filePath =  getFileFromAssets(this@MainActivity, "Coroutine.pdf").absolutePath
        val printJob = print(
            "Coroutine PDF",
            PdfDocumentAdapter(applicationContext, filePath),
            PrintAttributes.Builder().build()
        )
    }

    fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
        .also {
            if (!it.exists()) {
                it.outputStream().use { cache ->
                    context.assets.open(fileName).use { inputStream ->
                        inputStream.copyTo(cache)
                    }
                }
            }
        }

    private fun print(
        name: String, adapter: PrintDocumentAdapter,
        attrs: PrintAttributes
    ): PrintJob? {
        startService(Intent(this, PrintJobMonitorService::class.java))
        return printManager!!.print(name, adapter, attrs)
    }

}