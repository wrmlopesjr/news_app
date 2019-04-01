package dev.wilsonjr.newsapp.api.model.enums

import dev.wilsonjr.newsapp.R

enum class Category(private val resId: Int) : BaseDataEnum {

    ALL(R.string.ALL_CATEGORIES), BUSINESS(R.string.BUSINESS), ENTERTAINMENT(R.string.ENTERTAINMENT), GENERAL(R.string.GENERAL), HEALTH(R.string.HEALTH), SCIENCE(R.string.SCIENCE), SPORTS(R.string.SPORTS), TECHNOLOGY(R.string.TECHNOLOGY);

    override fun getRes(): Int {
        return resId
    }
}