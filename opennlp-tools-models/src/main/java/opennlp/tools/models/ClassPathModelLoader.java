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
package opennlp.tools.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Responsible for loading OpenNLP models from the classpath.
 */
public class ClassPathModelLoader {

  /**
   * Loads a {@link ClassPathModel} from a {@link ClassPathModelEntry}
   *
   * @param entry A valid {@link ClassPathModelEntry}, it must not be {@code null}.
   * @return A {@link ClassPathModel} containing the model resources.
   * @throws IOException thrown if something went wrong during reading resources from the classpath.
   */
  public ClassPathModel load(ClassPathModelEntry entry) throws IOException {
    Objects.requireNonNull(entry, "entry must not be null");
    Objects.requireNonNull(entry.properties(), "entry.properties() must not be null");
    Objects.requireNonNull(entry.model(), "entry.model() must not be null");

    final Properties properties = new Properties();

    if (entry.properties().isPresent()) {
      try (InputStream inputStream = entry.properties().get().toURL().openStream()) {
        properties.load(inputStream);
      }
    }

    final byte[] model;
    try (InputStream inputStream = entry.model().toURL().openStream()) {
      model = inputStream.readAllBytes();
    }

    return new ClassPathModel(properties, model);
  }
}
