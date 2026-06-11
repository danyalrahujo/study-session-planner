package com.example.studyplanner.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

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
	private FrameFixture tagWindow;
	private String databaseName;

	@Override
	protected void onSetUp() {
		databaseName = "studyPlannerE2E_" + System.nanoTime();

		application("com.example.studyplanner.app.swing.StudyPlannerAppSwing")
				.withArgs("--mongoHost=localhost", "--mongoPort=27017", "--databaseName=" + databaseName).start();

		studyPlannerWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Study Planner View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

		tagWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Tag View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

		focusStudyPlannerWindow();
	}

	private void focusStudyPlannerWindow() {
		studyPlannerWindow.show();
		robot().waitForIdle();
	}

	private void focusTagWindow() {
		tagWindow.show();
		robot().waitForIdle();
	}

	@Override
	protected void onTearDown() {
		if (tagWindow != null) {
			tagWindow.cleanUp();
		}
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
		focusStudyPlannerWindow();

		studyPlannerWindow.textBox("idTextBox").click();
		studyPlannerWindow.textBox("idTextBox").enterText("100");
		studyPlannerWindow.textBox("descriptionTextBox").click();
		studyPlannerWindow.textBox("descriptionTextBox").enterText("Study Spring");
		studyPlannerWindow.button("addSessionButton").click();

		assertThat(studyPlannerWindow.list("sessionList").contents()).anyMatch(item -> item.contains("Study Spring"));
	}

	@Test
	@GUITest
	public void testAddTagSuccess() {
		focusTagWindow();

		tagWindow.textBox("tagNameTextBox").enterText("Java");
		tagWindow.button("addTagButton").click();

		await().atMost(2, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(tagWindow.list("tagList").contents()).anyMatch(e -> e.contains("Java")));
	}

	@Test
	@GUITest
	public void testUpdateSessionSuccess() {
		focusStudyPlannerWindow();

		studyPlannerWindow.textBox("idTextBox").click();
		studyPlannerWindow.textBox("idTextBox").enterText("1");
		studyPlannerWindow.textBox("descriptionTextBox").click();
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
		focusStudyPlannerWindow();

		studyPlannerWindow.textBox("idTextBox").click();
		studyPlannerWindow.textBox("idTextBox").enterText("1");
		studyPlannerWindow.textBox("descriptionTextBox").click();
		studyPlannerWindow.textBox("descriptionTextBox").enterText("Math");
		studyPlannerWindow.button("addSessionButton").click();
		studyPlannerWindow.list("sessionList").selectItem(0);

		robot().waitForIdle();

		studyPlannerWindow.button("deleteSessionButton").requireEnabled();
		studyPlannerWindow.button("deleteSessionButton").click();

		assertThat(studyPlannerWindow.list("sessionList").contents()).noneMatch(e -> e.contains("Math"));
	}

	@Test
	@GUITest
	public void testUpdateTagSuccess() {
		focusTagWindow();

		tagWindow.textBox("tagNameTextBox").enterText("Java");
		tagWindow.button("addTagButton").click();
		assertThat(tagWindow.list("tagList").contents()).anyMatch(e -> e.contains("Java"));

		tagWindow.list("tagList").selectItem(0);

		await().atMost(5, TimeUnit.SECONDS)

				.untilAsserted(() ->

				tagWindow.button("updateTagButton").requireEnabled());
		tagWindow.textBox("tagNameTextBox").setText("Spring");
		tagWindow.button("updateTagButton").click();

		assertThat(tagWindow.list("tagList").contents()).anyMatch(e -> e.contains("Spring"));
	}

	@Test
	@GUITest
	public void testDeleteTagSuccess() {

		tagWindow.textBox("tagNameTextBox").enterText("Java");
		tagWindow.button("addTagButton").click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(tagWindow.list("tagList").contents()).anyMatch(e -> e.contains("Java")));

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(tagWindow.list("tagList").contents().length).isGreaterThan(0));

		tagWindow.list("tagList").selectItem(0);

		robot().waitForIdle();

		tagWindow.button("deleteTagButton").requireEnabled();

		tagWindow.button("deleteTagButton").click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(tagWindow.list("tagList").contents()).noneMatch(e -> e.contains("Java")));
	}

}