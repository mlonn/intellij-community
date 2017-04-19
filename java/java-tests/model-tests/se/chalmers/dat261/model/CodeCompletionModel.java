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

import com.intellij.codeInsight.completion.CodeCompletionAdapter;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * This models the case when a user can write nested if-statements with only the 'true' condition, with the only other statement being
 * assignments to the variable a;
 */
public class CodeCompletionModel implements FsmModel {
  private State state = State.CodeBlock;
  private CodeCompletionAdapter adapter;
  private final static String STATEMENT = "a = 3;";
  private Stack<String> contentAtLevel = new Stack<>();
  private String content = "";


  public CodeCompletionModel() throws Exception {
    adapter = new CodeCompletionAdapter();
  }

  private enum State {CodeBlock, IfHead}

  ;

  @Override
  public Object getState() {
    return state;
  }

  @Override
  public void reset(boolean b) {
    if (state == State.IfHead) {
      adapter.completeIfHead();
    }

    contentAtLevel = new Stack<>();
    content = "";
    state = State.CodeBlock;
  }

  public boolean addIfStatementGuard() {
    return state == State.CodeBlock;
  }

  public boolean addStatementGuard() {
    return state == State.CodeBlock;
  }

  public boolean addIfHeadGuard() {
    return state == State.IfHead;
  }

  public boolean exitCurrentBlockGuard() {
    return contentAtLevel.size() > 0 && state == State.CodeBlock;
  }

  @Action
  public void addIfStatement() {
    adapter.addEmptyIfHead();
    state = State.IfHead;

    contentAtLevel.add(content);
    content = "";
  }

  @Action
  public void addIfHead() {
    adapter.completeIfHead();
    state = State.CodeBlock;

    String expected = "if(true){}";
    adapter.assertCurrentCodeBlockEqualsIgnoringSpaces(expected);
  }

  @Action
  public void addStatement() {
    adapter.addStatement(STATEMENT);
    content += STATEMENT;

    String expected = wrapInIf(content);
    adapter.assertCurrentCodeBlockEqualsIgnoringSpaces(expected);
  }

  @Action
  public void exitCurrentBlock() {
    adapter.exitCurrentBlock();
    String tmp = content;
    content = contentAtLevel.pop();
    content += wrapInIf(tmp);

    String expected = wrapInIf(this.content);
    adapter.assertCurrentCodeBlockEqualsIgnoringSpaces(expected);
  }

  @NotNull
  private String wrapInIf(String content) {
    return "if(true){" + content + "}";
  }


  public void cleanup() throws Exception {
    adapter.cleanup();
  }
}
