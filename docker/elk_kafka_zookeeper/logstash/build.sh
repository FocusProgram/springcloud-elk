docker-compose -f logstash.yml stop

docker-compose -f logstash.yml rm --force

docker-compose -f logstash.yml up -d
