package com.example.studyplanner.view.swing;

import static org.assertj.swing.edt.GuiActionRunner.execute;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class StudyPlannerSwingViewIT extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override
	protected void onSetUp() {

		StudyPlannerSwingView view = execute(() -> new StudyPlannerSwingView());

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
	}

	@Test
	@GUITest
	public void testAddButtonInitiallyDisabled() {

		window.button("addSessionButton").requireDisabled();
	}

	@Test
	@GUITest
	public void testAddButtonEnabledWhenFieldsFilled() {

		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText("Study TDD");

		window.button("addSessionButton").requireEnabled();
	}
}