package dev.wilsonjr.newsapp.components

import android.content.Context
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import dev.wilsonjr.newsapp.api.model.enums.BaseDataEnum

class CustomAutoCompleteTextView : AutoCompleteTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun convertSelectionToString(selectedItem: Any?): CharSequence {
        if (selectedItem is BaseDataEnum) {
            return context.getString(selectedItem.getRes())
        }
        return super.convertSelectionToString(selectedItem)
    }

}