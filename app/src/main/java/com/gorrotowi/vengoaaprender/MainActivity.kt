package com.gorrotowi.vengoaaprender

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.support.v7.app.AppCompatActivity
import com.gorrotowi.vengoaaprender.R.string.date_format
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var pdfFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressbar: ProgressDialog = ProgressDialog(this)
        progressbar.isIndeterminate = true
        progressbar.setMessage("creando documento")
        progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressbar.show()
        createPDF(progressbar)

        txtShow.setOnClickListener { sendEmail() }
    }

    fun createPDF(progressBar: ProgressDialog) {
        val pdfFolder = File(getExternalStorageDirectory(), "pdfDemo")
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir()
        }

        val date = Date()
        val timeStamp = SimpleDateFormat(getString(date_format)).format(date)
        pdfFile = File("$pdfFolder$timeStamp.pdf")
        val output = FileOutputStream(pdfFile)

        val document: Document = Document(PageSize.LETTER)
        PdfWriter.getInstance(document, output)

        document.open()
        document.addTitle("CV Que para obtener un puesto")
        document.add(Paragraph("Este es un gran parrafo para nuestro hermoso y lindo CV"))
        document.close()
        progressBar.dismiss()

    }

    private fun viewPDF() {
        val intent: Intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_SUBJECT, "atencionciudadanasre@sre.gob.mx")
        intent.putExtra(Intent.EXTRA_TEXT, "Vengo a Aprender")
        val uri: Uri = Uri.parse(pdfFile.absolutePath)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
//        intent.type = "application/pdf"
        intent.type = "message/rfc822"
        startActivity(intent)
    }

}
