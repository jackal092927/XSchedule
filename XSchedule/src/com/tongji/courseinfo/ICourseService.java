package com.tongji.courseinfo;

import com.tongji.persistence.models.CourseModel;

public interface ICourseService {

	public void store(CourseModel courseModel);

	public void delete(CourseModel courseModel);
	
	public void deleteById(int id);
	
	public CourseModel getById(int id);
	
	
}
