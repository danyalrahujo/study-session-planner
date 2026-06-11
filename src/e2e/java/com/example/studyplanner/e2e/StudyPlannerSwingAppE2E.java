package com.example.studyplanner.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class StudyPlannerSwingAppE2E extends AssertJSwingJUnitTestCase {

	private FrameFixture studyPlannerWindow;

	@Override
	protected void onSetUp() {
		application("com.example.studyplanner.app.swing.StudyPlannerAppSwing")
				.withArgs("--mongoHost=localhost", "--mongoPort=27017").start();

		studyPlannerWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Study Planner View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	@Override
	protected void onTearDown() {
		if (studyPlannerWindow != null) {
			studyPlannerWindow.cleanUp();
		}
	}

	@Test
	@GUITest
	public void testApplicationStarts() {

		assertThat(studyPlannerWindow.target().isVisible()).isTrue();
	}

	@Test
	@GUITest
	public void testAddSessionSuccess() {

		studyPlannerWindow.textBox("idTextBox").enterText("100");
		studyPlannerWindow.textBox("descriptionTextBox").enterText("Study Spring");

		studyPlannerWindow.button("addSessionButton").click();

		assertThat(studyPlannerWindow.list("sessionList").contents()).anyMatch(item -> item.contains("Study Spring"));
	}

	@Test
	@GUITest
	public void testAddTagSuccess() {

		FrameFixture tagWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Tag View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		tagWindow.textBox("tagNameTextBox").enterText("Java");
		tagWindow.button("addTagButton").click();
		assertThat(tagWindow.list("tagList").contents()).anyMatch(item -> item.contains("Java"));
		tagWindow.cleanUp();
	}

	@Test
	@GUITest
	public void testUpdateSessionSuccess() {

		studyPlannerWindow.textBox("idTextBox").enterText("1");
		studyPlannerWindow.textBox("descriptionTextBox").enterText("Math");
		studyPlannerWindow.button("addSessionButton").click();
		studyPlannerWindow.list("sessionList").selectItem(0);
		studyPlannerWindow.textBox("descriptionTextBox").setText("Advanced Math");
		studyPlannerWindow.button("updateSessionButton").click();
		assertThat(studyPlannerWindow.list("sessionList").contents()).anyMatch(e -> e.contains("Advanced Math"));
	}

	@Test
	@GUITest
	public void testDeleteSessionSuccess() {

		studyPlannerWindow.textBox("idTextBox").enterText("1");
		studyPlannerWindow.textBox("descriptionTextBox").enterText("Math");
		studyPlannerWindow.button("addSessionButton").click();
		studyPlannerWindow.list("sessionList").selectItem(0);
		studyPlannerWindow.button("deleteSessionButton").click();
		assertThat(studyPlannerWindow.list("sessionList").contents()).noneMatch(e -> e.contains("Math"));
	}

	@Test
	@GUITest
	public void testUpdateTagSuccess() {

		FrameFixture tagWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Tag View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		tagWindow.textBox("tagNameTextBox").enterText("Java");
		tagWindow.button("addTagButton").click();
		assertThat(tagWindow.list("tagList").contents()).anyMatch(e -> e.contains("Java"));
		tagWindow.list("tagList").selectItem(0);
		assertThat(tagWindow.button("updateTagButton").target().isEnabled()).isTrue();
		tagWindow.textBox("tagNameTextBox").setText("Spring");
		tagWindow.button("updateTagButton").click();
		assertThat(tagWindow.list("tagList").contents()).anyMatch(e -> e.contains("Spring"));
	}

	@Test
	@GUITest
	public void testDeleteTagSuccess() {

		FrameFixture tagWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {

			@Override
			protected boolean isMatching(JFrame frame) {
				return "Tag View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		tagWindow.textBox("tagNameTextBox").enterText("Java");
		tagWindow.button("addTagButton").click();
		tagWindow.list("tagList").selectItem(0);
		tagWindow.button("deleteTagButton").click();
		assertThat(tagWindow.list("tagList").contents()).noneMatch(e -> e.contains("Java"));
	}
}