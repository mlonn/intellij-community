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

import com.intellij.codeInsight.completion.Lab4Adapter;
import com.intellij.codeInsight.completion.NextPrevParameterAction;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

import static junit.framework.TestCase.assertEquals;

/**
 * This models the case when a user can write nested if-statements with only the 'true' condition, with the only other statement being
 * assignments to the variable a;
 */
public class Lab4Model implements FsmModel {
  private Window window = Window.Editor;
  private Caret caret = Caret.Nothing;
  private Caret currentDocPage = Caret.Nothing;
  private int nbrFiles = 0;
  private int currentFile = -1;
  private Lab4Adapter adapter;

  private enum Window {Definition, Documentation, Editor}

  ;

  private enum Caret {Method, Class, Tag, Variable, JavaDoc, Nothing}

  public Lab4Model() throws Exception {
    adapter = new Lab4Adapter();
  }

  @Action
  public void caretToMethod() {
    adapter.caretToMethod();
    caret = Caret.Method;
    window = Window.Editor;
  }

  public boolean caretToMethodGuard() {
    return true;
  }

  @Action
  public void caretToClass() {
    adapter.caretToClass();
    caret = Caret.Class;
    window = Window.Editor;
  }

  public boolean caretToClassGuard() {
    return true;
  }

  @Action
  public void caretToTag() {
    adapter.caretToTag();
    caret = Caret.Tag;
    window = Window.Editor;
  }

  public boolean caretToTagGuard() {
    return true;
  }

  @Action
  public void caretToVariable() {
    adapter.caretToVariable();
    caret = Caret.Variable;
    window = Window.Editor;
  }

  public boolean caretToVariableGuard() {
    return true;
  }

  @Action
  public void caretToJavaDoc() {
    adapter.caretToJavaDoc();
    caret = Caret.JavaDoc;
    window = Window.Editor;
  }

  public boolean caretToJavaDocGuard() {
    return true;
  }

  @Action
  public void caretToNothing() {
    adapter.caretToNothing();
    caret = Caret.Nothing;
    window = Window.Editor;
  }

  public boolean caretToNothingGuard() {
    return true;
  }

  @Action
  public void showDefinition() {
    nbrFiles = adapter.showDefinition();
    window = Window.Definition;
  }

  public boolean showDefinitionGuard() {
    return window == Window.Editor && caret != Caret.Nothing;
  }

  @Action
  public void closeDefinition() {
    adapter.closeDefinition();
    window = Window.Editor;
  }

  public boolean closeDefinitionGuard() {
    return window == Window.Definition;
  }

  @Action
  public void selectFromDropList() {
    currentFile = adapter.selectFromDropList();
  }

  public boolean selectFromDropListGuard() {
    return window == Window.Definition && nbrFiles > 1;
  }

  @Action
  public void nextFile() {
    currentFile++;
    adapter.getCurrentFile(currentFile);
  }

  public boolean nextFileGuard() {
    return window == Window.Definition && nbrFiles > 1 && currentFile < nbrFiles - 1;
  }

  @Action
  public void previousFile() {
    currentFile--;
    adapter.getCurrentFile(currentFile);
  }

  public boolean previousFileGuard() {
    return window == Window.Definition && currentFile > 0;
  }

  @Action

  public void showDocumentation() {
    adapter.showDocumentation();
    window = Window.Documentation;
    currentDocPage = caret;
  }

  public boolean showDocumentationGuard() {
    return window == Window.Editor && caret != Caret.Nothing;
  }

  @Action
  public void closeDocumentation() {
    adapter.closeDocumentation();
    currentDocPage = Caret.Nothing;
    window = Window.Editor;
  }

  public boolean closeDocumentationGuard() {
    return window == Window.Documentation;
  }

  @Action
  public void editSource() {
    adapter.editSouce();
    caret = currentDocPage;
    currentDocPage = Caret.Nothing;
  }

  public boolean editSourceGuard() {
    return window == Window.Documentation;
  }

  @Action
  public void linkToMethod() {
    currentDocPage = Caret.Method;
  }

  public boolean linkToMethodGuard() {
    return window == Window.Documentation;
  }

  @Action
  public void linkToClass() {
    currentDocPage = Caret.Class;
  }

  public boolean linkToClassGuard() {
    return window == Window.Documentation;
  }

  @Action
  public void linkToTag() {
    currentDocPage = Caret.Tag;
  }

  public boolean linkToTagGuard() {
    return window == Window.Documentation;
  }

  @Action
  public void linkToVariable() {
    currentDocPage = Caret.Variable;
  }

  public boolean linkToMethodVariable() {
    return window == Window.Documentation;
  }

  @Override
  public Object getState() {
    return window;
  }

  @Override
  public void reset(boolean b) {
    window = Window.Editor;
    caret = Caret.Nothing;
  }

  public void cleanup() throws Exception {
    adapter.cleanup();
  }
}
