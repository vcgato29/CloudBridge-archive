/*
 * Copyright 2010 Cloud.com, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.bridge.service.core.ec2;

import java.util.ArrayList;
import java.util.List;

public class EC2DescribeInstances {

	private List<String> instancesSet = new ArrayList<String>();    // a list of strings identifying instances
	private EC2InstanceFilterSet ifs = null;

	public EC2DescribeInstances() {
	}

	public void addInstanceId( String param ) {
		instancesSet.add( param );
	}
	
	public String[] getInstancesSet() {
		return instancesSet.toArray(new String[0]);
	}
	
	public EC2InstanceFilterSet getFilterSet() {
		return ifs;
	}
	
	public void setFilterSet( EC2InstanceFilterSet param ) {
		ifs = param;
	}
}
