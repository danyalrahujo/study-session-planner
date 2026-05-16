package com.example.studyplanner.repository;

import com.example.studyplanner.model.Tag;
import java.util.ArrayList;

import java.util.List;

public class InMemoryTagRepository implements TagRepository {

	private List<Tag> tags = new ArrayList<>();

	@Override
	public void save(Tag tag) {
		tags.add(tag);
	}

	@Override
	public List<Tag> findAll() {
		return tags;
	}

	@Override
	public Tag findById(String id) {
		return null;
	}

	@Override
	public void update(Tag tag) {

	}

	@Override
	public void delete(String id) {

	}
}