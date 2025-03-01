/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.formats.leipzig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import opennlp.tools.commons.Sample;
import opennlp.tools.util.ObjectStream;

/**
 * A specialization of {@link ObjectStream} that shuffles samples.
 * @param <T> The template parameter which represents
 *            the {@link Sample} type.
 */
class SampleShuffleStream<T> implements ObjectStream<T> {

  private final List<T> bufferedSamples = new ArrayList<>();

  private Iterator<T> sampleIt;

  /**
   * Initializes a {@link SampleShuffleStream} with the specified parameters.
   *
   * @param samples       The {@link ObjectStream} to process.
   * @throws IOException Thrown if IO errors occurred during skip operation.
   */
  SampleShuffleStream(ObjectStream<T> samples) throws IOException {
    T sample;
    while ((sample = samples.read()) != null) {
      bufferedSamples.add(sample);
    }
    Collections.shuffle(bufferedSamples, new Random(23));
    reset();
  }

  @Override
  public T read() throws IOException {
    if (sampleIt.hasNext()) {
      return sampleIt.next();
    }
    return null;
  }

  @Override
  public void reset() throws IOException, UnsupportedOperationException {
    sampleIt = bufferedSamples.iterator();
  }
}
