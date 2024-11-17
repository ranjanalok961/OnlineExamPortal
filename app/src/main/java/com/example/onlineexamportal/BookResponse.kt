package com.example.onlineexamportal

// BookResponse.kt
data class BookResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<Book>
)

// Book.kt
data class Book(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo
)

// VolumeInfo.kt
data class VolumeInfo(
    val title: String,
    val subtitle: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val industryIdentifiers: List<IndustryIdentifier>?,
    val readingModes: ReadingModes?,
    val pageCount: Int?,
    val printType: String?,
    val categories: List<String>?,
    val maturityRating: String?,
    val allowAnonLogging: Boolean,
    val contentVersion: String?,
    val panelizationSummary: PanelizationSummary?,
    val imageLinks: ImageLinks?,
    val language: String?,
    val previewLink: String?,
    val infoLink: String?,
    val canonicalVolumeLink: String?
)

// IndustryIdentifier.kt
data class IndustryIdentifier(
    val type: String,
    val identifier: String
)

// ReadingModes.kt
data class ReadingModes(
    val text: Boolean,
    val image: Boolean
)

// PanelizationSummary.kt
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
)

// ImageLinks.kt
data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
)

// SaleInfo.kt
data class SaleInfo(
    val country: String?,
    val saleability: String?,
    val isEbook: Boolean
)

// AccessInfo.kt
data class AccessInfo(
    val country: String?,
    val viewability: String?,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: String?,
    val epub: Epub?,
    val pdf: Pdf?,
    val webReaderLink: String?,
    val accessViewStatus: String?,
    val quoteSharingAllowed: Boolean
)

// Epub.kt
data class Epub(
    val isAvailable: Boolean
)

// Pdf.kt
data class Pdf(
    val isAvailable: Boolean
)

// SearchInfo.kt
data class SearchInfo(
    val textSnippet: String?
)

