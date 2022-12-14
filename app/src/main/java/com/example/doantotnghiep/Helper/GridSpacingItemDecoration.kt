package com.example.doantotnghiep.Helper

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView




class GridSpacingItemDecoration : RecyclerView.ItemDecoration {
     var spanCount :Int = 0  ;
     var spacing : Int = 0 ;
     var includeEdge : Boolean = false
     constructor(spanCount : Int , spacing : Int , includeEdge : Boolean) {
         this.spanCount = spanCount
         this.spacing   = spacing
         this.includeEdge = includeEdge
     }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view) // item position
        Log.d("[RECYCLER_POS]" ,position.toString())

        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
            Log.d( "getItemOffsets: ", "left = ${ outRect.left} right = ${ outRect.right}")
            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        }else{
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            Log.d( "getItemOffsets: ", "left = ${ outRect.left} right = ${ outRect.right}")
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}