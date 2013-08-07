/*
 * Copyright: (c) 2009   Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
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
package edu.mayo.bmi.uima.core.ae;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.mayo.bmi.uima.core.type.textspan.Segment;
import edu.mayo.bmi.uima.core.util.DocumentIDAnnotationUtil;

/**
 * Creates a single segment annotation that spans the entire document. This is
 * useful for running a TAE without a CasInitializer that would normally create
 * the segment annotations.
 * 
 * @author Mayo Clinic
 */
public class SimpleSegmentAnnotator extends JCasAnnotator_ImplBase {
	private String segmentId;

	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);

		segmentId = (String) aContext.getConfigParameterValue("SegmentID");
		if (segmentId == null) {
			segmentId = "SIMPLE_SEGMENT";
		}
	}

	/**
	 * Entry point for processing.
	 */
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		Segment segment = new Segment(jCas);
		segment.setBegin(0);
		String text = jCas.getDocumentText();
		if (text == null) {
			String docId = DocumentIDAnnotationUtil.getDocumentID(jCas);
			throw new AnalysisEngineProcessException("text is null for docId="
					+ docId, null);
		}
		segment.setEnd(jCas.getDocumentText().length());
		segment.setId(segmentId);
		segment.addToIndexes();
	}
}