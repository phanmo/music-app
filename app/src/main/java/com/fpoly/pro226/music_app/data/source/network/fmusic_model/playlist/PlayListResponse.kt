package com.fpoly.pro226.music_app.data.source.network.fmusic_model.playlist

data class PlayListResponse(
    val `data`: List<MyPlaylist>,
    val message: String,
    val status: Int
)

fun PlayListResponse.updateCountPlaylist(count: Int, id: String) : PlayListResponse{
    val updatedData = this.data.map { myPlaylist ->
        if (myPlaylist._id == id) {
            myPlaylist.count = count
        }
        myPlaylist
    }
    val res = this.copy(data = updatedData)
    return res
}