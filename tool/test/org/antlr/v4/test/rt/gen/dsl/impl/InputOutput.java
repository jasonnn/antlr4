package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.dsl.BuilderApi;

/**
 * Created by jason on 4/14/15.
 */
public
class InputOutput<V> implements BuilderApi.Out<V> {
  String input;
  String output;
  String err;

  public
  InputOutput(String input) {
    this.input = input;
  }

  @Override
  public
  BuilderApi.Out<V> expectErrors(String expectedErrors) {
    this.err = expectedErrors; return this;
  }

  @Override
  public
  BuilderApi.Out<V> expectOutput(String expectedOutput) {
    this.output = expectedOutput; return this;
  }
}
