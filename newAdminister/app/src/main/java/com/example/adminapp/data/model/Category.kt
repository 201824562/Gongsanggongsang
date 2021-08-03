package com.example.adminapp.data.model

import com.example.adminapp.R
import com.example.adminapp.data.entity.CategoryEntity


data class CategoryItem(val type : RecyclerDataType, val data : CategoryData?){
    fun makeCategoryEntity(token : String) : CategoryEntity { return CategoryEntity(data!!.drawableID, data.name, token) }
}

data class CategoryData(val drawableID: Int, val name: String, var clicked : Boolean = false)

data class CategoryResourceItem(val drawableID: Int, var clicked: Boolean)

enum class CategoryResources(val drawableID: Int){
    MENTORING_ROOM(R.drawable.ic_mentoringroom), COMPUTER(R.drawable.ic_computer), OFFICE(R.drawable.ic_office),
    MEETING_ROOM(R.drawable.ic_meeting_room), CAFETERIA(R.drawable.ic_cafeteria), ROUNGE(R.drawable.ic_rounge),
    SHOWER_ROOM(R.drawable.ic_shower_room), WASHER(R.drawable.ic_washer), DRYER(R.drawable.ic_dryer), INDUCTION(R.drawable.ic_induction);

    companion object{
        private val categoryResourcesIdList : List<Int> = listOf(MENTORING_ROOM.drawableID, COMPUTER.drawableID, OFFICE.drawableID,
        MEETING_ROOM.drawableID, CAFETERIA.drawableID, ROUNGE.drawableID, SHOWER_ROOM.drawableID, WASHER.drawableID,
        DRYER.drawableID, INDUCTION.drawableID)

        fun makeListToClass() : List<CategoryResourceItem> {
            val rvList : MutableList<CategoryResourceItem> = mutableListOf()
            categoryResourcesIdList.forEach { rvList.add(CategoryResourceItem(it, false)) }
            return rvList
        }
    }
}