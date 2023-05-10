package com.miraeldev.filemanagerforvk.presentation.fileListAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.miraeldev.filemanagerforvk.R
import com.miraeldev.filemanagerforvk.databinding.FileItemBinding
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import java.text.SimpleDateFormat
import java.util.*


class FilesListAdapter : ListAdapter<FileModel, FileViewHolder>(FileListDiffCallBack) {

    var onDirectoryClickListener: OnDirectoryClickListener? = null
    var onFileClickListener: OnFileClickListener? = null
    var onShareFileClickListener: OnShareFileClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = getItem(position)
        with(holder.binding) {

            if (file.isDirectory) {
                fileImage.setImageResource(R.drawable.ic_folder)
            } else {
                when (file.type) {
                    "pdf" -> fileImage.setImageResource(R.drawable.ic_pdf)
                    "png" -> fileImage.setImageResource(R.drawable.ic_png)
                    "docx" -> fileImage.setImageResource(R.drawable.ic_docx)
                    "jpg" -> fileImage.setImageResource(R.drawable.ic_jpg)
                    "jpeg" -> fileImage.setImageResource(R.drawable.ic_jpg)
                    "mp3" -> fileImage.setImageResource(R.drawable.ic_mp3)
                    "mp4" -> fileImage.setImageResource(R.drawable.ic_mp4)
                    "pptx" -> fileImage.setImageResource(R.drawable.ic_pptx)
                    "txt" -> fileImage.setImageResource(R.drawable.ic_txt)
                    else -> fileImage.setImageResource(R.drawable.ic_file)
                }
            }
            fileName.text = file.name
            fileSize.text = file.formatSize

            fileCreationDate.text = millisToDate(file.dateInMillis)

        }
        holder.itemView.setOnClickListener {
            if (file.isDirectory) {
                onDirectoryClickListener?.onDirectoryClick(file.absolutePath)
            } else {
                onFileClickListener?.onFileClick(file.absolutePath)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (!file.isDirectory) {

                onShareFileClickListener?.onShareFileClick(file.absolutePath,it)
            }
            true
        }
    }


    private fun millisToDate(timeInMillis: Long): String {
        val format = SimpleDateFormat("dd.MM.yyyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        calendar.timeZone = TimeZone.getDefault()
        return format.format(calendar.getTime())
    }

    interface OnDirectoryClickListener {
        fun onDirectoryClick(path: String)
    }

    interface OnFileClickListener {
        fun onFileClick(path: String)
    }

    interface OnShareFileClickListener {
        fun onShareFileClick(path: String,view:View)
    }
}