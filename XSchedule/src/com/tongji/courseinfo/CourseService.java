package com.tongji.courseinfo;

import java.io.Serializable;

import javax.faces.bean.ManagedProperty;

import com.tongji.persistence.ICourseDAO;
import com.tongji.persistence.models.CourseModel;


public class CourseService implements ICourseService, Serializable {
	@ManagedProperty(value="#{courseDAO}")
	private ICourseDAO courseDAO;

	@Override
	public void store(CourseModel courseModel) {
		getCourseDAO().store(courseModel);
	}

	@Override
	public void delete(CourseModel courseModel) {
		getCourseDAO().delete(courseModel);
	}

	@Override
	public void deleteById(int id) {
		getCourseDAO().deleteById(id);
	}

	@Override
	public CourseModel getById(int id) {
		return getCourseDAO().getById(id);
	}

	public ICourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(ICourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}
}
