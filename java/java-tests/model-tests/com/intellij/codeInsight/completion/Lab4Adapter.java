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

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.codeInsight.hint.ImplementationViewComponent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Caret;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import org.jetbrains.annotations.Nullable;
import se.chalmers.dat261.adapter.BaseAdapter;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Mikae on 2017-05-03.
 */
public class Lab4Adapter extends BaseAdapter {
  protected JavaCodeInsightTestFixture myFixture;
  Caret caret;
  private DocumentationManager myDocumentationManager;

  public Lab4Adapter() throws Exception {
    super("/model-based/Lab4.java");
    caret = getEditor().getCaretModel().getCurrentCaret();
  }

  public void cleanup() throws Exception {
    //tearDown(); // Otherwise IntelliJ will throw an exception regarding memory leakage, which will prevent tests from running green.
  }

  public String caretToMethod() {
    caret.moveToOffset(688);
    return wordUnderCaret();
  }

  public String caretToClass() {
    caret.moveToOffset(740);
    return wordUnderCaret();
  }

  public String caretToTag() {
    caret.moveToOffset(668);
    return wordUnderCaret();
  }

  public String caretToVariable() {
    caret.moveToOffset(745);
    return wordUnderCaret();
  }

  public String caretToJavaDoc() {
    caret.moveToOffset(216);
    return wordUnderCaret();
  }

  public String caretToNothing() {
    caret.moveToOffset(660);
    return "";
  }

  public String showDefinition() {

    myDocumentationManager = DocumentationManager.getInstance(getProject());

    PsiElement elem = myDocumentationManager.findTargetElement(getEditor(), getFile());
    final String s = ImplementationViewComponent.getNewText(elem);
    return s;
  }

  @Nullable
  private String getDocFromWindow(PsiElement elem, PsiElement oelem) {
    final CountDownLatch latch = new CountDownLatch(1);
    final String[] s = new String[1];
    Thread t = new Thread("name") {
      @Override
      public void run() {
        super.run();
        try {
          s[0] = myDocumentationManager.getDefaultCollector(elem, oelem).getDocumentation();
          latch.countDown();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    t.start();
    try {
      latch.await();
      return s[0];
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String showDocumentation() {

    invokeAction(IdeActions.ACTION_QUICK_JAVADOC);

    myDocumentationManager = DocumentationManager.getInstance(getProject());

    PsiElement elem = myDocumentationManager.findTargetElement(getEditor(), getFile());
    PsiElement oelem = myFile.getOriginalElement();
    return getDocFromWindow(elem, oelem);
  }
}
