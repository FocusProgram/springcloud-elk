docker-compose -f elasticsearch-one.yml stop

docker-compose -f elasticsearch-one.yml rm --force

docker-compose -f elasticsearch-one.yml up -d
