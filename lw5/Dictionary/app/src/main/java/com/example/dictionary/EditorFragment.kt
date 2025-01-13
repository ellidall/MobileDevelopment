package com.example.dictionary

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentEditorBinding

class EditorFragment : Fragment(R.layout.fragment_editor) {
    private val viewModel: RecordViewModel by activityViewModels()
    private lateinit var binding: FragmentEditorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditorBinding.bind(view)

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentEditor_to_fragmentDiary)
        }

        if (arguments == null) {
            binding.buttonConfirm.setOnClickListener { onCreateButtonClick() }
        } else {
            binding.editTitle.setText(requireArguments().getCharSequence("TITLE"))
            binding.editDescription.setText(requireArguments().getCharSequence("CONTENT"))
            binding.buttonConfirm.setOnClickListener {
                updateRecord(
                    uid = requireArguments().getString("ID") as String,
                    requireArguments().getLong("DATE")
                )
            }
        }
    }

    private fun updateRecord(uid: String, createdAt: Long) {
        if (binding.editTitle.text.isEmpty()) {
            Toast.makeText(requireContext(), "Введите заголовок", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateRecord(
            uid,
            title = binding.editTitle.text.toString(),
            content = binding.editDescription.text.toString(),
            createdAt
        )

        findNavController().navigate(R.id.action_fragmentEditor_to_fragmentDiary)
    }

    private fun onCreateButtonClick() {
        if (binding.editTitle.text.isEmpty()) {
            Toast.makeText(requireContext(), "Введите заголовок", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.createRecord(
            binding.editTitle.text.toString(),
            binding.editDescription.text.toString()
        )

        findNavController().navigate(R.id.action_fragmentEditor_to_fragmentDiary)
    }
}