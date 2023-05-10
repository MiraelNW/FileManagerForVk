package com.miraeldev.filemanagerforvk.presentation.ui.changedFilesList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.miraeldev.filemanagerforvk.FileManagerForVkApp
import com.miraeldev.filemanagerforvk.databinding.ChangedFilesListFragmentBinding
import com.miraeldev.filemanagerforvk.presentation.fileListAdapter.FilesListAdapter
import com.miraeldev.filemanagerforvk.presentation.ui.filesList.FileList
import com.miraeldev.filemanagerforvk.presentation.ui.filesList.FileListViewModel
import com.miraeldev.filemanagerforvk.presentation.ui.filesList.Loading
import com.miraeldev.filemanagerforvk.utils.FileOpener
import com.miraeldev.filemanagerforvk.utils.ViewModelFactory
import java.io.File
import javax.inject.Inject

class ChangedFilesListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FileManagerForVkApp).component
    }

    private lateinit var adapter: FilesListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ChangedFilesListViewModel

    private var _binding: ChangedFilesListFragmentBinding? = null
    private val binding: ChangedFilesListFragmentBinding
        get() = _binding ?: throw RuntimeException("ChangedFilesListFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangedFilesListFragmentBinding.inflate(inflater, container, false)

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this, viewModelFactory)[ChangedFilesListViewModel::class.java]

        adapter = FilesListAdapter()
        binding.filesRv.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        binding.alert.visibility = View.VISIBLE
        viewModel.changedFilesListUseCase.invoke().observe(viewLifecycleOwner) {
            binding.alert.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            Log.d("main", it.toString())
            if (it == null || it.isEmpty()) {
                binding.filesRv.visibility = View.GONE
                binding.noFiles.visibility = View.VISIBLE
            } else {
                binding.noFiles.visibility = View.GONE
                binding.filesRv.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }

        adapter.onFileClickListener = object : FilesListAdapter.OnFileClickListener {
            override fun onFileClick(path: String) {
                val file = File(path)
                FileOpener.openFile(requireContext(), file)
            }
        }
        adapter.onShareFileClickListener = object : FilesListAdapter.OnShareFileClickListener {
            override fun onShareFileClick(path: String, view: View) {
                val popupMenu = PopupMenu(requireContext(), view)
                popupMenu.menu.add(SHARE)
                popupMenu.setOnMenuItemClickListener {
                    val file = File(path)
                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().applicationContext.packageName + PROVIDER,
                        file
                    )
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.type = "*/*"
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    requireActivity().startActivity(intent)
                    true
                }
                popupMenu.show()
            }
        }

    }


    companion object {
        private const val SHARE = "Share"
        private const val PROVIDER = ".provider"

        fun newInstance() = ChangedFilesListFragment()
    }

}