package com.example.studyplanner.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TagSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override
	protected void onSetUp() {

		TagSwingView view = GuiActionRunner.execute(() -> new TagSwingView());

		window = new FrameFixture(robot(), view);

		window.show();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.label(JLabelMatcher.withText("Tag Name"));

		window.textBox("tagNameTextBox").requireEnabled();

		window.button(JButtonMatcher.withText("Add Tag")).requireDisabled();

		window.list("tagList");

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();

		window.label("errorMessageLabel").requireText("");
	}
}
