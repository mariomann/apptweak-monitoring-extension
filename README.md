# AppDynamics AppTweak Monitoring Extension

This extension works only with the standalone machine agent.

##Use Case

The AppTweak monitoring extension gathers metrics for a mobile application (Android or iOS) and sends them to the AppDynamics Metric Browser.


##Installation

1. To build from source, clone this repository and run 'mvn clean install'. This will produce a AppTweakMonitor-VERSION.zip in the target directory. Alternatively, download the latest release archive from [Github](https://github.com/Appdynamics/apptweak-monitoring-extension/releases)
2. Unzip AppTweakMonitor.zip and copy the 'AppTweakMonitor' directory to `<MACHINE_AGENT_HOME>/monitors/`
3. Configure the extension by referring to the below section.
4. Restart the Machine Agent. 
 
In the AppDynamics Metric Browser, look for: Application Infrastructure Performance  | \<Tier\> | Custom Metrics | AppTweak (or the custom path you specified).


## Configuration

Note : Please make sure not to use tab (\t) while editing yaml files. You can validate the yaml file using a [yaml validator](http://yamllint.com/)

1. Configure the AppTweak Extension by editing the config.yml file in `<MACHINE_AGENT_HOME>/monitors/AppTweakMonitor/`.
2. Configure the path to the config.yml file by editing the <task-arguments> in the monitor.xml file in the `<MACHINE_AGENT_HOME>/monitors/AppTweakMonitor/` directory. Below is the sample

     ```
     <task-arguments>
         <!-- config file-->
         <argument name="config-file" is-required="true" default-value="monitors/AppTweakMonitor/config.yml" />
          ....
     </task-arguments>
    ```

## Metrics
### Metric Category: nfsMountStatus
An availability status for any external network file system (NFS) mounts is reported by executing the command `df | grep <fileSystem> | wc -l`.
The file systems to be monitored are to be configured in config.yml. 
```
mobileApplication:
      - applicationId:  "com.db.mm.deutschebank"
        applicationName:    "Deutsche Bank Mobile"
        country:  "de"
        language: "en"
        religion: "android"
      - applicationId:  "529479190"
        applicationName:    "Clash of Clans"
        country:    "de"
        language:   "en"
        religion:   "ios"
```

Note : By default, a Machine agent or a AppServer agent can send a fixed number of metrics to the controller. To change this limit, please follow the instructions mentioned [here](http://docs.appdynamics.com/display/PRO14S/Metrics+Limits).
For eg.
```
    java -Dappdynamics.agent.maxMetrics=2500 -jar machineagent.jar
```

##Custom Dashboard

![](https://github.com/Appdynamics/apptweak-monitoring-extension/blob/master/GooglePlayStore.png?raw=true)

##Contributing

Always feel free to fork and contribute any changes directly here on GitHub.

##Community

Find out more in the [AppSphere] (http://www.appdynamics.com/community/exchange/extension/apptweak-monitoring-extension) community.

##Support

For any questions or feature request, please contact [AppDynamics Support](mailto:help@appdynamics.com).


