# Spark Cluster in Docker
This section contains instructions on using a spark standalone cluster with 1 master and 1-2 worker nodes.

**Spark version**: 3.2.1

**Scala version**: 2.13.x

## Work with the Spark shell interactively
1. Start the spark cluster
```
docker-compose up -d

```

2. Connect to the master node container. Exit the container at any time by typing in ```exit``` at the command line
```
docker exec -it spark_spark-master_1  /bin/bash
```

3. Start the interactive shell
```
bin/spark-shell
```

4. View the container logs
```
docker logs <container name>
```
For more logging options see: https://docs.docker.com/engine/reference/commandline/logs/


5. Stop spark clusters
```
docker-compose down
```

## Submit a Spark job to the Docker cluster
1. Compile and package the scala application
```
sbt compile assembly
```

2. Copy the application to the Docker shared volume
```
cp target/scala-2.13/2022SpringSparkJob.jar docker/spark/apps
```

3. Start the spark cluster
```
docker-compose up -d
```

4. Connect to the master node container. Exit the container at any time by typing in ```exit``` at the command line
```
docker exec -it spark_spark-master_1  /bin/bash
```

5. Submit the spark job
```
--class "org.cscie88c.week11.SparkAverageTransactionAggregateJob" --master local[4] /opt/spark-apps/2022SpringSparkJob.jar
```

6. Review results
```
ls docker/spark/data/output
```

7. Exit terminal
```
exit
```

8. Shutdown Docker
```
docker-compose down
```