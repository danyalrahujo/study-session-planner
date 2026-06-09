package com.example.studyplanner.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import com.example.studyplanner.model.Tag;

public class TagSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private TagSwingView tagSwingView;

	@Override
	protected void onSetUp() {
		tagSwingView = GuiActionRunner.execute(() -> new TagSwingView());

		window = new FrameFixture(robot(), tagSwingView);

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

	@Test
	public void testWhenTagNameIsNonEmptyThenAddTagButtonShouldBeEnabled() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button(JButtonMatcher.withText("Add Tag")).requireEnabled();
	}

	@Test
	public void testWhenTagNameIsBlankThenAddTagButtonShouldBeDisabled() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button(JButtonMatcher.withText("Add Tag")).requireEnabled();

		window.textBox("tagNameTextBox").setText("");

		window.textBox("tagNameTextBox").enterText(" ");

		window.button(JButtonMatcher.withText("Add Tag")).requireDisabled();
	}

	@Test
	public void testDeleteTagButtonShouldBeEnabledOnlyWhenATagIsSelected() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.getListTagsModel().addElement(tag));

		window.list("tagList").selectItem(0);

		window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();

		window.list("tagList").clearSelection();

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
	}
}
