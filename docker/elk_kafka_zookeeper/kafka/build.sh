docker-compose -f kafka.yml stop

docker-compose -f kafka.yml rm --force

docker-compose -f kafka.yml up -d
