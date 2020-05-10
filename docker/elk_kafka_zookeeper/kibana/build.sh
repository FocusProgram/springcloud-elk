docker-compose -f kibana.yml stop

docker-compose -f kibana.yml rm --force

docker-compose -f kibana.yml up -d
