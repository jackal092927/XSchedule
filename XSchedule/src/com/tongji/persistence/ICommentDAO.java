package com.tongji.persistence;

import java.util.List;

import com.tongji.persistence.models.CommentModel;

public interface ICommentDAO {

	public void store(CommentModel commentModel);
	public void delete(CommentModel courseModel);
	public CommentModel getById(int id );
	public List<CommentModel> getAll();
}
