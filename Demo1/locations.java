package locations;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.sling.commons.json.JSONObject;


/**
 * Implements a streaming windowed version of the "WordCount" program.
 *
 * This program connects to a server socket and reads strings from the socket.
 * The easiest way to try this out is to open a text server (at port 12345)
 * using the <i>netcat</i> tool via
 * <pre>
 * nc -l 12345
 * </pre>
 * and run this example with the hostname and the port as arguments.
 */
@SuppressWarnings("serial")
public class locations {

	public static void main(String[] args) throws Exception {

		// the host and the port to connect to
		final String hostname = "localhost";
		final int port = 9000 ;

		// get the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// get input data by connecting to the socket
		DataStream<String> text = env.socketTextStream(hostname, port, "\n");


		DataStream<String> locations = text
				.map(new MapFunction<String, String>(){
					public String map(String value) { {
		               try
		               {
		                   JSONObject obj = location(value);
		                   return obj.toString() ;

		               }
		               catch(Exception e)
		               {
		                    return "Error";
		               }

		            }
		}}); 

		// print the results with a single thread, rather than in parallel
		locations.print().setParallelism(1);

		env.execute("locations");
	}

	public static JSONObject location(String value)
	{
		JSONObject obj = null;
		try{
			obj = new JSONObject() ;
			obj.put("DEVICE ID", value) ;
			obj.put("LATITUDE", 25.755891) ;
			obj.put("LONGITUDE", 28.2337818) ;
			obj.put("ALTITUDE", 1500) ;
		}
		catch(Exception ex) 
		{
			ex.printStackTrace(); 
		}
		return obj ;
	}
}
