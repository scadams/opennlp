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

import java.io.File;
import java.io.IOException;

import opennlp.tools.cmdline.ArgumentParser;
import opennlp.tools.cmdline.ArgumentParser.OptionalParameter;
import opennlp.tools.cmdline.ArgumentParser.ParameterDescription;
import opennlp.tools.cmdline.StreamFactoryRegistry;
import opennlp.tools.cmdline.TerminateToolException;
import opennlp.tools.cmdline.params.EncodingParameter;
import opennlp.tools.commons.Internal;
import opennlp.tools.formats.AbstractSampleStreamFactory;
import opennlp.tools.langdetect.LanguageSample;
import opennlp.tools.util.ObjectStream;

/**
 * <b>Note:</b>
 * Do not use this class, internal use only!
 *
 * @see LeipzigLanguageSampleStream
 */
@Internal
public class LeipzigLanguageSampleStreamFactory extends
        AbstractSampleStreamFactory<LanguageSample, LeipzigLanguageSampleStreamFactory.Parameters> {

  public interface Parameters extends EncodingParameter {
    @ParameterDescription(valueName = "sentencesDir",
        description = "dir with Leipzig sentences to be used")
    File getSentencesDir();

    @ParameterDescription(valueName = "sentencesPerSample",
        description = "number of sentences per sample")
    Integer getSentencesPerSample();

    @ParameterDescription(valueName = "samplesPerLanguage",
        description = "number of samples per language")
    Integer getSamplesPerLanguage();

    @ParameterDescription(valueName = "samplesToSkip",
        description = "number of samples to skip before returning")
    @OptionalParameter(defaultValue = "0")
    Integer getSamplesToSkip();
  }

  protected LeipzigLanguageSampleStreamFactory(Class<Parameters> params) {
    super(params);
  }

  public static void registerFactory() {
    StreamFactoryRegistry.registerFactory(LanguageSample.class,
        "leipzig", new LeipzigLanguageSampleStreamFactory(Parameters.class));
  }

  @Override
  public ObjectStream<LanguageSample> create(String[] args) {
    if (args == null) {
      throw new IllegalArgumentException("Passed args must not be null!");
    }
    Parameters p = ArgumentParser.parse(args, Parameters.class);
    File sentencesFileDir = p.getSentencesDir();

    try {
      return new SampleSkipStream<>(new SampleShuffleStream<>(new LeipzigLanguageSampleStream(
              sentencesFileDir, p.getSentencesPerSample(),
              p.getSamplesPerLanguage() + p.getSamplesToSkip())), p.getSamplesToSkip());
    } catch (IOException e) {
      throw new TerminateToolException(-1, "IO error while opening sample data.", e);
    }
  }
}
