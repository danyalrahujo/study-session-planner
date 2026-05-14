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

	@Test
	public void testGetAllTags() {

		when(tagRepository.findAll())
				.thenReturn(java.util.Arrays.asList(new Tag("1", "Programming"), new Tag("2", "Java")));

		java.util.List<Tag> tags = tagController.getAllTags();

		org.junit.Assert.assertEquals(2, tags.size());
	}

	@Test
	public void testDeleteTag() {

		tagController.deleteTag("1");

		verify(tagRepository).delete("1");
	}

	@Test
	public void testUpdateTag() {

		Tag tag = new Tag("1", "Updated Tag");

		tagController.updateTag(tag);

		verify(tagRepository).update(tag);
	}

	@Test
	public void testFindTagById() {

		Tag tag = new Tag("1", "Programming");

		when(tagRepository.findById("1")).thenReturn(tag);

		Tag result = tagController.findTagById("1");

		org.junit.Assert.assertNotNull(result);

		org.junit.Assert.assertEquals("Programming", result.getName());
	}
}