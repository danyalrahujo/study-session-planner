package com.example.studyplanner.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class TagSwingViewIT extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	@Override
	protected void onSetUp() {

		TagSwingView view = GuiActionRunner.execute(TagSwingView::new);
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

		window.button("addTagButton").requireDisabled();

		assertThat(window.button("addTagButton").target().isEnabled()).isFalse();

	}

	@Test

	@GUITest

	public void testAddButtonEnabledWhenTextEntered() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button("addTagButton").requireEnabled();

		assertThat(window.button("addTagButton").target().isEnabled()).isTrue();

	}
}