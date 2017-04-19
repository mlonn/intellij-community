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
package se.chalmers.dat261.adapter;

import com.intellij.codeInsight.daemon.LightIntentionActionTestCase;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Caret;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class BaseAdapter extends LightIntentionActionTestCase {
  public BaseAdapter(String testDocument) throws Exception {
    super();
    super.setUp();
    configureByFile(testDocument);
  }

  protected BaseAdapter() {

  }

  protected static String lineUnderCaret() {
    caret().selectLineAtCaret();
    String text = caret().getSelectedText();
    caret().removeSelection();
    return text;
  }

  protected static String wordUnderCaret() {
    caret().selectWordAtCaret(false);

    return caret().getSelectedText();
  }

  protected static void invokeAction(String actionId) {
    final AnAction action = ActionManager.getInstance().getAction(actionId);
    assertNotNull("Can find registered action with id=" + actionId, action);
    invokeAction(action);
  }

  protected static void invokeAction(AnAction action) {
    action.actionPerformed(AnActionEvent.createFromAnAction(action, null, "", DataManager.getInstance().getDataContext()));
  }

  @NotNull
  private static Caret caret() {
    return myEditor.getCaretModel().getCurrentCaret();
  }

  protected static String blockAroundCaret() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(lineUnderCaret());

    int i = 0;
    int depth = 1;
    String line = "";
    while(depth > 0){
      up();
      line = lineUnderCaret();
      stringBuilder.insert(0, line);

      depth += countMatches(Pattern.compile("}"), line);
      depth -= countMatches(Pattern.compile("\\{"), line);
      ++i;
    }

    while(i>0){
      down();
      --i;
    }

    while(!line.contains("}")){
      down();
      line = lineUnderCaret();
      stringBuilder.append(line);
      ++i;
    }
    while(i>0){
      up();
      --i;
    }

    return stringBuilder.toString();
  }

  public void cleanup() throws Exception {
    tearDown(); // Otherwise IntelliJ will throw an exception regarding memory leakage, which will prevent tests from running green.
  }

  static int countMatches(Pattern pattern, String string)
  {
    Matcher matcher = pattern.matcher(string);

    int count = 0;
    int pos = 0;
    while (matcher.find(pos))
    {
      count++;
      pos = matcher.start() + 1;
    }

    return count;
  }
}
