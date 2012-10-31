/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
package org.cleartk.classifier.chunking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.uimafit.util.JCasUtil;

/**
 * Chunking with Begin-Inside-Outside labels
 * 
 * <br>
 * Copyright (c) 2012, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Steven Bethard
 */
public class BIOChunking<SUB_CHUNK_TYPE extends Annotation, CHUNK_TYPE extends Annotation> extends
    Chunking_ImplBase<SUB_CHUNK_TYPE, CHUNK_TYPE> {

  public BIOChunking(
      Class<? extends SUB_CHUNK_TYPE> subChunkClass,
      Class<? extends CHUNK_TYPE> chunkClass) {
    super(subChunkClass, chunkClass, null);
  }

  public BIOChunking(
      Class<? extends SUB_CHUNK_TYPE> subChunkClass,
      Class<? extends CHUNK_TYPE> chunkClass,
      String featureName) {
    super(subChunkClass, chunkClass, featureName);
  }

  @Override
  protected Map<SUB_CHUNK_TYPE, String> getSubChunkToOutcomeMap(
      JCas jCas,
      List<SUB_CHUNK_TYPE> chunkComponents,
      List<CHUNK_TYPE> chunks) {
    Feature feature = this.getFeature(jCas);
    Map<SUB_CHUNK_TYPE, String> subChunkToOutcome = new HashMap<SUB_CHUNK_TYPE, String>();
    for (CHUNK_TYPE chunk : chunks) {
      String suffix = this.getOutcomeSuffix(chunk, feature);
      for (SUB_CHUNK_TYPE chunkComponent : JCasUtil.selectCovered(this.subChunkClass, chunk)) {
        if (chunkComponent.getBegin() == chunk.getBegin()) {
          subChunkToOutcome.put(chunkComponent, "B" + suffix);
        } else {
          subChunkToOutcome.put(chunkComponent, "I" + suffix);
        }
      }
    }
    return subChunkToOutcome;
  }

  @Override
  protected boolean isEndOfChunk(
      char currPrefix,
      String currLabel,
      char nextPrefix,
      String nextLabel) {
    return nextPrefix == 'O' || nextPrefix == 'B' || !nextLabel.equals(currLabel);
  }

}