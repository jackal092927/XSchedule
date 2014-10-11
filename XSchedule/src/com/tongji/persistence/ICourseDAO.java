package com.tongji.persistence;

import java.util.List;

import com.tongji.persistence.models.CourseModel;

public interface ICourseDAO {

	public void store(CourseModel courseModel);
	public void delete(CourseModel courseModel);
	public CourseModel getById(int id );
	public List<CourseModel> getAll();
	public void deleteById(int id);
}
