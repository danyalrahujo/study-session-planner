package com.example.studyplanner.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class StudyPlannerSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override

	protected void onSetUp() {

		StudyPlannerSwingView view =

				GuiActionRunner.execute(() -> new StudyPlannerSwingView());

		window = new FrameFixture(robot(), view);

		window.show();

	}

	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.label(JLabelMatcher.withText("id"));

		window.textBox("idTextBox").requireEnabled();

		window.label(JLabelMatcher.withText("description"));

		window.textBox("descriptionTextBox").requireEnabled();

		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();

		window.list("sessionList");

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();

		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenIdAndDescriptionAreNonEmptyThenAddButtonShouldBeEnabled() {

		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText("Math");

		window.button(JButtonMatcher.withText("Add Session")).requireEnabled();
	}
}