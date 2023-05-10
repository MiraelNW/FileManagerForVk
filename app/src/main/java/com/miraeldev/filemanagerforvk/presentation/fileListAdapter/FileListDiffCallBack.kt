package com.miraeldev.filemanagerforvk.presentation.fileListAdapter

import androidx.recyclerview.widget.DiffUtil
import com.miraeldev.filemanagerforvk.domain.model.FileModel

object FileListDiffCallBack : DiffUtil.ItemCallback<FileModel>() {
    override fun areItemsTheSame(oldItem: FileModel, newItem: FileModel): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun areContentsTheSame(oldItem: FileModel, newItem: FileModel): Boolean {
        return oldItem == newItem
    }

}