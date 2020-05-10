docker-compose -f elasticsearch.yml stop

docker-compose -f elasticsearch.yml rm --force

docker-compose -f elasticsearch.yml up -d
