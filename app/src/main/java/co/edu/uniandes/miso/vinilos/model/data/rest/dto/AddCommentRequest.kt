package co.edu.uniandes.miso.vinilos.model.data.rest.dto

data class CommentCollector(val id: Int, val name: String = "")

data class AddCommentRequest (val description: String, val rating: Int, val collector: CommentCollector)