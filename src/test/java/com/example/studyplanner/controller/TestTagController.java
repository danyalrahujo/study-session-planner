package com.example.studyplanner.controller;

import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.TagRepository;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestTagController {

	private TagController tagController;
	private TagRepository tagRepository;

	@Before
	public void setUp() {
		tagRepository = mock(TagRepository.class);
		tagController = new TagController(tagRepository);
	}

	@Test
	public void testAddTag() {
		Tag tag = new Tag("1", "Programming");

		tagController.addTag(tag);

		verify(tagRepository).save(tag);
	}
}