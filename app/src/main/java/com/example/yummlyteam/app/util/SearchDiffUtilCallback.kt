package com.example.yummlyteam.app.util

import android.support.v7.util.DiffUtil
import com.example.yummlyteam.app.model.Match

class SearchDiffUtilCallback(
        private val oldMatchResults: List<Match>,
        private val newMatchResults: List<Match>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMatchResults[oldItemPosition].id == newMatchResults[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldMatchResults.size

    override fun getNewListSize(): Int = newMatchResults.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMatchResults[oldItemPosition].ingredients.size ==
                newMatchResults[newItemPosition].ingredients.size
    }
}