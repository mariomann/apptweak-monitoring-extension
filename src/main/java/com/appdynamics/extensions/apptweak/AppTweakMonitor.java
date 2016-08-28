/**
 * Copyright 2013 AppDynamics
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

package com.appdynamics.extensions.apptweak;

import com.appdynamics.extensions.PathResolver;
import com.appdynamics.extensions.apptweak.config.Configuration;
import com.appdynamics.extensions.apptweak.config.MobileApplication;
import com.appdynamics.extensions.yml.YmlReader;
import com.google.common.base.Strings;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class AppTweakMonitor extends AManagedMonitor {

    private static final String text = "AppTweak Monitoring Task completed:";
    private static Logger logger = Logger.getLogger(AppTweakMonitor.class);

    public AppTweakMonitor() {
        System.out.println(logVersion());
    }

    public TaskOutput execute(Map<String, String> taskArgs, TaskExecutionContext taskExecutionContext)
            throws TaskExecutionException {
        if (taskArgs != null) {
            logger.info(logVersion());
            try {
                String configFilename = getConfigFilename(taskArgs.get("config-file"));
                Configuration config = YmlReader.readFromFile(configFilename, Configuration.class);

                Map<String, Object> statsMap = populateMetrics(config);
                printNestedMap(statsMap, config.getMetricPrefix());

                logger.info(text + " successfully");
                return new TaskOutput(text + " successfully");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                logger.error(text + " with failures ", e);
            }
        }
        throw new TaskExecutionException(text + " with failures");
    }

    private Map<String, Object> populateMetrics(Configuration config) {
        Stats stats = new Stats(logger);
        Map<String, Object> statsMap = new HashMap<>();
        Map<String, Object> map;

        MobileApplication[] applications = config.getMobileApplication();
        if (applications != null) {
            for (MobileApplication app : applications) {
                if ((map = stats.getAppRating(app, config.getToken())) != null) {
                    statsMap.put(app.getApplicationId(), map);
                }
            }
        }
        return statsMap;
    }

    private void printNestedMap(Map<String, Object> map, String metricPath) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Map) {
                printNestedMap((Map) val, metricPath + key + "|");
            } else {
                printMetric(metricPath + key, val);
            }
        }
    }

    private void printMetric(String metricName, Object metricValue) {
        if (metricValue != null) {
            MetricWriter metricWriter = getMetricWriter(metricName,
                    MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
                    MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
                    MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE
            );
            try {
                metricWriter.printMetric((String) metricValue);
            } catch (Exception e) {
                logger.error("Error while reporting metric " + metricName + " " + metricValue, e);
            }
        }
    }

    private String getConfigFilename(String filename) {
        if (filename == null) {
            return "";
        }
        // for absolute paths
        if (new File(filename).exists()) {
            return filename;
        }
        // for relative paths
        File jarPath = PathResolver.resolveDirectory(AManagedMonitor.class);
        String configFileName = "";
        if (!Strings.isNullOrEmpty(filename)) {
            configFileName = jarPath + File.separator + filename;
        }
        return configFileName;
    }

    private String logVersion() {
        String msg = "Using Monitor Version [" + getImplementationVersion() + "]";
        return msg;
    }

    public static String getImplementationVersion() {
        return AppTweakMonitor.class.getPackage().getImplementationTitle();
    }

}
