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
package com.intellij.codeInsight;

/**
 * Created by jibbs on 2017-04-19.
 */
public class JavaExternalDocumentationExtendedTest extends JavaExternalDocumentationTest {


  public void testClassNotFound() throws Exception {
    doTest("class Foo extends  com.jetbrains.Test implements com.jetbrains.<caret>DoesNotExist {}");
  }

  public void testdoubleLinkBetweenMethods() throws Exception {
    doTest("class Foo extends  com.jetbrains.Test {{ new com.jetbrains.LinkBetweenMethods().(new com.jetbrains.LinkBetweenMethods().<caret>m1()); }}");
  }

}
