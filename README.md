android-node-webkit-interface

Android에서 Node-Webkit 기반의 게임(ex. RMMV)을 WebVeiw로 구동하기 위한 용도로 사용

사용법

WebView에서 loadUrl 이전에 작성해둡니다
```kotlin
webView.addJavascriptInterface(FileSystemInterface(context, DocumentFile.fromFile(context.getExternalFilesDir(null))), "_NJSFileSystemInterface");
```

WebViewClient.onPageFinished에서 호출(<content_of...>은 njsinterface.js의 내용을 그대로 옮기면 됩니다)
```kotlin
webView.evaluateJavascript(<content_of_js/njsinterface.js>, null);
```

혹은 assets/에 njsinterface.js를 추가하고 불러오는 방식을 추천합니다.
```kotlin
fun loadAsset(context: Context, inFile: String): String {
    return try {
        val stream: InputStream = context.assets.open(inFile)
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (stream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        result.toString("UTF-8")
    } catch (e: Exception) {
        // Handle exceptions here
        ""
    }
}
webView.evaluateJavascript(loadAsset("njsinterface.js"), null);
```
