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

		for (Tag tag : tags) {

			if (tag.getId().equals(id)) {
				return tag;
			}
		}

		return null;
	}

	@Override
	public void update(Tag updatedTag) {

		for (int i = 0; i < tags.size(); i++) {

			Tag currentTag = tags.get(i);

			if (currentTag.getId().equals(updatedTag.getId())) {

				tags.set(i, updatedTag);
			}
		}
	}

	@Override
	public void delete(String id) {

		tags.removeIf(tag -> tag.getId().equals(id));
	}
}