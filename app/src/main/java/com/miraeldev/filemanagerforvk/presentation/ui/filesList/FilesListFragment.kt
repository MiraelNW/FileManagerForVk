package com.miraeldev.filemanagerforvk.presentation.ui.filesList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.miraeldev.filemanagerforvk.FileManagerForVkApp
import com.miraeldev.filemanagerforvk.R
import com.miraeldev.filemanagerforvk.databinding.FilesListFragmentBinding
import com.miraeldev.filemanagerforvk.presentation.fileListAdapter.FilesListAdapter
import com.miraeldev.filemanagerforvk.presentation.ui.changedFilesList.ChangedFilesListFragment
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
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this, viewModelFactory)[FileListViewModel::class.java]

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(CHANGES_FILES)
        val subMenuFileAscending = menu.addSubMenu(SORT_ASCENDING)
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_SIZE,
            Menu.NONE,
            BY_FILE_SIZE
        )
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_CREATION_DATE,
            Menu.NONE,
            BY_FILE_CREATION_DATE
        )
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_MIME_TYPE,
            Menu.NONE,
            BY_FILE_MIME_TYPE
        )
        subMenuFileAscending.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_NAME,
            Menu.NONE,
            BY_FILE_NAME
        )
        val subMenuFileDescending = menu.addSubMenu(SORT_DESCENDING)
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_SIZE,
            Menu.NONE,
            BY_FILE_SIZE
        )
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_CREATION_DATE,
            Menu.NONE,
            BY_FILE_CREATION_DATE
        )
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_MIME_TYPE,
            Menu.NONE,
            BY_FILE_MIME_TYPE
        )
        subMenuFileDescending.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_NAME,
            Menu.NONE,
            BY_FILE_NAME
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title?.equals(CHANGES_FILES) == true) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, ChangedFilesListFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        val sortType = when (item.itemId) {
            SORT_ASCENDING_FILE_SIZE -> 1
            SORT_ASCENDING_FILE_CREATION_DATE -> 2
            SORT_ASCENDING_FILE_MIME_TYPE -> 3
            SORT_ASCENDING_FILE_NAME -> 4
            SORT_DESCENDING_FILE_SIZE -> 5
            SORT_DESCENDING_FILE_CREATION_DATE -> 6
            SORT_DESCENDING_FILE_MIME_TYPE -> 7
            else -> 8
        }
        viewModel.sortList(sortType)
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
        private const val SORT_DESCENDING = "Отсортировать по убыванию"
        private const val SORT_ASCENDING = "Отсортировать по возрастанию"
        private const val CHANGES_FILES = "Измененные файлы"
        private const val BY_FILE_SIZE = "По размеру файла"
        private const val BY_FILE_CREATION_DATE = "По дате создания файла"
        private const val BY_FILE_MIME_TYPE = "По расширению файла"
        private const val BY_FILE_NAME = "По имени файла"
        private const val SHARE = "Share"
        private const val PROVIDER = ".provider"
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