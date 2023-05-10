package com.miraeldev.filemanagerforvk.presentation.ui.filesList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.miraeldev.filemanagerforvk.FileManagerForVkApp
import com.miraeldev.filemanagerforvk.R
import com.miraeldev.filemanagerforvk.databinding.FilesListFragmentBinding
import com.miraeldev.filemanagerforvk.presentation.fileListAdapter.FilesListAdapter
import com.miraeldev.filemanagerforvk.utils.FileOpener
import com.miraeldev.filemanagerforvk.utils.ViewModelFactory
import java.io.File
import javax.inject.Inject

class FilesListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FileManagerForVkApp).component
    }

    private lateinit var adapter: FilesListAdapter
    private lateinit var path: String

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FileListViewModel

    private var _binding: FilesListFragmentBinding? = null
    private val binding: FilesListFragmentBinding
        get() = _binding ?: throw RuntimeException("FilesListFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilesListFragmentBinding.inflate(inflater, container, false)

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[FileListViewModel::class.java]
        setHasOptionsMenu(true)

        adapter = FilesListAdapter()
        binding.filesRv.adapter = adapter
        path = requireArguments().getString(PATH) ?: Environment.getExternalStorageDirectory().path
        observeViewModel()
        viewModel.getList(path)
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is FileList -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(screenState.listOfFileModels)
                }
                Loading -> {
                    Log.d("list", "visible")
                    binding.progressBar.visibility = View.VISIBLE
                }
            }


            adapter.onDirectoryClickListener = object : FilesListAdapter.OnDirectoryClickListener {
                override fun onDirectoryClick(path: String) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, newInstance(path))
                        .addToBackStack(null)
                        .commit()
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
                    popupMenu.menu.add("Share")
                    popupMenu.setOnMenuItemClickListener {
                        Log.d("list", "click")
                        val file = File(path)
                        val uri = FileProvider.getUriForFile(
                            requireContext(),
                            requireContext().applicationContext.packageName + ".provider",
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val subMenuFileAscending = menu.addSubMenu("Отсоритировать по возрастанию")
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_SIZE,
            Menu.NONE,
            "По размеру файла"
        )
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_CREATION_DATE,
            Menu.NONE,
            "По дате создания"
        )
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_MIME_TYPE,
            Menu.NONE,
            "По расширению"
        )
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_NAME,
            Menu.NONE,
            "По имени файла"
        )
        val subMenuFileDescending = menu.addSubMenu("Отсоритировать по убыванию")
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_SIZE,
            Menu.NONE,
            "По размеру файла"
        )
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_CREATION_DATE,
            Menu.NONE,
            "По дате создания"
        )
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_MIME_TYPE,
            Menu.NONE,
            "По расширению"
        )
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_NAME,
            Menu.NONE,
            "По имени файла"
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            SORT_ASCENDING_FILE_SIZE -> {
                viewModel.sortList(1)
            }
            SORT_ASCENDING_FILE_CREATION_DATE -> {
                viewModel.sortList(2)
            }
            SORT_ASCENDING_FILE_MIME_TYPE -> {
                viewModel.sortList(3)
            }
            SORT_ASCENDING_FILE_NAME -> {
                viewModel.sortList(4)
            }
            SORT_DESCENDING_FILE_SIZE -> {
                viewModel.sortList(5)
            }
            SORT_DESCENDING_FILE_CREATION_DATE -> {
                viewModel.sortList(6)
            }
            SORT_DESCENDING_FILE_MIME_TYPE -> {
                viewModel.sortList(7)
            }
            SORT_DESCENDING_FILE_NAME -> {
                viewModel.sortList(8)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val SORT_ASCENDING_FILE_SIZE = 201
        private const val SORT_ASCENDING_FILE_CREATION_DATE = 202
        private const val SORT_ASCENDING_FILE_MIME_TYPE = 203
        private const val SORT_ASCENDING_FILE_NAME = 204
        private const val SORT_DESCENDING_FILE_SIZE = 301
        private const val SORT_DESCENDING_FILE_CREATION_DATE = 302
        private const val SORT_DESCENDING_FILE_MIME_TYPE = 303
        private const val SORT_DESCENDING_FILE_NAME = 304
        const val PATH = "path"

        fun newInstance(path: String): FilesListFragment {
            return FilesListFragment().apply {
                arguments = Bundle().apply {
                    putString(PATH, path)
                }
            }
        }
    }

}