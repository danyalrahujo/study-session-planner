package com.example.studyplanner.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.edt.GuiActionRunner.execute;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class StudyPlannerSwingViewIT extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override
	protected void onSetUp() {

		StudyPlannerSwingView view = execute(StudyPlannerSwingView::new);

		window = new FrameFixture(robot(), view);

		window.show();
	}

	@Override
	protected void onTearDown() {

		if (window != null) {
			window.cleanUp();
		}
	}

	@Test
	@GUITest
	public void testWindowIsVisible() {
		window.requireVisible();
		assertThat(window.target().isVisible()).isTrue();
	}

	@Test
	@GUITest
	public void testAddButtonInitiallyDisabled() {
		window.button("addSessionButton").requireDisabled();
		assertThat(window.button("addSessionButton").target().isEnabled()).isFalse();
	}

	@Test
	@GUITest
	public void testAddButtonEnabledWhenFieldsFilled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText("Study TDD");

		window.button("addSessionButton").requireEnabled();
		assertThat(window.button("addSessionButton").target().isEnabled()).isTrue();
	}
}