package com.ak.otaku_kun.model.index

import com.ak.otaku_kun.model.details.Media

data class Review(
    val id: Int = -1,
    val summary: String = "",
    val createAt: String = "",
    val score: Int = 0,
    var upVotes: Int = 0,
    var downVotes: Int = 0,
    var voteStatus: ReviewVote = ReviewVote.NO_VOTE,
    val user: User,
)

enum class ReviewVote { UP_VOTE, DOWN_VOTE, NO_VOTE }