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
}