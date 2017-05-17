/*
 * Copyright 2000-2016 JetBrains s.r.o.
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
package se.chalmers.dat261.model;

import com.intellij.JavaTestUtil;
import com.intellij.codeInsight.completion.Lab4Adapter;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * This models the case when a user can write nested if-statements with only the 'true' condition, with the only other statement being
 * assignments to the variable a;
 */
public class Lab4Model implements FsmModel {
  private Window window = Window.Editor;
  private Caret caret = Caret.Nothing;
  private Lab4Adapter adapter;

  public Lab4Model() throws Exception {
    adapter = new Lab4Adapter();
  }

  private static File getDataFile(String name) {
    return new File(JavaTestUtil.getJavaTestDataPath() + name);
  }

  @Action
  public void caretToMethod() {
    String actual = adapter.caretToMethod();
    String expected = "foo";
    caret = Caret.Method;
    window = Window.Editor;
    assertEquals(expected, actual);
  }

  public boolean caretToMethodGuard() {
    return true;
  }

  @Action
  public void caretToClass() {
    String actual = adapter.caretToClass();
    String expected = "File";
    caret = Caret.Class;
    window = Window.Editor;
    assertEquals(expected, actual);
  }

  public boolean caretToClassGuard() {
    return true;
  }

  @Action
  public void caretToTag() {
    String actual = adapter.caretToTag();
    String expected = "Override";
    caret = Caret.Tag;
    window = Window.Editor;
    assertEquals(expected, actual);
  }

  public boolean caretToTagGuard() {
    return true;
  }

  @Action
  public void caretToVariable() {
    String actual = adapter.caretToVariable();
    String expected = "file";
    caret = Caret.Variable;
    window = Window.Editor;
    assertEquals(expected, actual);
  }

  public boolean caretToVariableGuard() {
    return true;
  }

  @Action
  public void caretToJavaDoc() {
    String actual = adapter.caretToJavaDoc();
    String expected = "License";
    caret = Caret.JavaDoc;
    window = Window.Editor;
    assertEquals(expected, actual);
  }

  public boolean caretToJavaDocGuard() {
    return true;
  }

  @Action
  public void caretToNothing() {
    String actual = adapter.caretToNothing();
    String expected = "";
    caret = Caret.Nothing;
    window = Window.Editor;
    assertEquals(expected, actual);
  }

  public boolean caretToNothingGuard() {
    return true;
  }

  @Action
  public void showDefinition() {
    String actual = adapter.showDefinition();
    String expected = "";
    try {
      expected = StringUtil.convertLineSeparators(FileUtil.loadFile(getDataFile("/model-based/" + caret.text + "_definition.txt")));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    window = Window.Definition;
    assertEquals(expected, actual);
  }

  public boolean showDefinitionGuard() {
    return window == Window.Editor && caret != Caret.Nothing && caret != Caret.Tag;
  }

  @Action
  public void closeDefinition() {
    window = Window.Editor;
  }

  public boolean closeDefinitionGuard() {
    return window == Window.Definition;
  }

  @Action
  public void showDocumentation() {
    String actual = adapter.showDocumentation();
    String expected = "";
    try {
      expected = StringUtil.convertLineSeparators(FileUtil.loadFile(getDataFile("/model-based/" + caret.text + "_documentation.html")));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    window = Window.Documentation;
    assertEquals(expected, actual);
  }

  public boolean showDocumentationGuard() {
    return window == Window.Editor && caret != Caret.Nothing;
  }

  @Action
  public void closeDocumentation() {
    window = Window.Editor;
  }

  public boolean closeDocumentationGuard() {
    return window == Window.Documentation;
  }

  @Override
  public Object getState() {
    return window;
  }

  @Override
  public void reset(boolean b) {
    caret = Caret.Nothing;
    window = Window.Editor;
  }

  public void cleanup() {
    try {
      adapter.cleanup();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private enum Window {Definition, Documentation, Editor}

  private enum Caret {
    Method("method"), Class("class"), Tag("tag"), Variable("variable"), JavaDoc("javadoc"), Nothing("nothing");
    private String text;

    Caret(String text) {
      this.text = text;
    }
  }
}
