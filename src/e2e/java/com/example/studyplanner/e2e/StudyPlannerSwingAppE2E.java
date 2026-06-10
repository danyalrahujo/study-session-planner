package com.example.studyplanner.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

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
	public void testApplicationStarts() {

		assertThat(studyPlannerWindow.target().isVisible()).isTrue();
	}

	@Test
	public void testOnStartAllStudySessionsAreShown() {
		assertThat(studyPlannerWindow.list("sessionList").contents()).contains("StudySession[id=1, description=Math]",
				"StudySession[id=2, description=Physics]");
	}

}