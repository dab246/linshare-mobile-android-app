package com.linagora.android.linshare.adapter.sharedspace

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import arrow.core.Either
import com.linagora.android.linshare.R
import com.linagora.android.linshare.domain.model.sharedspace.WorkGroupDocument
import com.linagora.android.linshare.domain.model.sharedspace.WorkGroupNode
import com.linagora.android.linshare.domain.usecases.sharedspace.SharedSpaceDocumentViewState
import com.linagora.android.linshare.domain.usecases.utils.Failure
import com.linagora.android.linshare.domain.usecases.utils.Success
import com.linagora.android.linshare.glide.GlideApp
import com.linagora.android.linshare.util.TimeUtils
import com.linagora.android.linshare.util.TimeUtils.LinShareTimeFormat.LastModifiedFormat
import com.linagora.android.linshare.util.getDrawableIcon

@BindingAdapter("sharedSpaceDocumentState")
fun bindingSharedSpaceDocumentList(
    recyclerView: RecyclerView,
    sharedSpaceDocumentState: Either<Failure, Success>
) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter = SharedSpaceDocumentAdapter()
    }

    sharedSpaceDocumentState.fold(
        ifLeft = { recyclerView.isVisible = false },
        ifRight = { success ->
            when (success) {
                is SharedSpaceDocumentViewState -> {
                    recyclerView.isVisible = true
                    (recyclerView.adapter as SharedSpaceDocumentAdapter).submitList(success.documents)
                }
                else -> recyclerView.isVisible = false
            }
        }
    )
}

@BindingAdapter("sharedSpaceDocumentState")
fun bindingSharedSpaceDocumentLoading(
    swipeRefreshLayout: SwipeRefreshLayout,
    sharedSpaceDocumentState: Either<Failure, Success>
) {
    sharedSpaceDocumentState.fold(
        ifLeft = { swipeRefreshLayout.isRefreshing = false },
        ifRight = { success ->
            swipeRefreshLayout.isRefreshing = success.takeIf { it is Success.Loading }
                ?.let { true }
                ?: false
        }
    )
}

@BindingAdapter("sharedSpaceDocumentLastModified")
fun bindingSharedSpaceDocumentLastModified(
    textView: TextView,
    workGroupNode: WorkGroupNode
) {
    textView.text = runCatching {
        textView.context.getString(
            R.string.last_modified,
            TimeUtils.convertToLocalTime(workGroupNode.modificationDate, LastModifiedFormat)
        )
    }.getOrNull()
}

@BindingAdapter("sharedSpaceDocumentIcon")
fun bindingSharedSpaceDocumentIcon(imageView: ImageView, workGroupNode: WorkGroupNode) {
    val drawableICon = when (workGroupNode) {
        is WorkGroupDocument -> workGroupNode.mimeType.getDrawableIcon()
        else -> R.drawable.ic_folder
    }
    GlideApp.with(imageView.context)
        .load(drawableICon)
        .into(imageView)
}
