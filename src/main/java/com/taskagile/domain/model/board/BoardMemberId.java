package com.taskagile.domain.model.board;

import com.taskagile.domain.model.user.UserId;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class BoardMemberId implements Serializable {

    private static final long serialVersionUID = -5739169913659318896L;

    @Column(name = "board_id")
    private long boardId;

    @Column(name = "user_id")
    private long userId;

    public BoardMemberId() {
    }

    public BoardMemberId(BoardId boardId, UserId userId) {
        this.boardId = boardId.value();
        this.userId = userId.value();
    }

    public BoardId getBoardId() {
        return new BoardId(boardId);
    }

    public UserId getUserId() {
        return new UserId(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardMemberId)) return false;
        BoardMemberId that = (BoardMemberId) o;
        return boardId == that.boardId &&
            userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, userId);
    }

    @Override
    public String toString() {
        return "BoardMemberId{" +
            "boardId=" + boardId +
            ", userId=" + userId +
            '}';
    }
}
