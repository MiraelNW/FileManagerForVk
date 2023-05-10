package com.miraeldev.filemanagerforvk.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.miraeldev.filemanagerforvk.R
import com.miraeldev.filemanagerforvk.databinding.FilesListFragmentBinding
import com.miraeldev.filemanagerforvk.presentation.fileListAdapter.FilesListAdapter
import com.miraeldev.filemanagerforvk.FileManagerForVkApp
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
        path = requireArguments().getString(PATH) ?: ""
        viewModel.getList(path)
        viewModel.fileListLD.observe(viewLifecycleOwner) {
            adapter.submitList(it)

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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val subMenuFile = menu.addSubMenu("Отсоритировать по возрастанию")
        subMenuFile.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_SIZE,
            Menu.NONE,
            "По размеру файла"
        )
        subMenuFile.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_CREATION_DATE,
            Menu.NONE,
            "По дате создания"
        )
        subMenuFile.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_MIME_TYPE,
            Menu.NONE,
            "По расширению"
        )
        subMenuFile.add(
            Menu.NONE,
            SORT_ASCENDING_FILE_NAME,
            Menu.NONE,
            "По имени файла"
        )
        val subMenuEdit = menu.addSubMenu("Отсоритировать по убыванию")
        subMenuEdit.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_SIZE,
            Menu.NONE,
            "По размеру файла"
        )
        subMenuEdit.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_CREATION_DATE,
            Menu.NONE,
            "По дате создания"
        )
        subMenuEdit.add(
            Menu.NONE,
            SORT_DESCENDING_FILE_MIME_TYPE,
            Menu.NONE,
            "По расширению"
        )
        subMenuFile.add(
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