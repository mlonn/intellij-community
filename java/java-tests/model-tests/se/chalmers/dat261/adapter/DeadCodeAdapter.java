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

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.daemon.impl.quickfix.SimplifyBooleanExpressionFix;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class DeadCodeAdapter extends BaseAdapter {
  public DeadCodeAdapter() throws Exception {
    super("/model-based/DeadCode.java");
  }

  public void addDeadCode() {
    type("if(b != 42){System.out.println(\"Unreachable statement\");}\n");
  }

  public void removeDeadCode() {
    up();
    right();
    right();
    right();

    final SimplifyBooleanExpressionFix fix = createFix();

    if (fix == null) {
      return;
    }

    CommandProcessor.getInstance().executeCommand(getProject(), () -> {
      PsiDocumentManager.getInstance(getProject()).commitAllDocuments();

      WriteAction.run(fix::applyFix);
      }, "Don't really care (remove if condition)", null);
  }

  private static SimplifyBooleanExpressionFix createFix() {
    PsiElement element = myFile.findElementAt(myEditor.getCaretModel().getOffset()).getParent();

    if (!(element instanceof PsiExpression)) return null;
    if (PsiTreeUtil.findChildOfType(element, PsiAssignmentExpression.class) != null) return null;

    final PsiExpression expression = (PsiExpression)element;
    while (element.getParent() instanceof PsiExpression) {
      element = element.getParent();
    }
    final SimplifyBooleanExpressionFix fix = new SimplifyBooleanExpressionFix((PsiExpression)element, false);
    if (!fix.isAvailable()) {
      return null;
    }

    return fix;
  }

  @Override
  public String getName() {
    return "testDeadCode";
  }

  public void cleanup() throws Exception {
    tearDown(); // Otherwise IntelliJ will throw an exception regarding memory leakage, which will preven tests from running green.
  }
}
