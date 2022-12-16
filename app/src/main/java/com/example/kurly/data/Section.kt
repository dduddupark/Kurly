package com.example.kurly.data

import com.google.gson.annotations.SerializedName

/**
 * Kurly
 * Class: SectionInfo
 * Created by bluepark on 2022/12/12.
 *
 * Description:
 */
enum class SectionType constructor(val type: String) {
    Vertical("vertical"), Horizontal("horizontal"), Grid("grid")
}

data class SectionInfo(
    @SerializedName("data")
    val sectionList: List<Section>? = null,
    @SerializedName("paging")
    val page: Page? = null,
)

data class Section(
    val title: String? = "",
    val id: Int? = 0,
    val type: String? = "",
    val url: String? = "",
    var products: List<Product>? = null
)

data class Page(
    @SerializedName("next_page")
    val nextPage: Int? = 0
)

