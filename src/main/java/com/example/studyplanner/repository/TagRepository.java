package com.example.studyplanner.repository;

import com.example.studyplanner.model.Tag;
import java.util.List;

public interface TagRepository {

	void save(Tag tag);

	List<Tag> findAll();

	Tag findById(String id);

	void update(Tag tag);

	void delete(String id);
}