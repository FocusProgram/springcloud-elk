docker-compose -f elasticsearch-two.yml stop

docker-compose -f elasticsearch-two.yml rm --force

docker-compose -f elasticsearch-two.yml up -d
