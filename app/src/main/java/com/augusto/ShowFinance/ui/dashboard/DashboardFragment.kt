package com.jailton.androidapptemplate.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ccom.augusto.ShowFinance.R
import com.augusto.ShowFinance.baseclasses.Item
import com.augusto.ShowFinance.databinding.FragmentDashboardBinding
import java.util.UUID
import com.augusto.ShowFinance.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private lateinit var enderecoEditText: EditText
    private lateinit var itemImageView: ImageView
    private var imageUri: Uri? = null


    //TODO("Declare aqui as outras variaveis do tipo EditText que foram inseridas no layout")
    private lateinit var salvarButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        itemImageView = view.findViewById(R.id.image_item)
        salvarButton = view.findViewById(R.id.salvarItemButton)
        selectImageButton = view.findViewById(R.id.button_select_image)
        enderecoEditText = view.findViewById(R.id.enderecoItemEditText)
        //TODO("Capture aqui os outro campos que foram inseridos no layout. Por exemplo, ate
        // o momento so foi capturado o endereco (EditText)")

        auth = FirebaseAuth.getInstance()

        try {
            storageReference = FirebaseStorage.getInstance().reference.child("itens_images")
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Erro ao obter referência para o Firebase Storage", e)
            // Trate o erro conforme necessario, por exemplo:
            Toast.makeText(context, "Erro ao acessar o Firebase Storage", Toast.LENGTH_SHORT).show()
        }

        selectImageButton.setOnClickListener {
            openFileChooser()
        }

        salvarButton.setOnClickListener {
            salvarItem()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}