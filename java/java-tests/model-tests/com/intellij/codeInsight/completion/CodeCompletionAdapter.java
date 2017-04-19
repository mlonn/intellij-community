/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight.completion;

import com.intellij.openapi.actionSystem.IdeActions;
import se.chalmers.dat261.adapter.BaseAdapter;

@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class CodeCompletionAdapter extends BaseAdapter {
  public CodeCompletionAdapter() throws Exception {
    super("/model-based/CodeCompletion.java");
  }

  /**
   * Expected outcome 'if()<caret>'
   */
  public void addEmptyIfHead() {
    // <student code>
    type("if()");
    // </student code>
  }

  /**
   * Expected outcome '$stmt\n<caret>'
   */
  public void addStatement(String stmt) {
    // <student code>
    type(stmt + "\n");
    // </student code>
  }

  /**
   * Expected outcome 'if(true){<caret>}'
   */
  public void completeIfHead() {
    // <student code>
    completeStatement();
    // </student code>

    addIfCondition();

    // <student code>
    completeStatement();
    // </student code>
  }

  /**
   * Given
   *  if(...) {
   *    ...
   *    <caret>
   *  }
   * Expected outcome is
   *  if(...) {
   *    ...
   *
   *  }
   *  <caret>
   */
  public void exitCurrentBlock() {
    // <student code>
    down();
    type("\n");
    // </student code>
  }

  private static void completeStatement() {
    // It's easiest to see what happens in differnet situation from the below line if you just try it out in the editor (e.g. make a line
    // under this and start typing), you can look up what hotkey you have for the "complete statement" action in the keymap in the settings.
    // For me the hotkey is set to <Shift+Ctrl+Enter>.
    // 1.
    // if()<Shift+Ctrl+Enter>
    // 2.
    // if(true<Shift+Ctrl+Enter>) {
    // }
    // 3.
    // if(true) {
    //   int a = 3;
    // }

    // <student code>
    invokeAction(IdeActions.ACTION_EDITOR_COMPLETE_STATEMENT);
    // </student code>
  }

  /**
   * Should check that the current code block contains exactly $expected, ignoring whitespace (newline, tab, spaces)
   */
  public void assertCurrentCodeBlockEqualsIgnoringSpaces(String expected) {
    // <student code>
    String actual = blockAroundCaret();
    assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", ""));
    // </student code>
  }

  /**
   * This is just intended as an extra how-to, to help you construct the adapter
   */
  private void demoOfTheEditorActions() {
    type("if()\n");

    up();
    String actualLine = lineUnderCaret();
    down();

    String expectedLine = "    if()\n";
    assertEquals(expectedLine, actualLine);

    // You can use the debugger and set a breakpoint here, then use the Evaluate Expression <Shift+Alt+8> feature to try out carret movement
    // interactively, this is very helpful when designing your adapter!
    up();
    left();
    left();
    String actualWord = wordUnderCaret();
    down();

    String expectedWord = "if";
    assertEquals(expectedWord, actualWord);

    // One can also invoke editor actions as
    invokeAction(IdeActions.ACTION_EDITOR_MOVE_CARET_UP); // Have a look at the IdeActions enum for more actions
    down();
  }

  private void addIfCondition() {
    type("true");
  }

  @Override
  public String getName() {
    return "testCodeCompletion";
  }
}