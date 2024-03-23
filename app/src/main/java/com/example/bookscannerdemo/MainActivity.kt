package com.example.bookscannerdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookscannerdemo.ui.theme.BookScannerDemoTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookScannerDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetISBNButton(onClick = {startReader()})
                }
            }
        }
    }

    private fun startReader() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(            // 読み取るバーコードの種別を設定
                Barcode.FORMAT_ALL_FORMATS    // 今回は QR コードを読み取るよう設定
            )
            .enableAutoZoom()              // 自動ズーム有効
            .build()
        val scanner = GmsBarcodeScanning.getClient(this, options)

        // ここを実行するとバーコードスキャンの画面が起動する
        scanner.startScan()
            // 読み取りが成功した時のリスナー
            .addOnSuccessListener { barcode ->
                val rawValue: String? = barcode.rawValue
                Log.d("BarcodeTest", "成功しました : $rawValue")
                rawValue?.let {
                    Toast.makeText(this, rawValue, Toast.LENGTH_SHORT).show()
                }
            }
            // 読み取りキャンセルした時のリスナー
            .addOnCanceledListener {
                Log.d("BarcodeTest", "キャンセルしました")
            }
            // 読み取り失敗した時のリスナー
            .addOnFailureListener { exception ->
                Log.d("BarcodeTest", "失敗しました : ${exception.message}")
            }
    }
}

@Composable
private fun GetISBNButton(onClick : () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onClick) {
            Text(text = "GET ISBN CODE")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GetISBNButtonPreview() {
    BookScannerDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GetISBNButton(onClick = {/*Preview*/})
        }
    }
}