package com.example.onlineexamportal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentBook : Fragment() {
    private lateinit var bookViewModel: BookViewModel
    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_student_book, container, false)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        bookViewModel.fetchBooks("android")
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        bookViewModel.books.observe(viewLifecycleOwner, Observer { books ->
            bookAdapter = BookAdapter(books) { book ->
                // Handle item click (for example, show book details)
                Toast.makeText(requireContext(), "Selected: ${book.volumeInfo.title}", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = bookAdapter
        })

        view.findViewById<Button>(R.id.buttonAndroid).setOnClickListener {
            bookViewModel.setCategory("android")
            bookAdapter.notifyDataSetChanged()
        }
        view.findViewById<Button>(R.id.buttonNetworking).setOnClickListener {
            bookViewModel.setCategory("networking")
            bookAdapter.notifyDataSetChanged()
        }
        view.findViewById<Button>(R.id.buttonDBMS).setOnClickListener {
            bookViewModel.setCategory("dbms")
            bookAdapter.notifyDataSetChanged()
        }
        view.findViewById<Button>(R.id.buttonDataStructure).setOnClickListener {
            bookViewModel.setCategory("data+structure")
            bookAdapter.notifyDataSetChanged()
        }
        view.findViewById<Button>(R.id.buttonMathematics).setOnClickListener {
            bookViewModel.setCategory("mathematics")
            bookAdapter.notifyDataSetChanged()
        }

        return view
    }
}