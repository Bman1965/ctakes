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
package edu.mayo.bmi.fsm.condition;

import net.openai.util.fsm.Condition;
import edu.mayo.bmi.fsm.token.PunctuationToken;

/**
 * 
 * @author Mayo Clinic
 */
@SuppressWarnings("serial")
public class PunctuationValueCondition extends Condition {
	private char iv_punctChar;

	/**
	 * Constructor
	 * 
	 * @param t
	 */
	public PunctuationValueCondition(char c) {
		iv_punctChar = c;
	}

	/**
	 * Called to check if the conditional meets the criteria defined by this
	 * state.
	 */
	public boolean satisfiedBy(Object conditional) {
		if (conditional instanceof PunctuationToken) {
			PunctuationToken t = (PunctuationToken) conditional;
			if (t.getChar() == iv_punctChar) {
				return true;
			}
		}

		return false;
	}
}