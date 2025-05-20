package co.edu.uniandes.miso.vinilos.model.data.rest.dto

data class AddCommentResponse (val id:Int, val description: String, val rating: Int, val collector: CommentCollector)
