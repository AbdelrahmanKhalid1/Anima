package com.ak.otaku_kun.utils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentManager
import com.ak.otaku_kun.ui.details.media.MediaActivity

class ItemClickHandler(
    private val context: Context,
    private var _clickBehavior: ItemClickBehavior? = null,
    private var _longClickBehavior: ItemLongClickBehavior? = null
) {
    fun setClickBehavior(clickBehavior: ItemClickBehavior) {
        _clickBehavior = clickBehavior
    }

    fun setLongClickBehavior(longClickBehavior: ItemLongClickBehavior) {
        _longClickBehavior = longClickBehavior
    }

    fun onItemClick(itemId: Int, view: View) {
        _clickBehavior?.onItemClick(itemId, view, context)
    }

    fun onItemLongClick(itemId: Int, view: View) {
        _longClickBehavior?.onItemLongClick(itemId, view, context)
    }
}

interface ItemClickBehavior {
    fun onItemClick(itemId: Int, view: View, context: Context)
}

interface ItemLongClickBehavior {
    fun onItemLongClick(itemId: Int, view: View, context: Context)
}

class OnMediaClick(private val fm: FragmentManager? = null) : ItemClickBehavior, ItemLongClickBehavior {
    override fun onItemClick(itemId: Int, view: View, context: Context) {
        val intent = Intent(context, MediaActivity()::class.java)
        intent.putExtra(Keys.KEY_ITEM_ID, itemId)
        context.startActivity(intent)
    }

    override fun onItemLongClick(itemId: Int, view: View, context: Context) {
//        val filterQueryDialog =
//            FilterQueryDialog(viewModel.getQueryFilters(), this)
//        filterQueryDialog.show(fm, "FilterQueryDialog")
    }
}

class OnCharacterClick : ItemClickBehavior {
    override fun onItemClick(itemId: Int, view: View, context: Context) {
        val intent = Intent(context, MediaActivity()::class.java)
        intent.putExtra(Keys.KEY_ITEM_ID, itemId)
        context.startActivity(intent)
    }
}

class OnStudioClick : ItemClickBehavior {
    override fun onItemClick(itemId: Int, view: View, context: Context) {
        val intent = Intent(context, MediaActivity()::class.java)
        intent.putExtra(Keys.KEY_ITEM_ID, itemId)
        context.startActivity(intent)
    }
}

class OnStaffClick : ItemClickBehavior {
    override fun onItemClick(itemId: Int, view: View, context: Context) {
        val intent = Intent(context, MediaActivity()::class.java)
        intent.putExtra(Keys.KEY_ITEM_ID, itemId)
        context.startActivity(intent)
    }
}

//class OnCharacterClick() : ItemClickBehavior{
//    override fun onItemClick(itemId: Int, view: View, context: Context) {
//        val intent = Intent(context, MediaActivity()::class.java)
//        intent.putExtra(Const.KEY_ITEM_ID, itemId)
//        context.startActivity(intent)
//    }
//}