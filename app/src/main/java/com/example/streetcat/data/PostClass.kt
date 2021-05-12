package com.example.streetcat.data

data class PostClass(val author : String, val churu : Int, val comments_cnt : Int, val contents : String, val cnt : Int) {
} // author = 글쓴이, churu = 좋아요 수, comments_cnt = 댓글 수, comments= 댓글 contents = 본문 내용, cnt = 게시글에 등록된 사진 개수