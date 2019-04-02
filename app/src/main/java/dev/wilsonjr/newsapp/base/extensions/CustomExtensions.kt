import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<ArrayList<T>>.addListValues(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.postValue(value)
}

fun <T> MutableLiveData<ArrayList<T>>.clear() {
    this.postValue(null)
}