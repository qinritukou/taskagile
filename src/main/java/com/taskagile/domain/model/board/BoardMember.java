package com.taskagile.domain.model.board;

import com.taskagile.domain.model.user.UserId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "board_member")
public class BoardMember {

    private static final long serialVersionUID = 1101935717986500672L;

    @EmbeddedId
    private BoardMemberId id;

    public static BoardMember create(BoardId boardId, UserId userId) {
        BoardMember boardMember = new BoardMember();
        boardMember.id = new BoardMemberId(boardId, userId);
        return boardMember;
    }

    public BoardId getBoardId() {
        return id.getBoardId();
    }

    public UserId getUserId() {
        return id.getUserId();
    }
}
