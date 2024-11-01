package com.youssefmahmoud.pubdevsearcher

import com.beust.klaxon.Json

data class GetPackagesResponseModel(val packages: List<PackageModel>, val next: String?)

data class PackageModel(
    @Json(name = "package")
    val name: String
)