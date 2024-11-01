package com.youssefmahmoud.pubdevsearcher

import com.beust.klaxon.Klaxon
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class HttpHelper {
    companion object {
        fun getPackages(search: String): GetPackagesResponseModel? {
            var searchStr = search
            if (search != "") {
                while (searchStr.contains("  ")) {
                    searchStr = searchStr.replace("  ", " ")
                }
                searchStr = search.replace(" ", "+")
                searchStr = "q=$searchStr"
            }
            val url = "https://pub.dev/api/search?$searchStr"
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .build()

            val response: Response = client.newCall(request).execute()
            val body = response.body?.string()

            if (body != null) {
                val json = Klaxon().parse<GetPackagesResponseModel>(body)
                if (json != null) {
                    return json
                }
            }
            return null
        }

        fun getPackagesWithUrl(url: String): GetPackagesResponseModel? {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .build()

            val response: Response = client.newCall(request).execute()
            val body = response.body?.string()

            if (body != null) {
                val json = Klaxon().parse<GetPackagesResponseModel>(body)
                if (json != null) {
                    return json
                }
            }
            return null

        }
    }
}