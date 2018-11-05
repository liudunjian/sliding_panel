package com.hisense.hiask.widget.slidingpanel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by liudunjian on 2018/11/2.
 * Helper class for determining the current scroll positions for scrollable views. Currently works
 * for ListView, ScrollView and RecyclerView, but the library users can override it to add support
 * for other views
 */

public class ScrollableViewHelper {
    /**
     * Returns the current scroll position of the scrollable view. If this method returns zero or
     * less, it means at the scrollable view is in a position such as the panel should handle
     * scrolling. If the method returns anything above zero, then the panel will let the scrollable
     * view handle the scrolling
     *
     * @param scrollableView the scrollable view
     * @param isSlidingUp    whether or not the panel is sliding up or down
     * @return the scroll position
     */
    public int getScrollViewScrollPosition(View scrollableView, boolean isSlidingUp) {

        if (scrollableView == null) return 0;

        if (scrollableView instanceof ScrollView) {
            if (isSlidingUp) {
                return scrollableView.getScrollY();
            } else {
                View view = ((ScrollView) scrollableView).getChildAt(0);
                return scrollableView.getScrollY() + scrollableView.getHeight() - view.getMeasuredHeight();
            }
        } else if (scrollableView instanceof ListView && ((ListView) scrollableView).getChildCount() > 0) {
            ListView listView = (ListView) scrollableView;
            if (isSlidingUp) {
                View child = listView.getChildAt(0);
                return listView.getFirstVisiblePosition() * child.getHeight() - child.getTop();
            } else {
                int childCount = listView.getChildCount();
                View lastChild = listView.getChildAt(childCount - 1);
                return (listView.getAdapter().getCount() - listView.getLastVisiblePosition() - 1) * lastChild.getHeight() +
                        lastChild.getBottom() - listView.getHeight();
            }
        } else if (scrollableView instanceof RecyclerView && ((RecyclerView) scrollableView).getChildCount() > 0) {
            RecyclerView recyclerView = (RecyclerView) scrollableView;
            if (recyclerView.getAdapter() == null)
                return 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (isSlidingUp) {
                View firstChild = recyclerView.getChildAt(0);
                int childPos = recyclerView.getChildLayoutPosition(firstChild);
                int height = 0;
                for(int i = 0;i<childPos;i++) {
                    View child = layoutManager.findViewByPosition(i);
                    height += layoutManager.getDecoratedMeasuredHeight(child);
                }
                return  height - layoutManager.getDecoratedTop(firstChild);
            } else {
                View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                int height = 0;
                int childPos = recyclerView.getChildLayoutPosition(lastChild);

                for(int i = childPos+1;i<=recyclerView.getAdapter().getItemCount()-1;i++) {
                    View child = layoutManager.findViewByPosition(i);
                    height += layoutManager.getDecoratedMeasuredHeight(child);
                }

                return height + layoutManager.getDecoratedBottom(lastChild) -
                        recyclerView.getBottom();
            }
        }

        return 0;
    }
}
