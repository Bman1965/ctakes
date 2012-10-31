package edu.mayo.bmi.nlp.parser.ae;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.mayo.bmi.uima.core.type.syntax.ConllDependencyNode;
import edu.mayo.bmi.uima.core.type.syntax.BaseToken;
import edu.mayo.bmi.uima.core.type.textspan.Sentence;

public class PosAssigner extends JCasAnnotator_ImplBase{

	// LOG4J logger based on class name
	public Logger logger = Logger.getLogger(getClass().getName());
	    
	@Override
	public void initialize(UimaContext uimaContext) throws ResourceInitializationException {
		super.initialize(uimaContext);	    
	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {

		logger.info(" process(JCas)");
        List<ConllDependencyNode> nodes = new ArrayList<ConllDependencyNode>();
        List<BaseToken> tokens          = new ArrayList<BaseToken>();

        AnnotationIndex nodeIndex = jCas.getAnnotationIndex(ConllDependencyNode.type);
        AnnotationIndex tokenIndex = jCas.getAnnotationIndex(BaseToken.type);
        FSIterator sentences = jCas.getAnnotationIndex(Sentence.type).iterator();

        while (sentences.hasNext()) {
            Sentence sentence = (Sentence) sentences.next();

            tokens.clear();
            nodes.clear();

            FSIterator tokenIterator = tokenIndex.subiterator(sentence);
            while (tokenIterator.hasNext()) {
                tokens.add((BaseToken) tokenIterator.next());
            }

            FSIterator nodeIterator = nodeIndex.subiterator(sentence);
            while (nodeIterator.hasNext()) {
                ConllDependencyNode node = (ConllDependencyNode) nodeIterator.next();
                if (node.getId()!=0) {
                    nodes.add(node);
                }
            }

            ListIterator<BaseToken> itt           = tokens.listIterator();
            ListIterator<ConllDependencyNode> itn = nodes.listIterator();
            BaseToken           bt = null;
            ConllDependencyNode dn = null;
            if (tokens.size()>0 && nodes.size()>0) {
                // iterate through the parallel sorted lists
            	if (itt.hasNext()) bt                  = itt.next();
            	if (itn.hasNext()) dn                  = itn.next();
            	if (dn != null) 
            		if (dn.getId()==0 && itn.hasNext()) 
            			dn = itn.next();
            	while (itt.hasNext() || itn.hasNext()) {
            		if (bt.getBegin()==dn.getBegin() && bt.getEnd()==dn.getEnd()) {
            			dn.setPostag( bt.getPartOfSpeech() );
            			dn.setCpostag( bt.getPartOfSpeech() );  
            			dn.addToIndexes();
            			if (itt.hasNext()) bt = itt.next();
            			if (itn.hasNext()) dn = itn.next();
            		} else if ( bt.getBegin()<dn.getBegin() ) {
            			if (itt.hasNext()) bt = itt.next(); else break;
            		} else if ( bt.getBegin()>dn.getBegin() ) {
            			if (itn.hasNext()) dn = itn.next(); else break;
            		}
            	}
            	if (bt.getBegin()==dn.getBegin() && bt.getEnd()==dn.getEnd()) {
            		dn.setPostag( bt.getPartOfSpeech() );
            		dn.setCpostag( bt.getPartOfSpeech() );  
            		dn.addToIndexes();
            	}
            }
	            
		}
        
	}
	
}