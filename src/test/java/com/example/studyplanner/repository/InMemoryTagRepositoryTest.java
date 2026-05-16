package com.example.studyplanner.repository;

import com.example.studyplanner.model.Tag;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InMemoryTagRepositoryTest {

	private InMemoryTagRepository inMemoryTagRepository;

	@Before
	public void setUp() {

		inMemoryTagRepository = new InMemoryTagRepository();
	}

	@Test
	public void testSave() {

		Tag tag = new Tag("1", "Programming");

		inMemoryTagRepository.save(tag);

		List<Tag> result = inMemoryTagRepository.findAll();

		assertEquals(1, result.size());
	}

	@Test
	public void testFindById() {

		Tag tag = new Tag("1", "Programming");

		inMemoryTagRepository.save(tag);

		Tag result = inMemoryTagRepository.findById("1");

		assertEquals("Programming", result.getName());
	}

	@Test
	public void testUpdate() {

		Tag tag = new Tag("1", "Programming");

		inMemoryTagRepository.save(tag);

		Tag updatedTag = new Tag("1", "Java");

		inMemoryTagRepository.update(updatedTag);

		Tag result = inMemoryTagRepository.findById("1");

		assertEquals("Java", result.getName());
	}

	@Test
	public void testDelete() {

		Tag tag = new Tag("1", "Programming");

		inMemoryTagRepository.save(tag);

		inMemoryTagRepository.delete("1");

		List<Tag> result = inMemoryTagRepository.findAll();

		assertEquals(0, result.size());
	}
}