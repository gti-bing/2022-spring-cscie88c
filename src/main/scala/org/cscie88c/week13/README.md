##Command to create Spooldir connector (Source)

curl -i -X PUT -H "Accept:application/json" \
	-H "Content-Type:application/json" http://localhost:8083/connectors/source-csv-spooldir-00/config \
	-d '{
		"connector.class": "com.github.jcustenborder.kafka.connect.spooldir.SpoolDirLineDelimitedSourceConnector",
		"value.converter": "org.apache.kafka.connect.storage.StringConverter",
		"finished.path": "/opt/kafka-data/processed",
		"input.path": "/opt/kafka-data/unprocessed",
		"error.path": "/opt/kafka-data/error",
		"input.file.pattern": ".*\\.csv",
		"topic": "mls_source_topic_spooldir_00"
	}'

##Command to create JDBC connector (Sink)

curl -i -X PUT -H "Accept:application/json" \
	-H "Content-Type:application/json" http://localhost:8083/connectors/sink-postgresql-jdbc-00/config \
	-d '{
		"connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
		"connection.url": "jdbc:postgresql://ec2-54-211-160-34.compute-1.amazonaws.com:5432/dfsm0hfc2kldt8?sslmode=require",
		"connection.user": "appqwwnqmrhfar",
		"connection.password": "c1a93080ef1e175d0e82568c9959ef1cec51bb2987f7541dfd8b12a3f3877538",
		"key.converter": "org.apache.kafka.connect.storage.StringConverter",
	    "value.converter": "io.confluent.connect.avro.AvroConverter",
	    "value.converter.schema.registry.url": "http://schema-registry:8081",
		"topics": "MLS_SINK_TOPIC_JDBC_00_AVRO",
		"auto.create": true,
		"auto.evolve": true,
		"insert.mode": "upsert",
		"pk.mode": "record_value",
		"pk.fields": "TIMEKEY"
	}'