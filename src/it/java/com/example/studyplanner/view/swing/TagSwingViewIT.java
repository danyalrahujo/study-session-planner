package com.example.studyplanner.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TagSwingViewIT extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override
	protected void onSetUp() {

		TagSwingView view = GuiActionRunner.execute(() -> new TagSwingView());

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

		window.button("addTagButton").requireDisabled();
	}

	@Test
	@GUITest
	public void testAddButtonEnabledWhenTextEntered() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button("addTagButton").requireEnabled();
	}
}