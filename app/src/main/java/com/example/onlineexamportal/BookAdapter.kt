package com.example.onlineexamportal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    private val books: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // ViewHolder to represent the layout for each item
    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val imageView: ImageView = itemView.findViewById(R.id.bookImageView)

        // Bind the data to the views
        fun bind(book: Book) {
            titleTextView.text = book.volumeInfo.title
            authorTextView.text = book.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author"

            // Load the book image using Glide or Picasso (you can add dependencies)
            Glide.with(itemView.context)
                .load(book.volumeInfo.imageLinks?.smallThumbnail)
                .error(R.drawable.img)
                .into(imageView)

            // Handle item click
            itemView.setOnClickListener {
                onItemClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    // Function to update the list of books when data changes
    fun updateBooks(newBooks: List<Book>) {
        (books as MutableList).clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}
