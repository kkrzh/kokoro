package com.mobile.hotel.base

/**
 * Description:
 */
abstract class MiniBaseAdapter<T>(private val layoutRes: Int,currentDataList:List<T>): BaseAdapter() {
    var currentDataList:List<T> = arrayListOf()
    init {
        this.currentDataList = currentDataList
    }

    override fun setItemCount(): Int = currentDataList.size
    override fun setItemLayout(viewType: Int): Int = layoutRes

}